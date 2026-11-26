package sim;

import model.Edge;
import model.Graph;
import model.Node;

import java.util.Random;

public class TopologyGenerator {
    private final Random random;

    public TopologyGenerator(long seed) {
        this.random = new Random(seed);
    }

    public Graph generate(int satellites, int groundStations, int extraEdges) {
        Graph graph = new Graph();
        int total = satellites + groundStations;

        for (int i = 0; i < satellites; i++) {
            graph.addNode(i, Node.Type.SATELLITE);
        }
        for (int i = satellites; i < total; i++) {
            graph.addNode(i, Node.Type.GROUND_STATION);
        }

        // Ring backbone for satellites
        for (int i = 0; i < satellites; i++) {
            int next = (i + 1) % satellites;
            if (i != next) {
                graph.addUndirectedEdge(i, next, randomLatency(4, 12), randomCapacity(80, 200));
            }
        }

        // Connect ground stations to nearby satellites
        for (int g = satellites; g < total; g++) {
            int a = random.nextInt(Math.max(1, satellites));
            int b = (a + 3 + random.nextInt(Math.max(1, satellites / 2 + 1))) % Math.max(1, satellites);
            graph.addUndirectedEdge(g, a, randomLatency(8, 18), randomCapacity(40, 120));
            if (b != a) {
                graph.addUndirectedEdge(g, b, randomLatency(8, 18), randomCapacity(40, 120));
            }
        }

        // Add random cross-links
        for (int i = 0; i < extraEdges; i++) {
            int u = random.nextInt(total);
            int v = random.nextInt(total);
            if (u == v) continue;
            if (graph.getEdge(u, v) != null) continue;
            graph.addUndirectedEdge(u, v, randomLatency(2, 15), randomCapacity(60, 180));
        }

        return graph;
    }

    private int randomLatency(int minInclusive, int maxInclusive) {
        return minInclusive + random.nextInt(maxInclusive - minInclusive + 1);
    }

    private int randomCapacity(int minInclusive, int maxInclusive) {
        return minInclusive + random.nextInt(maxInclusive - minInclusive + 1);
    }

    public void assignCongestion(Graph graph, double loadFactor) {
        for (Integer nodeId : graph.getNodeIds()) {
            for (Edge edge : graph.getNeighbors(nodeId)) {
                int load = (int) Math.round(edge.getCapacity() * (0.15 + loadFactor * random.nextDouble()));
                edge.setCurrentLoad(load);
                edge.setFailureRisk(Math.min(0.95, 0.05 + (double) load / (double) edge.getCapacity() * 0.4));
            }
        }
    }
}
