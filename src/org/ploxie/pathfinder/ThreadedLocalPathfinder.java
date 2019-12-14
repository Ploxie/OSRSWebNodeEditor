package org.ploxie.pathfinder;

import org.ploxie.pathfinder.collision.CollisionData;
import org.ploxie.pathfinder.collision.Region;
import org.ploxie.pathfinder.util.Position;
import org.ploxie.pathfinder.web.Web;
import org.ploxie.pathfinder.web.WebNode;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadedLocalPathfinder extends LocalPathfinder {

    private ReadWriteLock openListReadWriteLock = new ReentrantReadWriteLock();
    private Lock openListReadLock = openListReadWriteLock.readLock();

    public ThreadedLocalPathfinder(Region... regionLimit) {
        super(regionLimit);
    }

    public ThreadedLocalPathfinder(List<Region> regionLimit) {
        super(regionLimit);
    }

    @Override
    public WebPath findPath(WebNode start, WebNode end) {
        this.end = end;
        openList = new PriorityQueue<>(end.distanceTo(start), Comparator.comparingDouble(WebNode::getTotalCost));
        closedSet = new HashSet<>();

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

    @Override
    protected void addNeighbours(WebNode node) {

        addNeighbour(node, 0, 1, 0, STEP_COST);
        addNeighbour(node, 0, -1, 0, STEP_COST);
        addNeighbour(node, 1, 0, 0, STEP_COST);
        addNeighbour(node, -1, 0, 0, STEP_COST);

        addNeighbour(node, -1, 1, 0, STEP_DIAGONAL_COST);
        addNeighbour(node, 1, 1, 0, STEP_DIAGONAL_COST);
        addNeighbour(node, -1, -1, 0, STEP_DIAGONAL_COST);
        addNeighbour(node, 1, -1, 0, STEP_DIAGONAL_COST);
    }

    private void addNeighbour(WebNode current, int xTranslate, int yTranslate, int zTranslate, int cost) {
        WebNode targetNode = new WebNode(current.translate(xTranslate, yTranslate, zTranslate));

        if (closedSet.contains(targetNode) || openList.contains(targetNode)) {
            return;
        }

        Region currentRegion = getRegion(current);
        Region targetRegion = getRegion(targetNode);

        if (targetRegion == null || !targetRegion.contains(targetNode)) {
            return;
        }

        CollisionData collisionData = currentRegion.get(current);
        CollisionData targetCollisionData = targetRegion.get(targetNode);

        if (!targetCollisionData.isWalkable() || collisionData.blockedInDirection(xTranslate, yTranslate)) {
            return;
        }

        double g = current.getGCost() + cost;
        //targetNode.calculateHeuristic(end);

        targetNode.setParent(current);
        targetNode.setGCost(g);

        openList.add(targetNode);

    }
}
