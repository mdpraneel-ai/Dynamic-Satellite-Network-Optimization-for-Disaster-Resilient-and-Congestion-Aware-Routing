package model;

public class Edge {
    private final int from;
    private final int to;
    private final int baseLatency;
    private final int capacity;
    private int currentLoad;
    private boolean active;
    private double failureRisk;

    public Edge(int from, int to, int baseLatency, int capacity) {
        this.from = from;
        this.to = to;
        this.baseLatency = baseLatency;
        this.capacity = Math.max(1, capacity);
        this.currentLoad = 0;
        this.active = true;
        this.failureRisk = 0.0;
    }

    public Edge(Edge other) {
        this.from = other.from;
        this.to = other.to;
        this.baseLatency = other.baseLatency;
        this.capacity = other.capacity;
        this.currentLoad = other.currentLoad;
        this.active = other.active;
        this.failureRisk = other.failureRisk;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getBaseLatency() {
        return baseLatency;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = Math.max(0, currentLoad);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getFailureRisk() {
        return failureRisk;
    }

    public void setFailureRisk(double failureRisk) {
        this.failureRisk = Math.max(0.0, failureRisk);
    }

    public double getCongestionRatio() {
        return Math.min(2.0, (double) currentLoad / (double) capacity);
    }
}
