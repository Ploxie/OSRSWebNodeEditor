package org.ploxie.pathfinder.collision;

public class CollisionData {

    private int collisionValue;

    public CollisionData(int collisionValue) {
        this.collisionValue = collisionValue;
    }

    public int getCollisionValue() {
        return this.collisionValue;
    }

    public void setCollisionValue(int collisionValue) {
        this.collisionValue = collisionValue;
    }

    public boolean blockedNorthWest() {
        return blockedNorthWest(this.collisionValue);
    }

    public boolean blockedNorth() {
        return blockedNorth(this.collisionValue);
    }

    public boolean blockedNorthEast() {
        return blockedNorthEast(this.collisionValue);
    }

    public boolean blockedEast() {
        return blockedEast(this.collisionValue);
    }

    public boolean blockedSouthEast() {
        return blockedSouthEast(this.collisionValue);
    }

    public boolean blockedSouth() {
        return blockedSouth(this.collisionValue);
    }

    public boolean blockedSouthWest() {
        return blockedSouthWest(this.collisionValue);
    }

    public boolean blockedWest() {
        return blockedWest(this.collisionValue);
    }

    public boolean blockedInDirection(int x, int y){
        if (x > 0 && blockedEast()) {
            return true;
        }
        if (x < 0 && blockedWest()) {
            return true;
        }
        if (y > 0 && blockedNorth()) {
            return true;
        }
        if (y < 0 && blockedSouth()) {
            return true;
        }

        if (x < 0 && y > 0 && blockedNorthWest()) {
            return true;
        }

        if (x > 0 && y > 0 && blockedNorthEast()) {
            return true;
        }

        if (x < 0 && y < 0 && blockedSouthWest()) {
            return true;
        }

        if (x > 0 && y < 0 && blockedSouthEast()) {
            return true;
        }
        return false;
    }

    public boolean isWalkable() {
        return isWalkable(this.collisionValue);
    }

    public boolean isInitialized() {
        return isInitialized(this.collisionValue);
    }

    @Override
    public String toString() {
        return Integer.toHexString(collisionValue);
    }

    public static boolean blockedNorthWest(int collisionValue) {
        return CollisionFlags.check(collisionValue, CollisionFlags.NORTH_WEST) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED_NORTH_WALL);
    }

    public static boolean blockedNorth(int collisionValue) {
        return CollisionFlags.check(collisionValue, CollisionFlags.NORTH) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED_NORTH_WALL);
    }

    public static boolean blockedNorthEast(int collisionValue) {
        return CollisionFlags.check(collisionValue, CollisionFlags.NORTH_EAST) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED_NORTH_WALL);
    }

    public static boolean blockedEast(int collisionValue) {
        return CollisionFlags.check(collisionValue, CollisionFlags.EAST) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED_EAST_WALL);
    }

    public static boolean blockedSouthEast(int collisionValue) {
        return CollisionFlags.check(collisionValue, CollisionFlags.SOUTH_EAST) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED_SOUTH_WALL);
    }

    public static boolean blockedSouth(int collisionValue) {
        return CollisionFlags.check(collisionValue, CollisionFlags.SOUTH) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED_SOUTH_WALL);
    }

    public static boolean blockedSouthWest(int collisionValue) {
        return CollisionFlags.check(collisionValue, CollisionFlags.SOUTH_WEST) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED_SOUTH_WALL);
    }

    public static boolean blockedWest(int collisionValue) {
        return CollisionFlags.check(collisionValue, CollisionFlags.WEST) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED_WEST_WALL);
    }

    public static boolean isWalkable(int collisionValue) {
        return !(CollisionFlags.check(collisionValue, CollisionFlags.OCCUPIED) || CollisionFlags.check(collisionValue, CollisionFlags.SOLID) || CollisionFlags.check(collisionValue, CollisionFlags.BLOCKED) || CollisionFlags.check(collisionValue, CollisionFlags.CLOSED));
    }

    public static boolean isInitialized(int collisionValue) {
        return !(blockedNorth(collisionValue) && blockedEast(collisionValue) && blockedSouth(collisionValue) && blockedWest(collisionValue) && !isWalkable(collisionValue)) || CollisionFlags.check(collisionValue, CollisionFlags.INITIALIZED);
    }

}
