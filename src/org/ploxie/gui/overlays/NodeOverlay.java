package org.ploxie.gui.overlays;

import org.ploxie.gui.WorldMapViewer;

import java.awt.*;
import java.awt.event.MouseEvent;

public class NodeOverlay extends MapOverlay {

    private Rectangle tile;

    public NodeOverlay(WorldMapViewer mapViewer) {
        super(mapViewer);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        if(tile != null){
            g.fillRect(tile.x + (int) mapViewer.getxOffset(), tile.y + (int) mapViewer.getyOffset(), (int)tile.getWidth(), (int)tile.getHeight());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        tile = getHoveredTile();
    }

}
