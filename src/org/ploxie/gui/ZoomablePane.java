package org.ploxie.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class ZoomablePane extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener, KeyListener {

    protected double zoomFactor = 1;
    private double prevZoomFactor = 1;
    protected double xOffset = 0;

    protected double yOffset = 0;
    private int xDiff;
    private int yDiff;
    private Point startPoint;

    public AffineTransform transform = new AffineTransform();

    private Point cursorPos = new Point(0, 0);


    public ZoomablePane(double xOffset, double yOffset) {
        initComponent();

        this.xOffset = xOffset;
        this.yOffset = yOffset;

        transform.setToTranslation(xOffset, yOffset);
        transform.scale(zoomFactor, zoomFactor);
    }

    private void initComponent() {
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);

    }

    protected void update(){

    }

    public Point getCursorPos() {
        return cursorPos;
    }

    public double getxOffset() {
        return xOffset;
    }

    public double getyOffset() {
        return yOffset;
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

        System.out.println(zoomDiv+", "+zoomFactor+", "+prevZoomFactor);

        xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
        yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

        transform.setToTranslation(xOffset, yOffset);
        transform.scale(zoomFactor, zoomFactor);
        prevZoomFactor = zoomFactor;
        update();
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
        update();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(e != null){
            this.cursorPos = new Point((int) ((-xOffset + e.getX()) / zoomFactor), (int) ((-yOffset + e.getY()) / zoomFactor));
        }
        update();
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}