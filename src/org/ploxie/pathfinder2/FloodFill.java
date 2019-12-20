package org.ploxie.pathfinder2;

import org.ploxie.pathfinder2.collision.CollisionData;
import org.ploxie.pathfinder2.collision.ReachMap;
import org.ploxie.pathfinder2.collision.Region;
import org.ploxie.pathfinder2.util.Position;
import org.ploxie.pathfinder2.web.WebNode;

import java.util.*;

public class FloodFill extends AStarPathfinder {

    protected static final int STEP_COST = 10;
    protected static final int STEP_DIAGONAL_COST = 14;

    protected HashMap<Position, Region> regionLimit;

    public ReachMap reachMap;

    public FloodFill(Region regionLimit) {
        this.regionLimit = new HashMap<>();
        this.regionLimit.put(regionLimit.getPosition(), regionLimit);
    }

    protected Region getRegion(WebNode node) {
        return regionLimit.get(node.getRegionPosition());
    }

    @Override
    public WebPath findPath(WebNode start, WebNode end) {
        reachMap = new ReachMap(getRegion(start).getWorldPosition());
        this.end = end;
        openList = new PriorityQueue<>(end.distanceTo(start), Comparator.comparingDouble(WebNode::getTotalCost));
        closedSet = new HashSet<>();

        //openList.add(getNode(start.getX(),start.getY(),start.getPlane()));

        openList.add(start);

        while (openList.size() > 0) {
            WebNode currentNode = openList.poll();
            closedSet.add(currentNode);

            addNeighbours(currentNode);

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
        WebNode horizontalNode = new WebNode(current.translate(xTranslate,0,0));
        WebNode verticalNode = new WebNode(current.translate(0,yTranslate,0));

        if (closedSet.contains(targetNode)) {
            return;
        }

        Region currentRegion = getRegion(current);
        Region targetRegion = getRegion(targetNode);
        Region horizontalRegion = getRegion(horizontalNode);
        Region verticalRegion = getRegion(verticalNode);

        if (targetRegion == null || !targetRegion.contains(targetNode)) {
            return;
        }

        CollisionData collisionData = currentRegion.get(current);
        CollisionData targetCollisionData = targetRegion.get(targetNode);
        CollisionData horizontalCollisionData = horizontalRegion.get(horizontalNode);
        CollisionData verticalCollisionData = verticalRegion.get(verticalNode);


        if (!targetCollisionData.isWalkable() || collisionData.blockedInDirection(xTranslate, yTranslate)) {
            return;
        }

        if(!horizontalCollisionData.isWalkable() || !verticalCollisionData.isWalkable()){
            return;
        }

        if(current.equals(new Position(3213,3221,0))){
            System.out.println(collisionData.blockedInDirection(xTranslate, yTranslate) + ", "+xTranslate+", "+yTranslate);
        }

        if (!openList.contains(targetNode)) {
            double g = current.getGCost() + cost;
            targetNode.calculateHeuristic(end);

            targetNode.setParent(current);
            targetNode.setGCost(g);

            openList.add(targetNode);
            reachMap.put(targetNode, true);
            return;
        }

    }

}
