package org.ploxie.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author Thanasis1101
 * @version 1.0
 */
public class ZoomablePane extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {

    protected BufferedImage image;

    protected double zoomFactor = 1;
    private double prevZoomFactor = 1;
    protected double xOffset = 0;
    protected double yOffset = 0;
    private int xDiff;
    private int yDiff;
    private Point startPoint;

    public AffineTransform transform = new AffineTransform();

    private Point cursorPos = new Point(0, 0);

    public ZoomablePane(String urlPath) {
        try {
            URL url = new URL(urlPath);
            this.image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        initComponent();

        xOffset = -13065.2583;
        yOffset = -5343.9743;
        zoomFactor = 2.1435;
        prevZoomFactor = 2.1435;

        transform.setToTranslation(xOffset, yOffset);
        transform.scale(zoomFactor, zoomFactor);
    }

    public ZoomablePane(BufferedImage image) {
        this.image = image;
        initComponent();

    }

    private void initComponent() {
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public Point getCursorPos() {
        return cursorPos;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
        }
        //Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= 1.1;
        }

        double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
        double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();
        double zoomDiv = zoomFactor / prevZoomFactor;

        xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
        yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

        transform.setToTranslation(xOffset, yOffset);
        transform.scale(zoomFactor, zoomFactor);
        prevZoomFactor = zoomFactor;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if(e.getModifiers() == 4) {
            Point curPoint = e.getLocationOnScreen();
            xDiff = curPoint.x - startPoint.x;
            yDiff = curPoint.y - startPoint.y;

            xOffset += xDiff;
            yOffset += yDiff;

            transform.setToTranslation(xOffset, yOffset);
            transform.scale(zoomFactor, zoomFactor);

            startPoint = curPoint;
        }

        this.cursorPos = new Point((int) ((-xOffset + e.getX()) / zoomFactor), (int) ((-yOffset + e.getY()) / zoomFactor));
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.cursorPos = new Point((int) ((-xOffset + e.getX()) / zoomFactor), (int) ((-yOffset + e.getY()) / zoomFactor));
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON3){
            startPoint = MouseInfo.getPointerInfo().getLocation();
        }
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