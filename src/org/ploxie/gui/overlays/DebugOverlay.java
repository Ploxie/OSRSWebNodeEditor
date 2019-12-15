package org.ploxie.gui.overlays;

import org.ploxie.gui.Chunk;
import org.ploxie.gui.WorldMapViewer;

import java.awt.*;

public class DebugOverlay extends MapOverlay {

    private boolean drawHoveredTile = true;
    private boolean drawHoveredChunk = true;
    private boolean drawTileCoords = true;

    public DebugOverlay(WorldMapViewer mapViewer) {
        super(mapViewer);
    }

    @Override
    public void draw(Graphics2D g) {
        if(drawHoveredTile) {
            g.setColor(Color.GREEN);
            Rectangle hoveredTile = getHoveredTile();
            if (hoveredTile != null) {
                g.drawRect(hoveredTile.x + (int) mapViewer.getxOffset(), hoveredTile.y + (int) mapViewer.getyOffset(), (int) hoveredTile.getWidth(), (int) hoveredTile.getHeight());
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

        if(drawTileCoords) {
            g.setColor(Color.GREEN);
            Point mapTileCoord = getMapTileCoordinates(mousePosition.x, mousePosition.y);
            Point tileCoord = getWorldCoordinates(mapTileCoord.x, mapTileCoord.y);
            g.drawString("Tile: ("+tileCoord.x+", "+tileCoord.y+")", 10, 40);
        }
    }
}
