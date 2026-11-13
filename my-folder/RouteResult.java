package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteResult {
    private final List<Integer> path;
    private final double totalCost;
    private final long runtimeMs;
    private final int rerouteCount;
    private final boolean success;
    private final String algorithm;
    private final String notes;

    public RouteResult(String algorithm, List<Integer> path, double totalCost, long runtimeMs, int rerouteCount, boolean success, String notes) {
        this.algorithm = algorithm;
        this.path = new ArrayList<>(path);
        this.totalCost = totalCost;
        this.runtimeMs = runtimeMs;
        this.rerouteCount = rerouteCount;
        this.success = success;
        this.notes = notes == null ? "" : notes;
    }

    public List<Integer> getPath() {
        return Collections.unmodifiableList(path);
    }

    public double getTotalCost() {
        return totalCost;
    }

    public long getRuntimeMs() {
        return runtimeMs;
    }

    public int getRerouteCount() {
        return rerouteCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getNotes() {
        return notes;
    }
}
