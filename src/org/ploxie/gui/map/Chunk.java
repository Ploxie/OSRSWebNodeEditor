package org.ploxie.gui.map;

import org.ploxie.gui.WorldMapViewer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Chunk {

    private static final boolean LOAD_FROM_WEB = false;

    private BufferedImage image;
    private Rectangle rectangle;
    private WorldMap grid;
    private int plane;
    private int zoom;
    private int x;
    private int y;

    public Chunk(WorldMap grid, int plane, int zoom, int x, int y) {
        this.grid = grid;
        this.plane = plane;
        this.zoom = zoom;
        this.x = x;
        this.y = y;

        this.rectangle = new Rectangle(x * WorldMap.TILE_SIZE, (y * WorldMap.TILE_SIZE), WorldMap.TILE_SIZE, WorldMap.TILE_SIZE);
    }

    public void load(WorldMapViewer viewer) {
        int height = grid.getHeight();
        new Thread(() -> {
            try {
                if (LOAD_FROM_WEB) {
                    URL url = new URL("https://raw.githubusercontent.com/Explv/osrs_map_full_2019_09_13/master/" + plane + "/" + zoom + "/" + x + "/" + (height - 1 - y) + ".png");
                    this.image = ImageIO.read(url);
                } else {
                    File file = new File("C:\\Users\\Ploxie\\Documents\\MapData\\" + plane + "/" + zoom + "/" + x + "/" + (height - 1 - y) + ".png");
                    this.image = ImageIO.read(file);
                }

                viewer.repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public BufferedImage getImage() {
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
