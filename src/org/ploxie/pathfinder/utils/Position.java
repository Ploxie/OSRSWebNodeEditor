package org.ploxie.pathfinder.utils;

import org.ploxie.pathfinder.collision.Region;
import org.ploxie.pathfinder.collision.Region2;

public class Position extends Triplet<Integer, Integer, Integer> implements Positionable{

    protected int cached_hash = -1;

    public Position(int x, int y, int z) {
        super(x,y,z);
    }

    public void translate(Direction direction){
       translate(direction.getXOffset(), direction.getYOffset(), direction.getZOffset());
    }

    public void translate(int x, int y){
        translate(x, y, 0);
    }

    public void translate(int x, int y, int z){
        this.first += x;
        this.second += y;
        this.third += z;
        cached_hash = -1;
    }

    public Position getTranslated(Direction direction){
        return getTranslated(direction.getXOffset(), direction.getYOffset(), direction.getZOffset());
    }

    public Position getTranslated(int x, int y){
        return getTranslated(x, y, 0);
    }

    public Position getTranslated(int x, int y, int z){
        Position result = clone();
        result.translate(x,y,z);
        return result;
    }

    public Position getRegionBase(){
        return Region2.getRegionBase(getRegionID(), getZ());
    }

    public int getRegionID(){
        return Region2.getRegionID(this);
    }

    protected Position clone() {
        return new Position(first, second, third);
    }

    @Override
    public Position getPosition() {
        return this;
    }

    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(!(obj instanceof Position)){
            return false;
        }
        return hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        if(cached_hash == -1) {
            cached_hash = ((getZ() & 0b11) << 26) | ((getY() & 0b1111_1111_1111_11) << 12) | ((getX()-1152 & 0b1111_1111_1111));
        }
        return cached_hash;
    }


}
