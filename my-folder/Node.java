package model;

public class Node {
    public enum Type {
        SATELLITE,
        GROUND_STATION
    }

    private final int id;
    private final Type type;
    private boolean active;

    public Node(int id, Type type) {
        this.id = id;
        this.type = type;
        this.active = true;
    }

    public Node(Node other) {
        this.id = other.id;
        this.type = other.type;
        this.active = other.active;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
