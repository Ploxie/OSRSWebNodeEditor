package org.ploxie.pathfinder.utils;

public class Direction extends Triplet<Integer, Integer, Integer>{

    public static final Direction NORTH_WEST = new Direction(-1, 1, 0);
    public static final Direction NORTH = new Direction(0, 1, 0);
    public static final Direction NORTH_EAST = new Direction(1, 1, 0);
    public static final Direction EAST = new Direction(1, 0, 0);
    public static final Direction SOUTH_EAST = new Direction(1, -1, 0);
    public static final Direction SOUTH = new Direction(0, -1, 0);
    public static final Direction SOUTH_WEST = new Direction(-1, -1, 0);

    public static final Direction UP = new Direction(0, 0, 1);
    public static final Direction DOWN = new Direction(0, 0, -1);

    public Direction(int x, int y){
        super(x, y,0);
    }

    public Direction(int x, int y, int z) {
        super(x, y, z);
    }

    public int getXOffset(){
        return first;
    }

    public int getYOffset(){
        return second;
    }

    public int getZOffset(){
        return third;
    }
}
