package org.ploxie.pathfinder;

import org.ploxie.pathfinder.util.Position;
import org.ploxie.pathfinder.web.Web;
import org.ploxie.pathfinder.web.WebNode;
import org.ploxie.pathfinder.web.WebNodeConnection;
import sun.rmi.runtime.Log;

import java.util.*;

public class AStarPathfinder implements Pathfinder {

    protected PriorityQueue<WebNode> openList;
    public HashSet<WebNode> closedSet;

    protected Web web;
    protected WebNode end;

    @Override
    public WebPath findPath(WebNode start, WebNode end) {
        this.end = end;
        openList = new PriorityQueue<>(end.distanceTo(start),Comparator.comparingDouble(WebNode::getTotalCost));
        closedSet = new HashSet<>();

        //openList.add(getNode(start.getX(),start.getY(),start.getPlane()));

        openList.add(start);

        while (openList.size() > 0) {
            WebNode currentNode = openList.poll();
            closedSet.add(currentNode);

            if (isEndNode(currentNode)) {
                return backtracePath(currentNode);
            } else {
                addNeighbours(currentNode);
            }
        }

        return null;
    }

    protected void addNeighbours(WebNode node) {
        for (WebNodeConnection connection : node.getConnections()) {
            WebNode target = connection.getTarget();
            if (closedSet.contains(target)) {
                continue;
            }

            if (!openList.contains(target)) {
                double cost = node.distanceTo(target);
                double g = node.getGCost()+cost;
                target.calculateHeuristic(end);

                target.setParent(node);
                target.setGCost(g);

                openList.add(target);
                continue;
            }

            double cost = node.distanceTo(target);
            double g = node.getGCost() + cost;

            if ((g) < target.getGCost()) {
                target.setParent(node);
                target.setGCost(g + cost);

                openList.remove(target);
                openList.add(target);
            }
        }
    }

    protected WebPath backtracePath(WebNode node) {

        WebPath path = new WebPath();
        path.add(node);
        WebNode parent;
        while ((parent = node.getParent()) != null) {
            path.add(0, parent);
            node = parent;
        }

        return path;
    }

    protected boolean isEndNode(WebNode node) {
        return node.equals(end);
    }

}
