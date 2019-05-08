package org.ploxie.pathfinder.web;

import java.util.LinkedList;

public class WebNode{

    private final int x;
    private final int y;
    private final int plane;

    private final LinkedList<WebNodeConnection> connections;

    private WebNode parent;

    private double gCost;
    private double heuristic;

    public WebNode(int x, int y, int plane) {
        this.x = x;
        this.y = y;
        this.plane = plane;

        this.connections = new LinkedList<WebNodeConnection>();
    }

    protected boolean addConnection(WebNode node) {
        WebNodeConnection connection = new WebNodeConnection(this, node);
        if(connections.contains(connection)) {
            return false;
        }
        return addConnection(connection);
    }

    protected boolean addConnection(WebNodeConnection connection) {
        return !connections.contains(connection) && connections.add(connection);
    }

    protected boolean removeConnection(WebNodeConnection connection) {
        return connections.contains(connection) && connections.remove(connection);
    }

    public boolean isConnectedTo(WebNode target){
        return getConnection(target) != null;
    }

    public WebNodeConnection getConnection(WebNode node) {
        for (WebNodeConnection e : connections) {
            if (e.getTarget().equals(node)) {
                return e;
            }
        }
        return null;
    }

    public int distanceTo(WebNode target) {
        int distX = Math.abs(getX()-target.getX());
        int distY = Math.abs(getY()-target.getY());
        int distP = Math.abs(getPlane()-target.getPlane());
        return distX + distY + distP;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlane() {
        return plane;
    }

    public void setParent(WebNode parent) {
        this.parent = parent;
    }

    public WebNode getParent() {
        return parent;
    }

    public double calculateHeuristic(WebNode goal) {
        return distanceTo(goal);
    }

    public void setGCost(double cost) {
        this.gCost = cost;
    }

    public double getGCost() {
        return gCost;
    }

    public double getTotalCost() {
        return gCost+heuristic;
    }

    public LinkedList<WebNodeConnection> getConnections(){
        return connections;
    }

    @Override
    public int hashCode() {
        return (x ^ y ^ plane);
    }

    @Override
    public String toString() {
        return "("+x+", "+y+", "+plane+")";
    }

    @Override
    public boolean equals(Object obj) {
        WebNode other = (WebNode) obj;
        return this.getX() == other.getX() && this.getY() == other.getY() && this.getPlane() == other.getPlane();
    }

}
