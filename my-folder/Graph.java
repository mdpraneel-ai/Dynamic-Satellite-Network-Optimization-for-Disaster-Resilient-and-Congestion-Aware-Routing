package model;

import java.util.*;

public class Graph {
    private final Map<Integer, Node> nodes;
    private final Map<Integer, List<Edge>> adjacency;

    public Graph() {
        this.nodes = new LinkedHashMap<>();
        this.adjacency = new HashMap<>();
    }

    public Graph(Graph other) {
        this();
        for (Node node : other.nodes.values()) {
            Node copy = new Node(node);
            this.nodes.put(copy.getId(), copy);
        }
        for (Map.Entry<Integer, List<Edge>> entry : other.adjacency.entrySet()) {
            List<Edge> list = new ArrayList<>();
            for (Edge edge : entry.getValue()) {
                list.add(new Edge(edge));
            }
            this.adjacency.put(entry.getKey(), list);
        }
    }

    public void addNode(int id, Node.Type type) {
        nodes.putIfAbsent(id, new Node(id, type));
        adjacency.putIfAbsent(id, new ArrayList<>());
    }

    public void addUndirectedEdge(int from, int to, int baseLatency, int capacity) {
        addDirectedEdge(from, to, baseLatency, capacity);
        addDirectedEdge(to, from, baseLatency, capacity);
    }

    public void addDirectedEdge(int from, int to, int baseLatency, int capacity) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            throw new IllegalArgumentException("Both endpoints must exist before adding an edge.");
        }
        adjacency.putIfAbsent(from, new ArrayList<>());
        adjacency.get(from).add(new Edge(from, to, baseLatency, capacity));
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public Collection<Node> getNodes() {
        return nodes.values();
    }

    public List<Edge> getNeighbors(int nodeId) {
        return adjacency.getOrDefault(nodeId, Collections.emptyList());
    }

    public Set<Integer> getNodeIds() {
        return nodes.keySet();
    }

    public boolean hasNode(int id) {
        return nodes.containsKey(id);
    }

    public void failNode(int nodeId) {
        Node node = nodes.get(nodeId);
        if (node != null) {
            node.setActive(false);
        }
    }

    public void recoverNode(int nodeId) {
        Node node = nodes.get(nodeId);
        if (node != null) {
            node.setActive(true);
        }
    }

    public void failEdge(int from, int to) {
        for (Edge edge : getNeighbors(from)) {
            if (edge.getTo() == to) {
                edge.setActive(false);
            }
        }
    }

    public void recoverEdge(int from, int to) {
        for (Edge edge : getNeighbors(from)) {
            if (edge.getTo() == to) {
                edge.setActive(true);
            }
        }
    }

    public Edge getEdge(int from, int to) {
        for (Edge edge : getNeighbors(from)) {
            if (edge.getTo() == to) {
                return edge;
            }
        }
        return null;
    }

    public int countDirectedEdges() {
        int total = 0;
        for (List<Edge> edges : adjacency.values()) {
            total += edges.size();
        }
        return total;
    }

    public int countUndirectedEdgePairs() {
        return countDirectedEdges() / 2;
    }

    public boolean isValidPath(List<Integer> path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        for (int i = 0; i < path.size(); i++) {
            Node node = nodes.get(path.get(i));
            if (node == null || !node.isActive()) {
                return false;
            }
            if (i < path.size() - 1) {
                Edge edge = getEdge(path.get(i), path.get(i + 1));
                if (edge == null || !edge.isActive()) {
                    return false;
                }
            }
        }
        return true;
    }

    public double computePathCost(List<Integer> path) {
        if (path == null || path.size() < 2) {
            return Double.POSITIVE_INFINITY;
        }
        double cost = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Edge edge = getEdge(path.get(i), path.get(i + 1));
            if (edge == null) {
                return Double.POSITIVE_INFINITY;
            }
            cost += edge.getBaseLatency();
        }
        return cost;
    }
}
