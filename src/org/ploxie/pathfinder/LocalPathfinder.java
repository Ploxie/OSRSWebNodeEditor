package org.ploxie.pathfinder;

import org.ploxie.pathfinder.collision.CollisionData;
import org.ploxie.pathfinder.collision.Region;
import org.ploxie.pathfinder.util.Position;
import org.ploxie.pathfinder.web.Web;
import org.ploxie.pathfinder.web.WebNode;

import java.util.*;

public class LocalPathfinder extends AStarPathfinder {

    protected static final int STEP_COST = 10;
    protected static final int STEP_DIAGONAL_COST = 14;

    protected HashMap<Position, Region> regionLimit;

    public LocalPathfinder(Region... regionLimit) {
        this.regionLimit = new HashMap<>();
        for (Region region : regionLimit) {
            this.regionLimit.put(region.getPosition(), region);
        }
    }

    public LocalPathfinder(List<Region> regionLimit) {
        this.regionLimit = new HashMap<>();
        for (Region region : regionLimit) {
            this.regionLimit.put(region.getPosition(), region);
        }
    }

    protected Region getRegion(WebNode node) {
        return regionLimit.get(node.getRegionPosition());

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

    private boolean isBlockedInDirection(WebNode current, int xTranslate, int yTranslate){
        WebNode targetNode = new WebNode(current.translate(xTranslate, yTranslate,0));

        Region currentRegion = getRegion(current);

        CollisionData collisionData = currentRegion.get(current);
        if (!currentRegion.get(targetNode).isWalkable() || collisionData.blockedInDirection(xTranslate, yTranslate)) {
            return true;
        }

        if(!currentRegion.get(new WebNode(current.translate(xTranslate,0,0))).isWalkable()){
            return true;
        }

        if(!currentRegion.get(new WebNode(current.translate(0,yTranslate,0))).isWalkable()){
            return true;
        }

        return false;
    }

    private void addNeighbour(WebNode current, int xTranslate, int yTranslate, int zTranslate, int cost) {
        WebNode targetNode = new WebNode(current.translate(xTranslate, -yTranslate,zTranslate));

        if (closedSet.contains(targetNode)) {
            return;
        }

        Region currentRegion = getRegion(current);
        Region targetRegion = getRegion(targetNode);


        if (targetRegion == null || !targetRegion.contains(targetNode)) {
            return;
        }

        CollisionData collisionData = currentRegion.get(current);
        CollisionData targetCollisionData = targetRegion.get(targetNode);

        if (!targetCollisionData.isWalkable() || collisionData.blockedInDirection(xTranslate, -yTranslate)) {
            return;
        }

        if(!isBlockedInDirection(current, xTranslate,-yTranslate)){
            return;
        }

        if (!openList.contains(targetNode)) {
            double g = current.getGCost() + cost;
            targetNode.calculateHeuristic(end);

            targetNode.setParent(current);
            targetNode.setGCost(g);

            openList.add(targetNode);
            return;
        }

    }
}
