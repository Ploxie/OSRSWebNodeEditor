package org.ploxie.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChunkGrid {

    public static int MAP_WIDTH = 12544;
    public static int MAP_HEIGHT = 37120;
    public static int TILE_SIZE = 256;
    public static int OFFSET_X = 1152;
    public static int OFFSET_Y = -26625;

    private Chunk[][] tiles;
    public WorldMapViewer viewer;

    public ChunkGrid(WorldMapViewer viewer){
        this.viewer = viewer;
    }

    public void loadTiles(int zoom, int plane){
        int w = getWidth(zoom);
        int h = getHeight(zoom);

        tiles = new Chunk[w][h];

        for(int y = 0; y < h;y++){
            for(int x = 0; x < w; x++){
                getTiles()[x][y] = new Chunk(this, plane, zoom, x,  y);
            }
        }
    }

    public List<Chunk> getChunksInViewport(Rectangle viewport){

        int minX = Math.max(0, viewport.x  / TILE_SIZE);
        int maxX = Math.min((int)((viewport.x+viewport.getWidth()) / TILE_SIZE)+1,tiles.length);

        int minY = Math.max(0, viewport.y / TILE_SIZE);
        int maxY = Math.min((int)((viewport.y+viewport.getHeight()) / TILE_SIZE)+1, tiles[0].length);

        List<Chunk> list = new ArrayList<>();

        for(int y = minY; y < maxY;y++){
            for(int x = minX; x < maxX;x++){
                list.add(tiles[x][y]);
            }
        }

        return list;
    }

    public double getUnitsPerPixel(int zoom){
        return (TILE_SIZE * (Math.pow(2, -zoom)));
    }

    public int getHeight(int zoom){
        return (int)(MAP_HEIGHT / (TILE_SIZE * getUnitsPerPixel(zoom))) + 1;
    }

    public int getWidth(int zoom){
        return (int)(MAP_WIDTH / (TILE_SIZE * getUnitsPerPixel(zoom))) + 1;
    }

    public Chunk[][] getTiles() {
        return tiles;
    }
}
