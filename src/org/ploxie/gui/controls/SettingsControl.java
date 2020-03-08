package org.ploxie.gui.controls;

import org.ploxie.gui.WorldMapViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SettingsControl extends JPanel {

    private WorldMapViewer mapViewer;

    private JButton settingsButton;
    private BufferedImage settingsImage;
    private boolean open;

    private Dimension openDimensions = new Dimension(180,200);
    private Dimension closedDimensions = new Dimension(25,25);

    private Font font = new Font("Courier", Font.PLAIN, 14);

    public SettingsControl(WorldMapViewer mapViewer){
        this.mapViewer = mapViewer;

        setLayout(null);
        setOpaque(false);
        setSize(closedDimensions);

        try {
            settingsImage = ImageIO.read(getClass().getClassLoader().getResource("settings.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        settingsButton = new JButton();
        settingsButton.addActionListener(e -> {
            open = !open;
            if(open){
                setSize(openDimensions);
            }else{
                setSize(closedDimensions);
            }
        });
        settingsButton.setSize(closedDimensions);
        settingsButton.setFocusable(false);
        settingsButton.setIcon(new ImageIcon(settingsImage));
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);


        final Color backgroundColor = new Color(0, 0, 0, 0);

        JPanel content = new JPanel();
        content.setLocation(5, settingsButton.getY()+settingsButton.getHeight());
        content.setSize(openDimensions.width-10, openDimensions.height-(settingsButton.getY()+settingsButton.getHeight() + 5));
        content.setFocusable(false);
        content.setOpaque(false);
        content.setBackground(backgroundColor);

        content.setLayout(new GridLayout(2,1));

        JCheckBox drawChunks = new JCheckBox("Draw Chunks");
        drawChunks.setFocusable(false);
        drawChunks.setOpaque(false);
        drawChunks.setForeground(Color.white);


        JCheckBox drawHoveredChunk = new JCheckBox("Draw Hovered Tile");
        drawHoveredChunk.setFocusable(false);
        drawHoveredChunk.setOpaque(false);
        drawHoveredChunk.setForeground(Color.white);

        content.add(drawChunks);
        content.add(drawHoveredChunk);

        add(content);
        add(settingsButton);
    }



    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(25,25,25,200));
        g.fillRect(1,1, getWidth()-2, getHeight()-2);

        g.setColor(new Color(100,100,100));
        g.drawRect(0,0, getWidth()-1, getHeight()-1);

        g.drawRect(0,0, settingsButton.getWidth(), settingsButton.getHeight());

        g.drawLine(settingsButton.getWidth(), settingsButton.getY() + settingsButton.getHeight(), (int)openDimensions.getWidth(), settingsButton.getY() + settingsButton.getHeight());

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString("Settings", ((openDimensions.width-settingsButton.getWidth()) / 2), 19);
    }

}
