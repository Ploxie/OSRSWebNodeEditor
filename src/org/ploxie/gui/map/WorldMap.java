package org.ploxie.gui.map;

import org.ploxie.pathfinder.utils.Pair;
import org.ploxie.pathfinder.utils.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WorldMap {

    public static final int MAP_WIDTH = 12544;
    public static final int MAP_HEIGHT = 37120;
    public static final int TILE_SIZE = 256;
    public static final int OFFSET_X = 1152;
    public static final int OFFSET_Y = -26625;

    private Chunk[][] tiles;
    private int zoom;
    private int plane;

    public void loadTiles(int zoom, int plane) {
        this.zoom = zoom;
        this.plane = plane;

        int w = getWidth();
        int h = getHeight();

        tiles = new Chunk[w][h];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                getTiles()[x][y] = new Chunk(this, plane, zoom, x, y);
            }
        }
    }

    public List<Chunk> getChunksInViewport(Rectangle viewport) {

        int minX = Math.max(0, viewport.x / TILE_SIZE);
        int maxX = Math.min((int) ((viewport.x + viewport.getWidth()) / TILE_SIZE) + 1, tiles.length);

        int minY = Math.max(0, viewport.y / TILE_SIZE);
        int maxY = Math.min((int) ((viewport.y + viewport.getHeight()) / TILE_SIZE) + 1, tiles[0].length);

        List<Chunk> list = new ArrayList<>();

        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                list.add(tiles[x][y]);
            }
        }

        return list;
    }

    private double getUnitsPerPixel() {
        return (TILE_SIZE * (Math.pow(2, -zoom)));
    }

    public double getChunkSize() {
        double unitsPerPixel = getUnitsPerPixel();
        return (TILE_SIZE * unitsPerPixel) / 4;
    }

    public double getPixelsPerTile() {
        double tilesPerChunk = getChunkSize();
        return TILE_SIZE / tilesPerChunk;
    }

    public Point getWorldCoordinates(int x, int y) {
        double tilesPerChunk = getChunkSize();
        return new Point(x + WorldMap.OFFSET_X, WorldMap.MAP_HEIGHT - (y - (int) tilesPerChunk) + WorldMap.OFFSET_Y);
    }

    public Point getMapCoordinates(int x, int y) {
        double pixelsPerTile = getPixelsPerTile();
        return new Point((int) (x / pixelsPerTile), (int) (y / pixelsPerTile));
    }

    public Rectangle getTileOnPosition(int x, int y){
        double pixelsPerTile = getPixelsPerTile();
        Point mapTileCoords = getMapCoordinates(x, y);
        Rectangle rect = new Rectangle(mapTileCoords.x * (int)pixelsPerTile, mapTileCoords.y * (int)pixelsPerTile, (int)pixelsPerTile, (int)pixelsPerTile);
        return rect;
    }

    public int getHeight() {
        return (int) (MAP_HEIGHT / (TILE_SIZE * getUnitsPerPixel())) + 1;
    }

    public int getWidth() {
        return (int) (MAP_WIDTH / (TILE_SIZE * getUnitsPerPixel())) + 1;
    }

    public int getZoom() {
        return zoom;
    }

    public int getPlane() {
        return plane;
    }

    public Chunk[][] getTiles() {
        return tiles;
    }


    public static class MapPoint extends Pair<Integer, Integer> {

        private WorldMap map;

        public MapPoint(WorldMap map, int x, int y) {
            super(x, y);
            this.map = map;
        }

        public MapTile toMapTile(){
            double pixelsPerTile = map.getPixelsPerTile();
            return new MapTile(map, (int) (first / pixelsPerTile), (int) (second / pixelsPerTile));
        }

        public WorldTile toWorldTile(){
            return toMapTile().toWorldTile();
        }

        public int getX(){
            return getFirst();
        }

        public int getY(){
            return getSecond();
        }
    }

    public static class MapTile extends Pair<Integer, Integer> {

        private WorldMap map;

        public MapTile(WorldMap map, int x, int y) {
            super(x, y);
            this.map = map;
        }

        public WorldTile toWorldTile(){
            double tilesPerChunk = map.getChunkSize();
            return new WorldTile(map, first + WorldMap.OFFSET_X, WorldMap.MAP_HEIGHT - (second - (int) tilesPerChunk) + WorldMap.OFFSET_Y);
        }

        public Rectangle getRectangle(){
            double pixelsPerTile = map.getPixelsPerTile();
            Rectangle rect = new Rectangle(first * (int)pixelsPerTile, second * (int)pixelsPerTile, (int)pixelsPerTile, (int)pixelsPerTile);
            return rect;
        }

        public int getX(){
            return getFirst();
        }

        public int getY(){
            return getSecond();
        }
    }

    public static class WorldTile extends Position {

        private WorldMap map;

        public WorldTile(WorldMap map, int x, int y) {
            super(x, y, map.plane);
            this.map = map;
        }

        public MapTile toMapTile(){
            double tilesPerChunk = map.getChunkSize();
            return new MapTile(map, first - WorldMap.OFFSET_X, WorldMap.MAP_HEIGHT - (second + (int) tilesPerChunk) - WorldMap.OFFSET_Y);
        }

        public int getX(){
            return getFirst();
        }

        public int getY(){
            return getSecond();
        }
    }

}
