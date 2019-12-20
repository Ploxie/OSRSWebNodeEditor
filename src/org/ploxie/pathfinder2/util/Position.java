package org.ploxie.pathfinder2.util;

public class Position extends Triplet<Integer, Integer, Integer> {

    private int cached_hash = -1;

    public Position(int x, int y, int z) {
        super(x, y, z);
    }

    public int getX() {
        return this.getFirst();
    }

    public int getY() {
        return this.getSecond();
    }

    public int getZ() {
        return this.getThird();
    }

    public Position translate(int x, int y, int z){
        return new Position(getX() + x, getY()+y,getZ() + z);
    }

    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        Position compare = (Position) obj;
        return hashCode() == compare.hashCode();
    }

    @Override
    public int hashCode() {
        if(cached_hash == -1) {
            cached_hash = ((getX() & 0b1111_1111_1111_111) << 17) | ((getY() & 0b1111_1111_1111_111) << 2) | (getZ() & 0b11);
        }
        return cached_hash;
    }

    @Override
    public String toString() {
        return "("+getX()+", "+getY()+", "+getZ()+")";
    }

}