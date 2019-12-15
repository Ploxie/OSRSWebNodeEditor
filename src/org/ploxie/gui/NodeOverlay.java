package org.ploxie.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class NodeOverlay extends MapOverlay implements MouseListener {

    private Rectangle tile;

    public NodeOverlay(WorldMapViewer mapViewer) {
        super(mapViewer);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        if(tile != null){
            g.fillRect(tile.x + (int) mapViewer.xOffset, tile.y + (int) mapViewer.yOffset, (int)tile.getWidth(), (int)tile.getHeight());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        tile = getHoveredTile();
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
