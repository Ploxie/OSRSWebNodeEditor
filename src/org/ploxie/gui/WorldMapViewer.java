package org.ploxie.gui;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class WorldMapViewer extends ZoomablePane {

    private MapTileGrid tileGrid;

    private int zoom = 3;

    private boolean drawTiles = false;
    private boolean drawCoords = false;

    public WorldMapViewer(double xOffset, double yOffset) {
        super(xOffset, yOffset);

        this.tileGrid = new MapTileGrid();

        this.tileGrid.loadTiles(zoom, 0);

        addKeyListener(this);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        g2.setColor(Color.BLACK);
        g2.fillRect(0,0, getWidth(), getHeight());

        int xOffset = (int)getTransform().getTranslateX();
        int yOffset = (int)getTransform().getTranslateY();
        Rectangle viewport = new Rectangle(-xOffset, -yOffset, getWidth(),getHeight());

        List<MapTile> tiles = new ArrayList<>();

        for(int y = 0; y < tileGrid.getHeight(zoom);y++) {
            for (int x = 0; x < tileGrid.getWidth(zoom); x++) {
                MapTile tile = tileGrid.tiles[x][y];
                Rectangle bounds = (Rectangle)tile.getRectangle().clone();
                bounds.translate(xOffset, yOffset);
                if(tile.getRectangle() == null){
                    continue;
                }
                if(!tile.isInside(viewport)){
                    continue;
                }
                tile.load();
                tiles.add(tile);
            }
        }

        g2.setColor(Color.white);
        for(MapTile tile : tiles){
            if(tile.getImage() == null){
                continue;
            }
            Rectangle bounds = tile.getRectangle();
            int x = (tile.getX() * MapTileGrid.TILE_SIZE) + xOffset;
            int y = (tile.getY() * MapTileGrid.TILE_SIZE) + yOffset;
            g2.drawImage(tile.getImage(),x , y, MapTileGrid.TILE_SIZE , MapTileGrid.TILE_SIZE , null);

            if(drawTiles){
                g2.drawRect(bounds.x+xOffset, bounds.y+yOffset, (int)bounds.getWidth(), (int)bounds.getHeight());
            }
            if(drawCoords){
                g2.drawString(tile.getX()+", "+tile.getY(), x + (MapTileGrid.TILE_SIZE / 2), y + (MapTileGrid.TILE_SIZE / 2));
            }
        }

        g2.drawString(""+xOffset+", "+yOffset, 10, 15);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom += e.getWheelRotation();

        if(zoom < 3){
            zoom = 3;
        }else if(zoom > 11){
            zoom = 11;
        }

        this.tileGrid.loadTiles(zoom, 0);

        repaint();
    }
}
