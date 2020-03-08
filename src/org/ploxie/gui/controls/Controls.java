package org.ploxie.gui.controls;

import org.ploxie.gui.WorldMapViewer;
import org.ploxie.gui.map.WorldMap;
import org.ploxie.gui.overlays.Overlay;

import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {

    private JButton increasePlane;
    private JButton decreasePlane;

    public Controls(WorldMapViewer mapViewer){

        setLayout(null);
        setOpaque(false);
        setBackground(new Color(0,0,0,0));

        this.increasePlane = new JButton("+");
        this.increasePlane.addActionListener(e -> {
            mapViewer.setPlane(mapViewer.getPlane()+1);
        });
        this.increasePlane.setLocation(128, 39);
        this.increasePlane.setSize(30, 20);

        this.increasePlane.setFocusable(false);
        this.increasePlane.validate();

        this.decreasePlane = new JButton("-");
        this.decreasePlane.addActionListener(e -> {
            mapViewer.setPlane(mapViewer.getPlane()-1);
        });
        this.decreasePlane.setLocation(157, 39);
        this.decreasePlane.setSize(30, 20);
        this.decreasePlane.setFocusable(false);

        add(increasePlane);
        add(decreasePlane);
    }

}
