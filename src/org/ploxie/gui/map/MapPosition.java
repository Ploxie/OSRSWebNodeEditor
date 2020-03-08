package org.ploxie.gui.map;

import org.ploxie.pathfinder.utils.Position;


public class MapPosition {

    public enum Type{
        WORLD,
        TILE,
        PIXEL
    }

    private Type type;
    private WorldMap worldMap;

    private Position worldPosition;
    private Position tilePosition;
    private Position pixelPosition;

    public MapPosition(Type type, WorldMap worldMap, Position position){
        this.type = type;
        this.worldMap = worldMap;

        switch (type){
            case WORLD:
                this.worldPosition = position;
                break;
            case TILE:
                this.tilePosition = position;
                break;
            case PIXEL:
                this.pixelPosition = position;
                break;
        }

        updateValues();
    }

    private void updateValues(){
        double tilesPerChunk = worldMap.getChunkSize();
        double pixelsPerTile = worldMap.getPixelsPerTile();

        switch (type){
            case WORLD:
                this.tilePosition = new Position((int)(worldPosition.getX() - WorldMap.OFFSET_X), (int)(WorldMap.MAP_HEIGHT - (worldPosition.getY() - WorldMap.OFFSET_Y - (int)tilesPerChunk)), worldPosition.getZ());
                this.pixelPosition = new Position((int) (tilePosition.getX() * pixelsPerTile), (int) (tilePosition.getY() * pixelsPerTile), tilePosition.getZ());
                break;
            case TILE:
                this.worldPosition = new Position((int)(tilePosition.getX() + WorldMap.OFFSET_X), (int)(WorldMap.MAP_HEIGHT - (tilePosition.getY() - (int) tilesPerChunk) + WorldMap.OFFSET_Y), tilePosition.getZ());
                this.pixelPosition = new Position((int) (tilePosition.getX() * pixelsPerTile), (int) (tilePosition.getY() * pixelsPerTile), tilePosition.getZ());
                break;
            case PIXEL:
                this.tilePosition = new Position((int) (pixelPosition.getX() / pixelsPerTile), (int) (pixelPosition.getY() / pixelsPerTile), tilePosition.getZ());
                this.worldPosition = new Position((int)(tilePosition.getX() + WorldMap.OFFSET_X), (int)(WorldMap.MAP_HEIGHT - (tilePosition.getY() - (int) tilesPerChunk) + WorldMap.OFFSET_Y), tilePosition.getZ());
                break;
        }
    }

    public Position getWorldPosition(){
        updateValues();
        return worldPosition;
    }

    public Position getTilePosition(){
        updateValues();
        return tilePosition;
    }

    public Position getPixelPosition(){
        updateValues();
        return pixelPosition;
    }

}
