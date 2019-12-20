package org.ploxie.gui.overlays;

import org.ploxie.gui.map.Chunk;
import org.ploxie.gui.map.WorldMap;
import org.ploxie.gui.WorldMapViewer;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class Overlay implements MouseMotionListener, MouseListener {

    protected WorldMapViewer mapViewer;

    protected WorldMap.MapPoint mapPointOnMouse;
    protected WorldMap.MapTile mapTileOnMouse;
    protected WorldMap.WorldTile worldTileOnMouse;

    public Overlay(WorldMapViewer mapViewer){
        this.mapViewer = mapViewer;

        this.mapPointOnMouse = new WorldMap.MapPoint(mapViewer.getWorldMap(), 0,0);
        this.mapTileOnMouse = this.mapPointOnMouse.toMapTile();
        this.worldTileOnMouse = this.mapTileOnMouse.toWorldTile();

        mapViewer.addMouseMotionListener(this);
        mapViewer.addMouseListener(this);
    }

    public abstract void draw(Graphics2D g);

    protected Chunk getHoveredChunk(){
        int x = mapPointOnMouse.getX() / WorldMap.TILE_SIZE;
        int y = mapPointOnMouse.getY() / WorldMap.TILE_SIZE;

        if(mapViewer.getWorldMap().getTiles().length <= x){
            return null;
        }
        if(mapViewer.getWorldMap().getTiles()[x].length <= y){
            return null;
        }

        return mapViewer.getWorldMap().getTiles()[x][y];
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = (int)(-mapViewer.getxOffset()+e.getX());
        int y = (int)(-mapViewer.getyOffset()+e.getY());
        this.mapPointOnMouse = new WorldMap.MapPoint(mapViewer.getWorldMap(), x,y);
        this.mapTileOnMouse = this.mapPointOnMouse.toMapTile();
        this.worldTileOnMouse = this.mapTileOnMouse.toWorldTile();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
