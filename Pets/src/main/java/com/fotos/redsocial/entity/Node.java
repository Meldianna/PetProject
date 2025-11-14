package com.fotos.redsocial.entity;

public class Node implements Comparable<Node> {
    private Location location;
    private double distance;

    public Node(Location location, double distance) {
        this.location = location;
        this.distance = distance;
    }

    public Location getLocation() {
        return location;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.distance, other.distance);
    }
}