package org.ploxie.pathfinder2.util;

public class Triplet<X, Y, Z> {

    protected X first;
    protected Y second;
    protected Z third;

    public Triplet(X first, Y second, Z third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public X getFirst() {
        return this.first;
    }

    public Y getSecond() {
        return this.second;
    }

    public Z getThird() {
        return this.third;
    }

}
