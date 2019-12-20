package org.ploxie.gui;

import org.ploxie.gui.controls.Controls;
import org.ploxie.gui.map.Chunk;
import org.ploxie.gui.map.WorldMap;
import org.ploxie.gui.overlays.DebugOverlay;
import org.ploxie.gui.overlays.Overlay;
import org.ploxie.gui.overlays.WebOverlay;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class WorldMapViewer extends ZoomablePane {

    private WorldMap worldMap;
    private Overlay debugOverlay;
    private WebOverlay nodeOverlay;

    private static final int MIN_ZOOM = 3;
    private static final int MAX_ZOOM = 11;
    private int zoom = MIN_ZOOM;

    private static final int MIN_PLANE = 0;
    private static final int MAX_PLANE = 3;
    private int plane = 0;

    private boolean drawChunks = false;
    private boolean drawCoords = false;

    public WorldMapViewer(double xOffset, double yOffset) {
        super(xOffset, yOffset);
        this.worldMap = new WorldMap();

        this.debugOverlay = new DebugOverlay(this);
        this.nodeOverlay = new WebOverlay(this, null);

        this.worldMap.loadTiles(zoom, plane);

        setLayout(new GridLayout(0,1));
        Controls controls = new Controls(worldMap, debugOverlay);
        add(controls);

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

        g2.setColor(Color.white);
        for(Chunk chunk : worldMap.getChunksInViewport(viewport)){
            if(chunk == null){
                continue;
            }
            chunk.load(this);
            if(chunk.getImage() == null){
                continue;
            }
            Rectangle bounds = chunk.getRectangle();
            int x = (chunk.getX() * WorldMap.TILE_SIZE) + xOffset;
            int y = (chunk.getY() * WorldMap.TILE_SIZE) + yOffset;
            g2.drawImage(chunk.getImage(),x , y, WorldMap.TILE_SIZE , WorldMap.TILE_SIZE , null);

            if(drawChunks){
                g2.drawRect(bounds.x+xOffset, bounds.y+yOffset, (int)bounds.getWidth(), (int)bounds.getHeight());
            }
            if(drawCoords){
                g2.drawString(chunk.getX()+", "+chunk.getY(), x + (WorldMap.TILE_SIZE / 2), y + (WorldMap.TILE_SIZE / 2));
            }
        }

        nodeOverlay.draw(g2);
        debugOverlay.draw(g2);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double lastWidth = worldMap.getWidth();
        double lastHeight = worldMap.getHeight();

        double xRel = (MouseInfo.getPointerInfo().getLocation().getX()) - (getLocationOnScreen().getX());
        double yRel = (MouseInfo.getPointerInfo().getLocation().getY()) - (getLocationOnScreen().getY());

        zoom -= e.getWheelRotation();
        zoom = Math.max(Math.min(zoom, MAX_ZOOM), MIN_ZOOM);
        this.worldMap.loadTiles(zoom, plane);

        double zoomDivX = (worldMap.getWidth() / lastWidth);
        double zoomDivY = (worldMap.getHeight() / lastHeight);

        xOffset = (zoomDivX) * (xOffset) + (1 - zoomDivX) * (xRel);
        yOffset = (zoomDivY) * (yOffset) + (1 - zoomDivY) * (yRel);

        transform.setToTranslation(xOffset, yOffset);
        repaint();
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public int getZoom() {
        return zoom;
    }

    public int getPlane() {
        return plane;
    }

    public void setPlane(int plane) {
        if(plane < MIN_PLANE){
            return;
        }
        if(plane > MAX_PLANE){
            return;
        }
        this.plane = plane;
        this.worldMap.loadTiles(zoom, plane);
        repaint();
    }
}
