package org.ploxie.gui.overlays;

import org.ploxie.gui.map.Chunk;
import org.ploxie.gui.map.WorldMap;
import org.ploxie.gui.WorldMapViewer;
import org.ploxie.pathfinder.utils.Position;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class Overlay implements MouseMotionListener, MouseListener {

    protected WorldMapViewer mapViewer;

    public Overlay(WorldMapViewer mapViewer){
        this.mapViewer = mapViewer;

        mapViewer.addMouseMotionListener(this);
        mapViewer.addMouseListener(this);
    }

    public abstract void draw(Graphics2D g);

    protected void drawTile(Graphics2D g, Position position){
        Rectangle viewport = mapViewer.getViewport();

        int xOffset = (int)-viewport.getX();
        int yOffset = (int)-viewport.getY();

        WorldMap.WorldTile tile = new WorldMap.WorldTile(mapViewer.getWorldMap(), position.getX(), position.getY());
        Rectangle rectangle = tile.toMapTile().getRectangle();
        g.drawRect(rectangle.x+xOffset,rectangle.y+yOffset,rectangle.width,rectangle.height);
    }

    protected Chunk getHoveredChunk(){
        int x = (int)(mapViewer.getMapPointOnMouse().getX() / WorldMap.TILE_SIZE);
        int y = (int)(mapViewer.getMapPointOnMouse().getY() / WorldMap.TILE_SIZE);

        if(mapViewer.getWorldMap().getTiles().length <= x){
            return null;
        }
        if(mapViewer.getWorldMap().getTiles()[x].length <= y){
            return null;
        }

        return mapViewer.getWorldMap().getTiles()[x][y];
    }



    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
