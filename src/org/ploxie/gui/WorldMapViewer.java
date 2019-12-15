package org.ploxie.gui;

import org.ploxie.gui.overlays.DebugOverlay;
import org.ploxie.gui.overlays.MapOverlay;
import org.ploxie.gui.overlays.NodeOverlay;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class WorldMapViewer extends ZoomablePane {

    private ChunkGrid chunkGrid;
    private MapOverlay debugOverlay;
    private NodeOverlay nodeOverlay;

    private static final int MIN_ZOOM = 3;
    private static final int MAX_ZOOM = 11;
    private int zoom = MIN_ZOOM;
    private int plane = 0;

    private boolean drawChunks = true;
    private boolean drawCoords = true;

    private List<Chunk> chunks;

    public WorldMapViewer(double xOffset, double yOffset) {
        super(xOffset, yOffset);

        this.debugOverlay = new DebugOverlay(this);
        this.nodeOverlay = new NodeOverlay(this);

        this.chunkGrid = new ChunkGrid(this);
        this.chunkGrid.loadTiles(zoom, plane);

        this.chunks = new ArrayList<>();
    }

    @Override
    protected void update(){
        this.chunks.clear();

        int xOffset = (int)getTransform().getTranslateX();
        int yOffset = (int)getTransform().getTranslateY();
        Rectangle viewport = new Rectangle(-xOffset, -yOffset, getWidth(),getHeight());

        chunks = chunkGrid.getChunksInViewport(viewport);
        for(Chunk chunk : chunks){
            chunk.load();
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        g2.setColor(Color.BLACK);
        g2.fillRect(0,0, getWidth(), getHeight());

        int xOffset = (int)getTransform().getTranslateX();
        int yOffset = (int)getTransform().getTranslateY();

        g2.setColor(Color.white);
        for(Chunk tile : chunks){
            if(tile == null){
                continue;
            }
            if(tile.getImage() == null){
                continue;
            }
            Rectangle bounds = tile.getRectangle();
            int x = (tile.getX() * ChunkGrid.TILE_SIZE) + xOffset;
            int y = (tile.getY() * ChunkGrid.TILE_SIZE) + yOffset;
            g2.drawImage(tile.getImage(),x , y, ChunkGrid.TILE_SIZE , ChunkGrid.TILE_SIZE , null);

            if(drawChunks){
                g2.drawRect(bounds.x+xOffset, bounds.y+yOffset, (int)bounds.getWidth(), (int)bounds.getHeight());
            }
            if(drawCoords){
                g2.drawString(tile.getX()+", "+tile.getY(), x + (ChunkGrid.TILE_SIZE / 2), y + (ChunkGrid.TILE_SIZE / 2));
            }
        }

        nodeOverlay.draw(g2);
        debugOverlay.draw(g2);

        g2.setColor(Color.white);
        g2.drawString(""+xOffset+", "+yOffset, 10, 15);
        g2.drawString("Tiles: "+chunks.size(), 10, 30);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        double lastWidth = chunkGrid.getWidth(zoom);
        double lastHeight = chunkGrid.getHeight(zoom);

        double xRel = (MouseInfo.getPointerInfo().getLocation().getX()) - (getLocationOnScreen().getX());
        double yRel = (MouseInfo.getPointerInfo().getLocation().getY()) - (getLocationOnScreen().getY());

        zoom -= e.getWheelRotation();
        zoom = Math.max(Math.min(zoom, MAX_ZOOM), MIN_ZOOM);
        this.chunkGrid.loadTiles(zoom, plane);

        double zoomDivX = (chunkGrid.getWidth(zoom) / lastWidth);
        double zoomDivY = (chunkGrid.getHeight(zoom) / lastHeight);

        xOffset = (zoomDivX) * (xOffset) + (1 - zoomDivX) * (xRel);
        yOffset = (zoomDivY) * (yOffset) + (1 - zoomDivY) * (yRel);

        transform.setToTranslation(xOffset, yOffset);
        update();
    }

    public ChunkGrid getChunkGrid() {
        return chunkGrid;
    }

    public int getZoom() {
        return zoom;
    }
}
