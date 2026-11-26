package util;

import model.RouteResult;

import java.util.*;

public class MetricsCollector {
    public static class Summary {
        public final String algorithm;
        public final int runs;
        public final double avgRuntime;
        public final double avgCost;
        public final double successRate;
        public final double avgHops;
        public final double avgReroutes;

        public Summary(String algorithm, int runs, double avgRuntime, double avgCost, double successRate, double avgHops, double avgReroutes) {
            this.algorithm = algorithm;
            this.runs = runs;
            this.avgRuntime = avgRuntime;
            this.avgCost = avgCost;
            this.successRate = successRate;
            this.avgHops = avgHops;
            this.avgReroutes = avgReroutes;
        }
    }

    public static Summary summarize(String algorithm, List<RouteResult> results) {
        int runs = results.size();
        double runtime = 0.0;
        double cost = 0.0;
        int successCount = 0;
        double hops = 0.0;
        double reroutes = 0.0;

        for (RouteResult r : results) {
            runtime += r.getRuntimeMs();
            if (r.isSuccess() && Double.isFinite(r.getTotalCost())) {
                cost += r.getTotalCost();
                successCount++;
                hops += Math.max(0, r.getPath().size() - 1);
            }
            reroutes += r.getRerouteCount();
        }

        double avgCost = successCount == 0 ? Double.POSITIVE_INFINITY : cost / successCount;
        double avgHops = successCount == 0 ? 0.0 : hops / successCount;

        return new Summary(
                algorithm,
                runs,
                runtime / runs,
                avgCost,
                (double) successCount / (double) runs,
                avgHops,
                reroutes / runs
        );
    }
}
