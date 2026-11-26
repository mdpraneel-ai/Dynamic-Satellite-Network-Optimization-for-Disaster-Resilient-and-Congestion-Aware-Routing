package sim;

import algo.HybridRecoveryPlanner;
import algo.RoutePlanner;
import model.Graph;
import model.RouteResult;

import java.util.ArrayList;
import java.util.List;

public class TraversalSimulator {
    public static RouteResult executeWithDynamicFailures(
            Graph graph,
            RoutePlanner planner,
            int source,
            int destination,
            List<FailureEvent> events) {

        if (planner instanceof HybridRecoveryPlanner) {
            return ((HybridRecoveryPlanner) planner).findRouteWithRecovery(graph, source, destination, events);
        }

        RouteResult initial = planner.findRoute(graph, source, destination);
        if (!initial.isSuccess() || events == null || events.isEmpty()) {
            return initial;
        }

        // Non-recovery algorithms get invalidated if the failure intersects their chosen path.
        List<Integer> path = new ArrayList<>(initial.getPath());
        for (FailureEvent event : events) {
            int hop = event.getHop();
            if (hop <= 0 || hop >= path.size()) continue;

            if (event.getType() == FailureEvent.Type.NODE) {
                int failedNode = event.getNodeId();
                for (int node : path) {
                    if (node == failedNode) {
                        return new RouteResult(
                                planner.getName(),
                                initial.getPath(),
                                Double.POSITIVE_INFINITY,
                                initial.getRuntimeMs(),
                                0,
                                false,
                                "Path broken by dynamic node failure"
                        );
                    }
                }
            } else {
                int from = event.getFrom();
                int to = event.getTo();
                for (int i = 0; i < path.size() - 1; i++) {
                    if (path.get(i) == from && path.get(i + 1) == to) {
                        return new RouteResult(
                                planner.getName(),
                                initial.getPath(),
                                Double.POSITIVE_INFINITY,
                                initial.getRuntimeMs(),
                                0,
                                false,
                                "Path broken by dynamic edge failure"
                        );
                    }
                }
            }
        }

        return initial;
    }
}
