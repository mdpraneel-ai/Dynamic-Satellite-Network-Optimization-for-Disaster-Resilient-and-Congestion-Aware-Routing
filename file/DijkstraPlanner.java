package algo;

import model.Edge;
import model.Graph;
import model.Node;
import model.RouteResult;

import java.util.*;

public class DijkstraPlanner implements RoutePlanner {

    protected static class State {
        int node;
        double cost;

        State(int node, double cost) {
            this.node = node;
            this.cost = cost;
        }
    }

    protected static class Prev {
        int node;
        double weight;

        Prev(int node, double weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    @Override
    public String getName() {
        return "Dijkstra";
    }

    protected double edgeWeight(Edge edge) {
        return edge.getBaseLatency();
    }

    @Override
    public RouteResult findRoute(Graph graph, int source, int destination) {
        long start = System.nanoTime();
        List<Integer> path = new ArrayList<>();
        Map<Integer, Double> dist = new HashMap<>();
        Map<Integer, Integer> prev = new HashMap<>();

        for (Integer nodeId : graph.getNodeIds()) {
            dist.put(nodeId, Double.POSITIVE_INFINITY);
        }
        if (!graph.hasNode(source) || !graph.hasNode(destination)) {
            return new RouteResult(getName(), path, Double.POSITIVE_INFINITY, 0, 0, false, "Invalid endpoints");
        }
        if (!graph.getNode(source).isActive() || !graph.getNode(destination).isActive()) {
            return new RouteResult(getName(), path, Double.POSITIVE_INFINITY, 0, 0, false, "Source/destination failed");
        }

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a.cost));
        dist.put(source, 0.0);
        pq.add(new State(source, 0.0));

        Set<Integer> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            State cur = pq.poll();
            if (visited.contains(cur.node)) {
                continue;
            }
            visited.add(cur.node);

            if (cur.node == destination) {
                break;
            }

            for (Edge edge : graph.getNeighbors(cur.node)) {
                if (!edge.isActive()) continue;
                Node nextNode = graph.getNode(edge.getTo());
                if (nextNode == null || !nextNode.isActive()) continue;

                double newDist = dist.get(cur.node) + edgeWeight(edge);
                if (newDist < dist.getOrDefault(edge.getTo(), Double.POSITIVE_INFINITY)) {
                    dist.put(edge.getTo(), newDist);
                    prev.put(edge.getTo(), cur.node);
                    pq.add(new State(edge.getTo(), newDist));
                }
            }
        }

        if (!prev.containsKey(destination) && source != destination) {
            long runtime = (System.nanoTime() - start) / 1_000_000;
            return new RouteResult(getName(), path, Double.POSITIVE_INFINITY, runtime, 0, false, "No path");
        }

        int curr = destination;
        path.add(curr);
        while (curr != source) {
            curr = prev.get(curr);
            path.add(curr);
        }
        Collections.reverse(path);

        double cost = dist.get(destination);
        long runtime = (System.nanoTime() - start) / 1_000_000;
        return new RouteResult(getName(), path, cost, runtime, 0, true, "Static shortest path");
    }
}
