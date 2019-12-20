package org.ploxie.pathfinder.utils;

public class Triplet<X, Y, Z> {

    protected X first;
    protected Y second;
    protected Z third;

    public Triplet(X first, Y second, Z third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    protected X getFirst() {
        return this.first;
    }

    protected Y getSecond() {
        return this.second;
    }

    protected Z getThird() {
        return this.third;
    }

    @Override
    public String toString() {
        return "("+getFirst()+", "+getSecond()+", "+getThird()+")";
    }
}
