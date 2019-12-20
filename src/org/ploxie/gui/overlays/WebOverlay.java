package org.ploxie.gui.overlays;

import org.ploxie.gui.WorldMapViewer;
import org.ploxie.pathfinder.web.Web;
import org.ploxie.pathfinder.web.WebNode;

import java.awt.*;

public class WebOverlay extends Overlay {

    private static final Color NODE_FILL_COLOR = new Color(0, 0, 255, 128);

    private Web web;

    public WebOverlay(WorldMapViewer mapViewer, Web web) {
        super(mapViewer);

        this.web = web;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);

        /*for(WebNode node : web.getNodes()){
            drawNode(g, node);
        }*/
    }

    private void drawNode(Graphics2D g, WebNode node){

        g.setColor(NODE_FILL_COLOR);

        /*Rectangle tile = mapViewer.getWorldMap().getTileOnPosition(mousePosition.x, mousePosition.y);
        g.fillRect(tile.x, tile.y, (int)tile.getWidth(), (int)tile.getHeight());*/

    }

}
