package org.ploxie.gui.overlays;

import org.ploxie.gui.Chunk;
import org.ploxie.gui.ChunkGrid;
import org.ploxie.gui.WorldMapViewer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class MapOverlay implements MouseMotionListener, MouseListener {

    protected WorldMapViewer mapViewer;

    protected Point mousePosition;

    public MapOverlay(WorldMapViewer mapViewer){
        this.mapViewer = mapViewer;
        this.mousePosition = new Point(0,0);

        mapViewer.addMouseMotionListener(this);
        mapViewer.addMouseListener(this);
    }

    public abstract void draw(Graphics2D g);

    protected Point getWorldCoordinates(int x, int y){
        double tilesPerChunk = getTilesPerChunk();
        return new Point(x+ ChunkGrid.OFFSET_X, ChunkGrid.MAP_HEIGHT-(y-(int)tilesPerChunk) + ChunkGrid.OFFSET_Y);
    }

    protected double getTilesPerChunk(){
        double unitsPerPixel = mapViewer.getChunkGrid().getUnitsPerPixel(mapViewer.getZoom());
        return (mapViewer.getChunkGrid().TILE_SIZE * unitsPerPixel) / 4;
    }

    protected double getPixelsPerTile(){
        double tilesPerChunk = getTilesPerChunk();
        return mapViewer.getChunkGrid().TILE_SIZE / tilesPerChunk;
    }

    protected Point getMapTileCoordinates(int x, int y){
        double pixelsPerTile = getPixelsPerTile();
        return new Point((int)(x/pixelsPerTile), (int)(y/pixelsPerTile));
    }

    protected Rectangle getHoveredTile(){
        double pixelsPerTile = getPixelsPerTile();
        Point mapTileCoords = getMapTileCoordinates(mousePosition.x, mousePosition.y);
        Rectangle rect = new Rectangle(mapTileCoords.x * (int)pixelsPerTile, mapTileCoords.y * (int)pixelsPerTile, (int)pixelsPerTile, (int)pixelsPerTile);
        return rect;
    }

    protected Chunk getHoveredChunk(){
        int x = (int)mousePosition.getX() / ChunkGrid.TILE_SIZE;
        int y = (int)mousePosition.getY() / ChunkGrid.TILE_SIZE;

        if(mapViewer.getChunkGrid().getTiles().length <= x){
            return null;
        }
        if(mapViewer.getChunkGrid().getTiles()[x].length <= y){
            return null;
        }

        return mapViewer.getChunkGrid().getTiles()[x][y];
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = (int)(-mapViewer.getxOffset()+e.getX());
        int y = (int)(-mapViewer.getyOffset()+e.getY());
        this.mousePosition = new Point(x,y);
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
