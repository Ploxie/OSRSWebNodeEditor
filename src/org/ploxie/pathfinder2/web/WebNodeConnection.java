package org.ploxie.pathfinder2.web;

public  class WebNodeConnection implements Comparable<WebNodeConnection> {

    private final WebNode source;
    private final WebNode target;

    private double heuristic;

    public WebNodeConnection(WebNode source, WebNode target) {
        this.source = source;
        this.target = target;

        this.heuristic = calculateHeuristic();
    }

    public WebNode getSource() {
        return source;
    }

    public WebNode getTarget() {
        return target;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public double calculateHeuristic() {
        return target.distanceTo(source);
    }

    @Override
    public final int compareTo(final WebNodeConnection o) {
        double dist = getHeuristic() - o.getHeuristic();
        if (dist == 0) {
            return 0;
        }
        if (dist < 0) {
            return -1;
        }
        return 1;
    }

    @Override
    public int hashCode() {
        WebNode source = getSource();
        int value = 0;
        if (source != null) {
            value += source.hashCode();
        }
        source = getTarget();
        if (source != null) {
            value += source.hashCode();
        }
        value += getHeuristic();
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof WebNodeConnection)) {
            return false;
        }
        WebNodeConnection wnc = (WebNodeConnection) o;
        if (getSource() == null && wnc.getSource() != null) {
            return false;
        } else if (!getSource().equals(wnc.getSource())) {
            return false;
        }
        if (getTarget() == null && wnc.getTarget() != null) {
            return false;
        } else if (!getTarget().equals(wnc.getTarget())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getSource() + " -> " + this.getTarget();
    }

}
