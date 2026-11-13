package algo;

import model.Edge;
import model.Graph;
import model.RouteResult;
import sim.FailureEvent;

import java.util.*;

public class HybridRecoveryPlanner extends DijkstraPlanner {
    private final double alpha;
    private final double beta;

    public HybridRecoveryPlanner(double alpha, double beta, double dynamicFailureRate, long seed) {
        this.alpha = alpha;
        this.beta = beta;
        // kept for API compatibility; recovery events are now injected externally
    }

    @Override
    public String getName() {
        return "HybridRecovery";
    }

    @Override
    protected double edgeWeight(Edge edge) {
        double congestionPenalty = alpha * edge.getCongestionRatio();
        double failurePenalty = beta * edge.getFailureRisk();
        return edge.getBaseLatency() * (1.0 + congestionPenalty + failurePenalty);
    }

    @Override
    public RouteResult findRoute(Graph graph, int source, int destination) {
        return super.findRoute(graph, source, destination);
    }

    public RouteResult findRouteWithRecovery(Graph graph, int source, int destination, List<FailureEvent> events) {
        long start = System.nanoTime();
        if (!graph.hasNode(source) || !graph.hasNode(destination)) {
            return new RouteResult(getName(), Collections.emptyList(), Double.POSITIVE_INFINITY, 0, 0, false, "Invalid endpoints");
        }

        List<Integer> currentPath = shortestPath(graph, source, destination);
        if (currentPath.isEmpty()) {
            long runtime = (System.nanoTime() - start) / 1_000_000;
            return new RouteResult(getName(), currentPath, Double.POSITIVE_INFINITY, runtime, 0, false, "No initial path");
        }

        List<Integer> executed = new ArrayList<>();
        executed.add(source);
        double totalCost = 0.0;
        int reroutes = 0;

        int currentIndex = 0;
        int currentNode = source;
        Set<String> appliedEvents = new HashSet<>();

        while (currentNode != destination) {
            if (currentIndex >= currentPath.size() - 1) {
                break;
            }

            int nextNode = currentPath.get(currentIndex + 1);

            FailureEvent matchingEvent = findMatchingEvent(events, currentIndex + 1, appliedEvents);
            if (matchingEvent != null) {
                appliedEvents.add(eventKey(matchingEvent));
                applyEvent(graph, matchingEvent);
                List<Integer> repaired = shortestPath(graph, currentNode, destination);
                reroutes++;
                if (repaired.isEmpty()) {
                    long runtime = (System.nanoTime() - start) / 1_000_000;
                    return new RouteResult(getName(), executed, Double.POSITIVE_INFINITY, runtime, reroutes, false, "Recovery failed after dynamic failure");
                }
                currentPath = repaired;
                currentIndex = 0;
                continue;
            }

            if (!graph.getNode(nextNode).isActive()) {
                List<Integer> repaired = shortestPath(graph, currentNode, destination);
                reroutes++;
                if (repaired.isEmpty()) {
                    long runtime = (System.nanoTime() - start) / 1_000_000;
                    return new RouteResult(getName(), executed, Double.POSITIVE_INFINITY, runtime, reroutes, false, "Recovery failed after encountered node failure");
                }
                currentPath = repaired;
                currentIndex = 0;
                continue;
            }

            if (graph.getEdge(currentNode, nextNode) == null || !graph.getEdge(currentNode, nextNode).isActive()) {
                List<Integer> repaired = shortestPath(graph, currentNode, destination);
                reroutes++;
                if (repaired.isEmpty()) {
                    long runtime = (System.nanoTime() - start) / 1_000_000;
                    return new RouteResult(getName(), executed, Double.POSITIVE_INFINITY, runtime, reroutes, false, "Recovery failed after encountered link failure");
                }
                currentPath = repaired;
                currentIndex = 0;
                continue;
            }

            totalCost += edgeWeight(graph.getEdge(currentNode, nextNode));
            currentNode = nextNode;
            executed.add(currentNode);
            currentIndex++;
        }

        boolean success = currentNode == destination;
        long runtime = (System.nanoTime() - start) / 1_000_000;
        return new RouteResult(getName(), executed, success ? totalCost : Double.POSITIVE_INFINITY, runtime, reroutes, success, success ? "Route recovered dynamically" : "Route incomplete");
    }

    private FailureEvent findMatchingEvent(List<FailureEvent> events, int hop, Set<String> appliedEvents) {
        if (events == null) {
            return null;
        }
        for (FailureEvent event : events) {
            if (event.getHop() == hop && !appliedEvents.contains(eventKey(event))) {
                return event;
            }
        }
        return null;
    }

    private String eventKey(FailureEvent event) {
        return event.getType() + ":" + event.getHop() + ":" + event.getFrom() + ":" + event.getTo() + ":" + event.getNodeId();
    }

    private void applyEvent(Graph graph, FailureEvent event) {
        if (event.getType() == FailureEvent.Type.NODE) {
            graph.failNode(event.getNodeId());
        } else if (event.getType() == FailureEvent.Type.EDGE) {
            graph.failEdge(event.getFrom(), event.getTo());
        }
    }

    private List<Integer> shortestPath(Graph graph, int source, int destination) {
        return super.findRoute(graph, source, destination).getPath();
    }
}
