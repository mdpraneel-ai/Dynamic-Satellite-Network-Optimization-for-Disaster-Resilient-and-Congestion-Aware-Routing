package algo;

import model.Edge;
import model.Graph;
import model.Node;
import model.RouteResult;

public class CongestionAwarePlanner extends DijkstraPlanner {
    private final double alpha;
    private final double beta;

    public CongestionAwarePlanner(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public String getName() {
        return "CongestionAware";
    }

    @Override
    protected double edgeWeight(Edge edge) {
        double congestionPenalty = alpha * edge.getCongestionRatio();
        double failurePenalty = beta * edge.getFailureRisk();
        return edge.getBaseLatency() * (1.0 + congestionPenalty + failurePenalty);
    }
}
