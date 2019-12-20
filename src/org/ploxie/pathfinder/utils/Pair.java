package org.ploxie.pathfinder.utils;

public class Pair<X, Y> {

    protected X first;
    protected Y second;

    public Pair(X first, Y second){
        this.first = first;
        this.second = second;
    }

    protected X getFirst() {
        return this.first;
    }

    protected Y getSecond() {
        return this.second;
    }

    @Override
    public String toString() {
        return "("+getFirst()+", "+getSecond()+")";
    }

}
