package org.ploxie.gui;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class WorldMapViewer extends ZoomablePane {

    private MapChunkGrid chunkGrid;
    private MapOverlay overlay;

    private int zoom = 3;
    private int plane = 0;

    private boolean drawChunks = true;
    private boolean drawCoords = true;

    public WorldMapViewer(double xOffset, double yOffset) {
        super(xOffset, yOffset);

        this.overlay = new MapOverlay(this);
        addMouseMotionListener(overlay);

        this.chunkGrid = new MapChunkGrid();
        this.chunkGrid.loadTiles(zoom, plane);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        g2.setColor(Color.BLACK);
        g2.fillRect(0,0, getWidth(), getHeight());

        int xOffset = (int)getTransform().getTranslateX();
        int yOffset = (int)getTransform().getTranslateY();
        Rectangle viewport = new Rectangle(-xOffset, -yOffset, getWidth(),getHeight());
        List<MapChunk> tiles = new ArrayList<>();

        for(int y = 0; y < chunkGrid.getHeight(zoom); y++) {
            for (int x = 0; x < chunkGrid.getWidth(zoom); x++) {
                MapChunk tile = chunkGrid.getTiles()[x][y];
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
        for(MapChunk tile : tiles){
            if(tile.getImage() == null){
                continue;
            }
            Rectangle bounds = tile.getRectangle();
            int x = (tile.getX() * MapChunkGrid.TILE_SIZE) + xOffset;
            int y = (tile.getY() * MapChunkGrid.TILE_SIZE) + yOffset;
            g2.drawImage(tile.getImage(),x , y, MapChunkGrid.TILE_SIZE , MapChunkGrid.TILE_SIZE , null);

            if(drawChunks){
                g2.drawRect(bounds.x+xOffset, bounds.y+yOffset, (int)bounds.getWidth(), (int)bounds.getHeight());
            }
            if(drawCoords){
                g2.drawString(tile.getX()+", "+tile.getY(), x + (MapChunkGrid.TILE_SIZE / 2), y + (MapChunkGrid.TILE_SIZE / 2));
            }
        }

        overlay.draw(g2);

        g2.setColor(Color.white);
        g2.drawString(""+xOffset+", "+yOffset, 10, 15);
        g2.drawString("Tiles: "+tiles.size(), 10, 30);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        double lastWidth = chunkGrid.getWidth(zoom);
        double lastHeight = chunkGrid.getHeight(zoom);

        double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
        double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();
        zoom -= e.getWheelRotation();
        zoom = Math.max(Math.min(zoom, 11), 3);
        this.chunkGrid.loadTiles(zoom, plane);

        double zoomDivX = chunkGrid.getWidth(zoom) / lastWidth;
        double zoomDivY = chunkGrid.getHeight(zoom) / lastHeight;

        xOffset = (zoomDivX) * (xOffset) + (1 - zoomDivX) * xRel;
        yOffset = (zoomDivY) * (yOffset) + (1 - zoomDivY) * yRel;

        transform.setToTranslation(xOffset, yOffset);
        repaint();
    }

    public MapChunkGrid getChunkGrid() {
        return chunkGrid;
    }

    public int getZoom() {
        return zoom;
    }
}
