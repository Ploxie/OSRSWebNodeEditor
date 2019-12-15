package org.ploxie.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MapOverlay implements MouseMotionListener {

    private WorldMapViewer mapViewer;

    private Point mousePosition;

    private boolean drawHoveredTile = true;
    private boolean drawHoveredChunk = true;
    private boolean drawTileCoords = true;

    public MapOverlay(WorldMapViewer mapViewer){
        this.mapViewer = mapViewer;
        this.mousePosition = new Point(0,0);
    }

    public void draw(Graphics2D g){
        if(drawHoveredTile) {
            g.setColor(Color.GREEN);
            Rectangle hoveredTile = getHoveredTile();
            if (hoveredTile != null) {
                g.drawRect(hoveredTile.x + (int) mapViewer.xOffset, hoveredTile.y + (int) mapViewer.yOffset, (int) hoveredTile.getWidth(), (int) hoveredTile.getHeight());
            }
        }

        if(drawHoveredChunk) {
            g.setColor(Color.GREEN);
            MapChunk hoveredChunk = getHoveredChunk();
            if (hoveredChunk != null) {
                Rectangle bounds = hoveredChunk.getRectangle();
                g.drawRect(bounds.x + (int) mapViewer.xOffset, bounds.y + (int) mapViewer.yOffset, (int) bounds.getWidth(), (int) bounds.getHeight());
            }
        }

        if(drawTileCoords) {
            g.setColor(Color.GREEN);
            Point mapTileCoord = getMapTileCoordinates(mousePosition.x, mousePosition.y);
            Point tileCoord = getWorldCoordinates(mapTileCoord.x, mapTileCoord.y);
            g.drawString("Tile: ("+tileCoord.x+", "+tileCoord.y+")", 10, 40);
        }
    }

    private Point getWorldCoordinates(int x, int y){
        double tilesPerChunk = getTilesPerChunk();
        return new Point(x+MapChunkGrid.OFFSET_X, MapChunkGrid.MAP_HEIGHT-(y-(int)tilesPerChunk) + MapChunkGrid.OFFSET_Y);
    }

    private double getTilesPerChunk(){
        double unitsPerPixel = mapViewer.getChunkGrid().getUnitsPerPixel(mapViewer.getZoom());
        return (mapViewer.getChunkGrid().TILE_SIZE * unitsPerPixel) / 4;
    }

    private double getPixelsPerTile(){
        double tilesPerChunk = getTilesPerChunk();
        return mapViewer.getChunkGrid().TILE_SIZE / tilesPerChunk;
    }

    private Point getMapTileCoordinates(int x, int y){
        double pixelsPerTile = getPixelsPerTile();
        return new Point((int)(x/pixelsPerTile), (int)(y/pixelsPerTile));
    }

    private Rectangle getHoveredTile(){
        double pixelsPerTile = getPixelsPerTile();
        Point mapTileCoords = getMapTileCoordinates(mousePosition.x, mousePosition.y);
        Rectangle rect = new Rectangle(mapTileCoords.x * (int)pixelsPerTile, mapTileCoords.y * (int)pixelsPerTile, (int)pixelsPerTile, (int)pixelsPerTile);
        return rect;
    }

    private MapChunk getHoveredChunk(){
        int x = (int)mousePosition.getX() / MapChunkGrid.TILE_SIZE;
        int y = (int)mousePosition.getY() / MapChunkGrid.TILE_SIZE;

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
        int x = (int)(-mapViewer.xOffset+e.getX());
        int y = (int)(-mapViewer.yOffset+e.getY());
        this.mousePosition = new Point(x,y);
    }
}
