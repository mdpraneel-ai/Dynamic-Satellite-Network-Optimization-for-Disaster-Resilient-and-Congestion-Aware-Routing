package util;

import model.RouteResult;

public class BenchmarkRecord {
    private final String algorithm;
    private final int nodes;
    private final int edges;
    private final double failureRate;
    private final double congestionLevel;
    private final RouteResult result;

    public BenchmarkRecord(String algorithm, int nodes, int edges, double failureRate, double congestionLevel, RouteResult result) {
        this.algorithm = algorithm;
        this.nodes = nodes;
        this.edges = edges;
        this.failureRate = failureRate;
        this.congestionLevel = congestionLevel;
        this.result = result;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getNodes() {
        return nodes;
    }

    public int getEdges() {
        return edges;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public double getCongestionLevel() {
        return congestionLevel;
    }

    public RouteResult getResult() {
        return result;
    }
}
