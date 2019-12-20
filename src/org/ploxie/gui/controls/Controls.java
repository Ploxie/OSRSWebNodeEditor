package org.ploxie.gui.controls;

import org.ploxie.gui.map.WorldMap;
import org.ploxie.gui.overlays.Overlay;

import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {

    private JButton tileX;
    private JButton tileY;
    private JButton tileZ;

    private JButton increasePlane;
    private JButton decreasePlane;

    public Controls(WorldMap grid, Overlay overlay){

        setLayout(null);
        setOpaque(false);
        setBackground(new Color(0,0,0,0));


        this.tileX = new JButton(){
            @Override
            public String getText() {
                return ""+overlay.getWorldTileOnMouse().getX();
            }
        };
        this.tileX.setLocation(10, 10);
        this.tileX.setSize(60, 30);
        this.tileX.setFocusable(false);

        this.tileY = new JButton(){
            @Override
            public String getText() {
                return ""+overlay.getWorldTileOnMouse().getY();
            }
        };
        this.tileY.setLocation(69, 10);

        this.tileY.setSize(60, 30);
        this.tileY.setFocusable(false);

        this.tileZ = new JButton(){
            @Override
            public String getText() {
                return ""+grid.getPlane();
            }
        };
        this.tileZ.setLocation(128, 10);
        this.tileZ.setSize(60, 30);
        this.tileZ.setFocusable(false);

        add(tileX);
        add(tileY);
        add(tileZ);

        this.increasePlane = new JButton("+");
        this.increasePlane.addActionListener(e -> {
            //grid.setPlane(grid.getPlane()+1);
        });
        this.increasePlane.setLocation(128, 39);
        this.increasePlane.setSize(30, 20);

        this.increasePlane.setFocusable(false);
        this.increasePlane.validate();

        this.decreasePlane = new JButton("-");
        this.decreasePlane.addActionListener(e -> {
            //viewer.setPlane(viewer.getPlane()-1);
        });
        this.decreasePlane.setLocation(157, 39);
        this.decreasePlane.setSize(30, 20);
        this.decreasePlane.setFocusable(false);

        add(increasePlane);
        add(decreasePlane);
    }

}
