package org.ploxie.gui.overlays;

import org.ploxie.gui.map.Chunk;
import org.ploxie.gui.WorldMapViewer;
import org.ploxie.gui.map.WorldMap;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DebugOverlay extends Overlay {

    private boolean drawHoveredTile = true;
    private boolean drawHoveredChunk = false;
    private boolean drawChunks = true;

    public DebugOverlay(WorldMapViewer mapViewer) {
        super(mapViewer);
    }

    @Override
    public void draw(Graphics2D g) {
        if(drawHoveredTile) {
            g.setColor(Color.GREEN);
            Rectangle hoveredTile = mapViewer.getMapTileOnMouse().getRectangle();
            if (hoveredTile != null) {
                g.drawRect(hoveredTile.x + (int) mapViewer.getxOffset(), hoveredTile.y + (int) mapViewer.getyOffset(), (int) hoveredTile.getWidth(), (int) hoveredTile.getHeight());
            }
        }

        if(drawChunks){
            g.setColor(Color.GRAY);
            Rectangle viewport = mapViewer.getViewport();

            int xOffset = (int)-viewport.getX();
            int yOffset = (int)-viewport.getY();

            for(Chunk chunk : mapViewer.getWorldMap().getChunksInViewport(viewport)){
                Rectangle bounds = chunk.getRectangle();
                g.drawRect(bounds.x+xOffset, bounds.y+yOffset, (int)bounds.getWidth(), (int)bounds.getHeight());
            }
        }

        if(drawHoveredChunk) {
            g.setColor(Color.GREEN);
            Chunk hoveredChunk = getHoveredChunk();
            if (hoveredChunk != null) {
                Rectangle bounds = hoveredChunk.getRectangle();
                g.drawRect(bounds.x + (int) mapViewer.getxOffset(), bounds.y + (int) mapViewer.getyOffset(), (int) bounds.getWidth(), (int) bounds.getHeight());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }
}
