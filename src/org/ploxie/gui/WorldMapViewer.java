package org.ploxie.gui;

import org.ploxie.pathfinder.AStarPathfinder;
import org.ploxie.pathfinder.Pathfinder;
import org.ploxie.pathfinder.web.Web;
import org.ploxie.pathfinder.web.WebNode;
import org.ploxie.pathfinder.web.WebNodeConnection;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class WorldMapViewer extends ZoomablePane {

    private BufferedImage worldMapOverlayImage;

    public Web web = new Web();

    private WebNode hoveredNode;
    private WebNode selectedNode;

    private Pathfinder pathfinder = new AStarPathfinder();
    private Collection<WebNode> path;

    private WebNode startNode;
    private WebNode endNode;


    public WorldMapViewer(String url) {
        super(url);

        this.worldMapOverlayImage = new BufferedImage(2000, 200, BufferedImage.TYPE_INT_ARGB);

        setComponentPopupMenu(new NodePopupMenu(this));

        MouseAdapter mouseAdapter = createMouseAdapter();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        Color nodeBorderColor = new Color(0, 0, 255, 200);
        Color nodeFillColor = new Color(0, 100, 255, 150);
        Color pathColor = new Color(0, 255, 100, 200);

        //Draw map
        g2.drawImage(image, getTransform(), null);

        Point cursorOnMap = getCursorPos();
        Point cursorTile = pointToTile(cursorOnMap.x, cursorOnMap.y);
        Point tilePoint = tileToPoint(cursorTile.x, cursorTile.y);

        //Draw map overlay
        g2.drawImage(worldMapOverlayImage, getTransform(), null);
        g2.setStroke(new BasicStroke((int) (0.5 * zoomFactor)));

        int tileSize = (int) (3 * zoomFactor);
        int lineOffset = (int) (1.5 * zoomFactor);

        //Draw tile on cursor
        Point cursorTilePointOnScreen = pointToScreen(tilePoint.x, tilePoint.y);
        Point cursorNodePoint = pointToTile(cursorTilePointOnScreen.x, cursorTilePointOnScreen.y);
        Point cursorNodeScreenPoint = tileToPoint(cursorNodePoint.x, cursorNodePoint.y);
        g2.drawRect(cursorNodeScreenPoint.x, cursorNodeScreenPoint.y, tileSize, tileSize);

        if(selectedNode != null){
            Point selectedNodePoint = tileToPoint(selectedNode.getX(), selectedNode.getY());
            Point selectedNodeScreenPoint = pointToScreen(selectedNodePoint.x, selectedNodePoint.y);
            g2.drawLine(selectedNodeScreenPoint.x + lineOffset,selectedNodeScreenPoint.y + lineOffset, cursorTilePointOnScreen.x + lineOffset, cursorTilePointOnScreen.y +lineOffset);
        }

        //Draw Nodes and connections
        for (WebNode node : web) {
            boolean selected = getHoveredNode() != null && node.equals(getHoveredNode());
            boolean isPath = path != null && path.contains(node);

            if (isPath && path.contains(node)) {
                continue;
            }

            Point nodePoint = tileToPoint(node.getX(), node.getY());
            Point nodeScreenPoint = pointToScreen(nodePoint.x, nodePoint.y);

            g2.setColor(isPath ? pathColor : selected ? nodeFillColor : nodeBorderColor);
            g2.drawRect(nodeScreenPoint.x, nodeScreenPoint.y, tileSize, tileSize);
            g2.setColor(isPath ? pathColor : selected ? nodeBorderColor : nodeFillColor);
            g2.fillRect(nodeScreenPoint.x, nodeScreenPoint.y, tileSize, tileSize);


            for (WebNodeConnection connection : node.getConnections()) {
                WebNode target = connection.getTarget();

                Point targetNodePoint = tileToPoint(target.getX(), target.getY());
                Point targetNodeScreenPoint = pointToScreen(targetNodePoint.x, targetNodePoint.y);

                g.setColor(Color.blue);

                g2.drawLine(nodeScreenPoint.x + lineOffset, nodeScreenPoint.y + lineOffset, targetNodeScreenPoint.x + lineOffset, targetNodeScreenPoint.y + lineOffset);
            }
        }

        //Draw Path
        if (path != null) {
            WebNode last = null;
            for (WebNode node : path) {

                Point nodePoint = tileToPoint(node.getX(), node.getY());
                Point nodeScreenPoint = pointToScreen(nodePoint.x, nodePoint.y);

                g2.setColor(pathColor);
                g2.drawRect(nodeScreenPoint.x, nodeScreenPoint.y, tileSize, tileSize);
                g2.fillRect(nodeScreenPoint.x, nodeScreenPoint.y, tileSize, tileSize);

                if (last != null) {
                    Point targetNodePoint = tileToPoint(last.getX(), last.getY());
                    Point targetNodeScreenPoint = pointToScreen(targetNodePoint.x, targetNodePoint.y);

                    g2.drawLine(nodeScreenPoint.x + lineOffset, nodeScreenPoint.y + lineOffset, targetNodeScreenPoint.x + lineOffset, targetNodeScreenPoint.y + lineOffset);
                }
                last = node;
            }
        }

        //Draw tile info
        final Font font1 = new Font("Verdana", Font.BOLD, 15);
        String tileCoordText = "Tile: (" + cursorTile.x + ", " + cursorTile.y + ")";
        drawString(g2, font1, tileCoordText, 10, 20, Color.BLACK, Color.RED, 2);

    }

    private void drawString(Graphics2D g, Font font, String text, int x, int y, Color outlineColor, Color fillColor, int outlineWidth) {
        FontRenderContext renderContext = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, font, renderContext);
        Shape textShape = textLayout.getOutline(AffineTransform.getTranslateInstance(x, y));

        g.setStroke(new BasicStroke(outlineWidth));
        g.setColor(outlineColor);
        g.draw(textShape);
        g.setColor(fillColor);
        g.fill(textShape);
    }

    public void removeNode(WebNode node) {
        if (node == null) {
            return;
        }

        web.removeWebNode(node);
        repaint();
    }

    public void setStartNode() {
        WebNode node = getCursorNode();
        if (web.contains(node)) {
            startNode = node;
        }
    }

    public void setEndNode() {
        WebNode node = getCursorNode();
        if (web.contains(node)) {
            endNode = node;
        }

        if (startNode != null && endNode != null) {
            path = pathfinder.findPath(startNode, endNode);
        }
    }

    private WebNode getCursorNode() {
        Point cursor = getCursorPos();
        Point cursorTile = pointToTile(cursor.x, cursor.y);
        return web.getNode(cursorTile.x, cursorTile.y, 0);
    }

    private MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                WebNode node = getCursorNode();
                if (web.contains(node)) {
                    hoveredNode = node;
                } else {
                    hoveredNode = null;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                WebNode node = getCursorNode();

                if(e.getButton() == MouseEvent.BUTTON3){
                    selectedNode = null;
                    return;
                }

                boolean existing = web.contains(node);
                if(!existing){
                    web.add(node);
                }

                if (selectedNode != null && !node.equals(selectedNode)) {

                    if(!selectedNode.isConnectedTo(node)){
                        web.addConnection(selectedNode, node);
                    }

                    if(e.isShiftDown()){
                        selectedNode = node;
                    }else{
                        selectedNode = null;
                    }
                }else{
                    if(existing || e.isShiftDown()){
                        selectedNode = node;
                    }else{
                        selectedNode = null;
                    }
                }
            }
        };
    }

    private Point pointToTile(int x, int y) {
        Point result = new Point();
        result.x = ((x - 25) / 3) + 1152;
        result.y = 4159 - (((y - 25) / 3) + 64);
        return result;
    }

    private Point tileToPoint(int x, int y) {
        Point result = new Point();
        result.x = 25 + ((x - 1152) * 3);
        result.y = 4850 - (25 + ((y - 2495) * 3));
        return result;
    }

    private Point pointToScreen(int x, int y) {
        return new Point((int) Math.floor((x + (xOffset / zoomFactor)) * zoomFactor), (int) Math.floor((y + (yOffset / zoomFactor)) * zoomFactor));
    }

    public WebNode getHoveredNode() {
        return hoveredNode;
    }
}
