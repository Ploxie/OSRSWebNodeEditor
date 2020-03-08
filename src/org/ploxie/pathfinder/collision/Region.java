package org.ploxie.pathfinder.collision;


import org.ploxie.pathfinder.utils.Position;

import java.util.Arrays;

public class Region {

    public static final int REGION_WIDTH = 64;

    private Position position;
    private Position worldPosition;

    private int[] collisionData;
    private int filled;

    public Region(Position position){
        this.position = position;
        this.worldPosition = regionToWorldPosition(position);

        this.collisionData = new int[REGION_WIDTH * REGION_WIDTH];
        Arrays.fill(collisionData, -1);
    }

    public Region(Position position, int[] collisionData){
        this.position = position;
        this.worldPosition = regionToWorldPosition(position);

        this.collisionData = collisionData;
        this.filled = collisionData.length;
    }

    private int getIndex(Position worldPosition){
        int xIndex = worldPosition.getX() - this.worldPosition.getX();
        int yIndex = worldPosition.getY() - this.worldPosition.getY();
        return (REGION_WIDTH * yIndex) + xIndex;
    }

    public Position getPosition(){
        return position;
    }

    public CollisionData get(Position position){
        int index = getIndex(position);
        if(index < 0 || index >= collisionData.length){
            return null;
        }
        return new CollisionData(collisionData[index]);
    }

    public void put(Position position, CollisionData collisionData){
        put(position, collisionData.getCollisionValue());
    }

    public void put(Position worldPosition, int collisionValue){
        int index = getIndex(worldPosition);
        if(collisionData[index] == -1){
            filled++;
        }
        collisionData[index] = collisionValue;
    }

    public boolean contains(Position worldPosition){
        int xOffset = worldPosition.getX() - this.worldPosition.getX();
        int yOffset = worldPosition.getY() - this.worldPosition.getY();
        return xOffset >= 0 && xOffset < REGION_WIDTH && yOffset >= 0 && yOffset < REGION_WIDTH && position.getZ() == worldPosition.getZ();
    }

    public Position getWorldPosition(){
        return worldPosition;
    }

    public int[] getCollisionData(){
        return collisionData;
    }

    public boolean isFilled(){
        return filled == collisionData.length;
    }

    @Override
    public boolean equals(Object obj) {
       /* if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Region)) {
            return false;
        }*/
        Region compare = (Region) obj;
        return hashCode() == compare.hashCode();
        //return position.equals(compare.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    public static Position worldToRegionPosition(Position worldPosition) {
        return new Position(worldPosition.getX() / Region.REGION_WIDTH, worldPosition.getY() / Region.REGION_WIDTH, worldPosition.getZ());
    }

    public static Position regionToWorldPosition(Position regionPosition) {
        return new Position((regionPosition.getX() * REGION_WIDTH), regionPosition.getY() * REGION_WIDTH, regionPosition.getZ());
    }
}
