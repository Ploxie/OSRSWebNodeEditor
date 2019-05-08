package org.ploxie.gui;

import org.ploxie.pathfinder.web.WebNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NodePopupMenu extends JPopupMenu {

    private WorldMapViewer mapViewer;

    public NodePopupMenu(WorldMapViewer viewer){
        this.mapViewer = viewer;

        final Font font1 = new Font("Verdana", Font.PLAIN, 10);
        final Insets inset = new Insets(-2,-30,-2,-12);


        JMenuItem info = new JMenuItem(){
            @Override
            public String getText() {
                WebNode hoverNode = viewer.getHoveredNode();
                return super.getText() + ((hoverNode != null) ? hoverNode : "");
            }
        };
        info.setMargin(inset);
        info.setFont(font1);
        info.setEnabled(false);

        JMenuItem setStart = new JMenuItem("Set Start");
        setStart.setMargin(inset);
        setStart.setFont(font1);

        JMenuItem setGoal = new JMenuItem("Set Goal");
        setGoal.setMargin(inset);
        setGoal.setFont(font1);

        JMenuItem removeNode = new JMenuItem("Remove");
        removeNode.setMargin(inset);
        removeNode.setFont(font1);

        setStart.addActionListener(e -> viewer.setStartNode());
        setGoal.addActionListener(e -> viewer.setEndNode());
        removeNode.addActionListener(e -> viewer.removeNode(viewer.getHoveredNode()));


        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!contains(e.getPoint())) {
                    setVisible(false);
                }

                WebNode hoverNode = viewer.getHoveredNode();
                if(hoverNode == null){
                    remove(info);
                    remove(setStart);
                    remove(setGoal);
                    remove(removeNode);
                }else{
                    add(info);
                    add(setStart);
                    add(setGoal);
                    add(removeNode);
                }
            }
        };

        viewer.addMouseMotionListener(mouseAdapter);


    }


}
