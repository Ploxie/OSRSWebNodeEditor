package org.ploxie.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MapTileGrid {

    public static int MAP_WIDTH = 12544;
    public static int MAP_HEIGHT = 37120;
    public static int TILE_SIZE = 256;

    public MapTile[][] tiles;

    public void loadTiles(int zoom, int plane){
        int w = getWidth(zoom);
        int h = getHeight(zoom);

        tiles = new MapTile[w][h];

        for(int y = 0; y < h;y++){
            for(int x = 0; x < w; x++){
                tiles[x][y] = new MapTile(this, plane, zoom, x,  y);
            }
        }
    }

    public int getHeight(int zoom){
        double unitsPerPixel = (TILE_SIZE * (Math.pow(2, -zoom)));
        return (int)(MAP_HEIGHT / (TILE_SIZE * unitsPerPixel)) + 1;
    }

    public int getWidth(int zoom){
        double unitsPerPixel = (TILE_SIZE * (Math.pow(2, -zoom)));
        return (int)(MAP_WIDTH / (TILE_SIZE * unitsPerPixel)) + 1;
    }

}
