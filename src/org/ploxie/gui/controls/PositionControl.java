package org.ploxie.gui.controls;

import org.ploxie.gui.WorldMapViewer;
import org.ploxie.gui.map.WorldMap;

import javax.swing.*;
import java.awt.*;

public class PositionControl extends JPanel {

    private WorldMapViewer mapViewer;

    private int buttonWidth = 60;
    private int buttonHeight = 24;
    private Font font = new Font("Courier", Font.PLAIN, buttonHeight / 2);

    public PositionControl(WorldMapViewer mapViewer){
        this.mapViewer = mapViewer;

        setOpaque(false);
        setSize(buttonWidth * 3, buttonHeight);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(50,50,50,200));
        g.fillRect(1,1, getWidth()-2, getHeight()-2);

        g.setColor(new Color(75,75,75));
        g.drawRect(0,0, buttonWidth, getHeight()-1);
        g.drawRect(buttonWidth,0, buttonWidth, getHeight()-1);
        g.drawRect(getWidth()- buttonWidth,0, buttonWidth, getHeight()-1);

        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);
        g.setColor(Color.WHITE);

        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

        WorldMap.WorldTile worldTile = mapViewer.getWorldTileOnMouse();
        g.drawString(""+worldTile.getX(), (int)((buttonWidth - g.getFontMetrics(font).getStringBounds(""+worldTile.getX(), g).getWidth()) / 2), y);
        g.drawString(""+worldTile.getY(), buttonWidth + (int)((buttonWidth - g.getFontMetrics(font).getStringBounds(""+worldTile.getY(), g).getWidth()) / 2), y);
        g.drawString(""+worldTile.getZ(), getWidth()- buttonWidth + (int)((buttonWidth - g.getFontMetrics(font).getStringBounds(""+worldTile.getZ(), g).getWidth()) / 2), y);

        g.setColor(new Color(100,100,100));
        g.drawRect(0,0, getWidth()-1, getHeight()-1);
    }
}
