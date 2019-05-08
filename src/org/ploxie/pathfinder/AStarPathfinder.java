package org.ploxie.pathfinder;

import org.ploxie.pathfinder.web.WebNode;
import org.ploxie.pathfinder.web.WebNodeConnection;

import java.util.*;

public class AStarPathfinder implements Pathfinder {

    protected PriorityQueue<WebNode> openList;
    protected Set<WebNode> closedSet;

    protected WebNode end;

    public AStarPathfinder() {
        openList = new PriorityQueue<>(Comparator.comparingDouble(WebNode::getTotalCost));
        closedSet = new HashSet<>();
    }

    @Override
    public Collection<WebNode> findPath(WebNode start, WebNode end) {
        this.end = end;

        openList.clear();
        closedSet.clear();

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
                double g = node.getGCost();
                double dist = connection.getHeuristic();
                target.setParent(node);
                target.setGCost(g + dist);

                openList.add(target);

                continue;
            }

            double g = node.getGCost();
            double dist = connection.getHeuristic();

            if ((g + dist) < target.getGCost()) {
                target.setParent(node);
                target.setGCost(g + dist);

                openList.remove(target);
                openList.add(target);
            }
        }
    }

    protected Collection<WebNode> backtracePath(WebNode node) {
        List<WebNode> path = new ArrayList<WebNode>();
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
