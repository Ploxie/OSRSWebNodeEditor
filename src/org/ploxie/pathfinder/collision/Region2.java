package org.ploxie.pathfinder.collision;

import org.ploxie.pathfinder.utils.Position;
import org.rspeer.ui.Log;

public class Region2 {

    private int id;
    private int plane;
    private int[][] collisionValues;

    public Region2(int id, int plane, int[][] collisionValues){
        this.id = id;
        this.collisionValues = collisionValues;
        this.plane = plane;
    }

    public int getID(){
        return id;
    }

    public int getPlane(){
        return plane;
    }

    public CollisionData getCollisionData(Position position){
        if(getID() != position.getRegionID() || position.getZ() != getPlane()){
            return null;
        }


        Position base = getBasePosition();

        int xOffset = position.getX() - base.getX();
        int yOffset = position.getY() - base.getY();

        int collisionValue = collisionValues[yOffset][xOffset];

        return new CollisionData(collisionValue);
    }

    public Position getBasePosition(){
        return getRegionBase(id, plane);
    }

    public static Position getRegionBase(int regionID){
        return getRegionBase(regionID, 0);
    }

    public static Position getRegionBase(int regionID, int plane){
        int yBase = (regionID & 0b111111);
        int xBase = (regionID - yBase) / 256;

        int x = xBase << 6;
        int y = yBase << 6;
        return new Position(x, y, plane);
    }

    public static int getRegionID(Position position){
        return (position.getX() >> 6) * 256 + (position.getY() >> 6);
    }

}
