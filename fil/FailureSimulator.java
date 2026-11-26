package sim;

import model.Graph;

import java.util.*;

public class FailureSimulator {
    private final Random random;

    public FailureSimulator(long seed) {
        this.random = new Random(seed);
    }

    public void applyStaticFailures(Graph graph, double nodeFailureRate, double edgeFailureRate) {
        for (Integer nodeId : graph.getNodeIds()) {
            if (random.nextDouble() < nodeFailureRate) {
                graph.failNode(nodeId);
            }
        }

        for (Integer nodeId : graph.getNodeIds()) {
            graph.getNeighbors(nodeId).forEach(edge -> {
                if (random.nextDouble() < edgeFailureRate) {
                    edge.setActive(false);
                }
            });
        }
    }

    public List<FailureEvent> createDynamicFailureSchedule(Graph graph, List<Integer> path, double eventRate) {
        List<FailureEvent> events = new ArrayList<>();
        if (path == null || path.size() < 3) {
            return events;
        }

        for (int hop = 1; hop < path.size() - 1; hop++) {
            if (random.nextDouble() < eventRate) {
                if (random.nextBoolean()) {
                    events.add(FailureEvent.nodeFailure(hop, path.get(hop)));
                } else {
                    events.add(FailureEvent.edgeFailure(hop, path.get(hop - 1), path.get(hop)));
                }
                break;
            }
        }
        return events;
    }
}
