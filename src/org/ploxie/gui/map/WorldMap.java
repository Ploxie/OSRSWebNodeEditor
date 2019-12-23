package org.ploxie.gui.map;

import org.ploxie.pathfinder.utils.Pair;
import org.ploxie.pathfinder.utils.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WorldMap {

    public static final double MAP_WIDTH = 12544;
    public static final double MAP_HEIGHT = 37120;
    public static final double TILE_SIZE = 256;
    public static final double OFFSET_X = 1152;
    public static final double OFFSET_Y = -26625;

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

        int minX = (int)Math.max(0, viewport.x / TILE_SIZE);
        int maxX = Math.min((int) ((viewport.x + viewport.getWidth()) / TILE_SIZE) + 1, tiles.length);

        int minY = (int)Math.max(0, viewport.y / TILE_SIZE);
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
        return getUnitsPerPixel(zoom);
    }

    private double getUnitsPerPixel(int zoom) {
        return (TILE_SIZE * (Math.pow(2, -zoom)));
    }

    public double getChunkSize() {
        return getChunkSize(zoom);
    }

    public double getChunkSize(int zoom) {
        double unitsPerPixel = getUnitsPerPixel(zoom);
        return (TILE_SIZE * unitsPerPixel) / 4.0;
    }

    public double getPixelsPerTile() {
        return getPixelsPerTile(zoom);
    }

    public double getPixelsPerTile(int zoom) {
        double tilesPerChunk = getChunkSize(zoom);
        return TILE_SIZE / tilesPerChunk;
    }

    public int getHeight() {
        return getHeight(zoom);
    }

    public int getHeight(int zoom) {
        return (int) (MAP_HEIGHT / (TILE_SIZE * getUnitsPerPixel(zoom))) + 1;
    }

    public int getWidth() {
        return getWidth(zoom);
    }

    public int getWidth(int zoom) {
        return (int) (MAP_WIDTH / (TILE_SIZE * getUnitsPerPixel(zoom))) + 1;
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
        private int zoom = -1;

        public MapPoint(WorldMap map, int x, int y) {
            super(x, y);
            this.map = map;
        }

        public MapPoint(WorldMap map,int zoom, int x, int y) {
            super(x, y);
            this.map = map;
            this.zoom = zoom;
        }

        public MapTile toMapTile(){
            double pixelsPerTile = map.getPixelsPerTile(getZoom());
            return new MapTile(map,getZoom(), (int) (first / pixelsPerTile), (int) (second / pixelsPerTile));
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

        public int getZoom(){
            if(zoom == -1){
                return map.zoom;
            }

            return zoom;
        }
    }

    public static class MapTile extends Pair<Integer, Integer> {

        private WorldMap map;
        private int zoom = -1;

        public MapTile(WorldMap map, int x, int y) {
            super(x, y);
            this.map = map;
        }

        public MapTile(WorldMap map,int zoom, int x, int y) {
            super(x, y);
            this.map = map;
            this.zoom = zoom;
        }

        public WorldTile toWorldTile(){
            double tilesPerChunk = map.getChunkSize(getZoom());
            return new WorldTile(map,getZoom(), (int)(first + OFFSET_X), (int)(MAP_HEIGHT - (second - (int) tilesPerChunk) + OFFSET_Y));
        }

        public MapPoint toMapPoint(){
            double pixelsPerTile = map.getPixelsPerTile(getZoom());
            return new MapPoint(map,getZoom(), (int) (first * pixelsPerTile), (int) (second * pixelsPerTile));
        }

        public Rectangle getRectangle(){
            double pixelsPerTile = map.getPixelsPerTile(getZoom());
            Rectangle rect = new Rectangle((int)(first * pixelsPerTile), (int)(second * pixelsPerTile), (int)pixelsPerTile, (int)pixelsPerTile);
            return rect;
        }

        public int getX(){
            return getFirst();
        }

        public int getY(){
            return getSecond();
        }

        public int getZoom(){
            if(zoom == -1){
                return map.zoom;
            }

            return zoom;
        }
    }

    public static class WorldTile extends Position {

        private WorldMap map;
        private int zoom = -1;

        public WorldTile(WorldMap map, int x, int y) {
            super(x, y, map.plane);
            this.map = map;
        }

        public WorldTile(WorldMap map,int zoom, int x, int y) {
            super(x, y, map.plane);
            this.map = map;
            this.zoom = zoom;
        }

        public MapTile toMapTile(){
            return toMapTile(getZoom());
        }

        public MapTile toMapTile(int zoom){
            double tilesPerChunk = map.getChunkSize(zoom);
            return new MapTile(map,getZoom(), (int)(first - OFFSET_X), (int)(MAP_HEIGHT - (second - OFFSET_Y - (int)tilesPerChunk)));
        }

        public int getX(){
            return getFirst();
        }

        public int getY(){
            return getSecond();
        }

        public int getZoom(){
            if(zoom == -1){
                return map.zoom;
            }

            return zoom;
        }
    }

}
