package org.ploxie.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MapTile {

    private BufferedImage image;
    private Rectangle rectangle;
    private MapTileGrid grid;
    private int plane;
    private int zoom;
    private int x;
    private int y;

    public MapTile(MapTileGrid grid, int plane, int zoom, int x, int y){
        this.grid = grid;
        this.plane = plane;
        this.zoom = zoom;
        this.x = x;
        this.y = y;

        this.rectangle = new Rectangle(x * MapTileGrid.TILE_SIZE, (y*MapTileGrid.TILE_SIZE), MapTileGrid.TILE_SIZE, MapTileGrid.TILE_SIZE);
    }

    public void load(){
        int height = grid.getHeight(zoom);

        new Thread(() -> {
            try {
                //URL url = new URL("https://raw.githubusercontent.com/Explv/osrs_map_full_2019_09_13/master/"+plane+"/"+zoom+"/"+x+"/"+(height- 1- y)+".png");

                this.image = ImageIO.read(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public boolean isInside(Rectangle viewport){
        return (!viewport.contains(rectangle.getMinX(), rectangle.getMinY()) && !viewport.contains(rectangle.getMaxX(), rectangle.getMinY()) && !viewport.contains(rectangle.getMaxX(), rectangle.getMaxY()) && !viewport.contains(rectangle.getMinX(), rectangle.getMaxY())) == false;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public BufferedImage getImage(){
        return image;
    }

    public int getPlane() {
        return plane;
    }

    public int getZoom() {
        return zoom;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
