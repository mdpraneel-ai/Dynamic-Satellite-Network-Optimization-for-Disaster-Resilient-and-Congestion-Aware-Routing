package sim;

public class FailureEvent {
    public enum Type {
        NODE,
        EDGE
    }

    private final int hop;
    private final Type type;
    private final int from;
    private final int to;
    private final int nodeId;

    private FailureEvent(int hop, Type type, int from, int to, int nodeId) {
        this.hop = hop;
        this.type = type;
        this.from = from;
        this.to = to;
        this.nodeId = nodeId;
    }

    public static FailureEvent nodeFailure(int hop, int nodeId) {
        return new FailureEvent(hop, Type.NODE, -1, -1, nodeId);
    }

    public static FailureEvent edgeFailure(int hop, int from, int to) {
        return new FailureEvent(hop, Type.EDGE, from, to, -1);
    }

    public int getHop() {
        return hop;
    }

    public Type getType() {
        return type;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getNodeId() {
        return nodeId;
    }
}
