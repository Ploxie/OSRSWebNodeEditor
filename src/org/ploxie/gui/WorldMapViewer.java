package org.ploxie.gui;

import org.ploxie.gui.controls.Controls;
import org.ploxie.gui.controls.PositionControl;
import org.ploxie.gui.map.Chunk;
import org.ploxie.gui.map.WorldMap;
import org.ploxie.gui.overlays.DebugOverlay;
import org.ploxie.gui.overlays.Overlay;
import org.ploxie.gui.overlays.WebOverlay;

import java.awt.*;
import java.awt.event.MouseEvent;
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

    protected WorldMap.MapPoint mapPointOnMouse;
    protected WorldMap.MapTile mapTileOnMouse;
    protected WorldMap.WorldTile worldTileOnMouse;

    public WorldMapViewer(double xOffset, double yOffset) {
        super(xOffset, yOffset);
        this.worldMap = new WorldMap();
        this.mapPointOnMouse = new WorldMap.MapPoint(getWorldMap(), 0,0);
        this.mapTileOnMouse = this.mapPointOnMouse.toMapTile();
        this.worldTileOnMouse = this.mapTileOnMouse.toWorldTile();

        this.debugOverlay = new DebugOverlay(this);
        this.nodeOverlay = new WebOverlay(this, null);

        this.worldMap.loadTiles(zoom, plane);

        setLayout(null);
        //Controls controls = new Controls(this);
        PositionControl controls = new PositionControl(this);
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
        Rectangle viewport = getViewport();

        g2.setColor(Color.white);
        for(Chunk chunk : worldMap.getChunksInViewport(viewport)){
            if(chunk == null){
                continue;
            }

            chunk.load(this);

            if(chunk.getImage() == null){
                continue;
            }
            int x = (int)(chunk.getX() * WorldMap.TILE_SIZE) + xOffset;
            int y = (int)(chunk.getY() * WorldMap.TILE_SIZE) + yOffset;
            g2.drawImage(chunk.getImage(),x , y, (int)WorldMap.TILE_SIZE , (int)WorldMap.TILE_SIZE , null);
        }

        nodeOverlay.draw(g2);
        debugOverlay.draw(g2);
    }

    public Rectangle getViewport() {
        return new Rectangle((int)-getTransform().getTranslateX(), (int)-getTransform().getTranslateY(), getWidth(),getHeight());
    }

    public WorldMap.MapPoint getMapPointOnMouse() {
        return mapPointOnMouse;
    }

    public WorldMap.MapTile getMapTileOnMouse() {
        return mapTileOnMouse;
    }

    public WorldMap.WorldTile getWorldTileOnMouse() {
        return worldTileOnMouse;
    }

    public void setCenterTile(WorldMap.WorldTile worldTile){
        setCenterTile(worldTile, zoom);
    }

    public void setCenterTile(WorldMap.WorldTile worldTile, int zoom){

        double width = (worldMap.getWidth() * worldMap.getChunkSize() * worldMap.getPixelsPerTile());
        double height = (worldMap.getHeight(zoom) * worldMap.getChunkSize(zoom) * worldMap.getPixelsPerTile(zoom));

        WorldMap.MapPoint mapPoint = worldTile.toMapTile(zoom).toMapPoint();

        double scaledX = mapPoint.getX() / width;
        double scaledY = mapPoint.getY() / height;

        xOffset = ((getWidth() / 2.0) - width) + (width * (1.0 - scaledX));
        yOffset = ((getHeight() / 2.0) - height) + (height * (1.0 - scaledY));

        transform.setToTranslation(xOffset, yOffset);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseMoved(e);

        int oldZoom = zoom;

        zoom -= e.getWheelRotation();
        if(zoom > MAX_ZOOM){
            zoom = MAX_ZOOM;
            return;
        }else if (zoom < MIN_ZOOM){
            zoom = MIN_ZOOM;
            return;
        }

        this.worldMap.loadTiles(zoom, plane);

        int z = zoom == 4 || this.zoom == 8 ? oldZoom : zoom;

        WorldMap.WorldTile worldTile = new WorldMap.WorldTile(worldMap, worldTileOnMouse.getX(), worldTileOnMouse.getY());
        setCenterTile(worldTile, z);

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        int x = (int)(-xOffset+e.getX());
        int y = (int)(-yOffset+e.getY());

        this.mapPointOnMouse = new WorldMap.MapPoint(getWorldMap(), x,y);
        this.mapTileOnMouse = this.mapPointOnMouse.toMapTile();
        this.worldTileOnMouse = this.mapTileOnMouse.toWorldTile();
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
