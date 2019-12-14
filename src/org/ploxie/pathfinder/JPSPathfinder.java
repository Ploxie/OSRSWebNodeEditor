package org.ploxie.pathfinder;

import org.ploxie.pathfinder.collision.CollisionData;
import org.ploxie.pathfinder.collision.CollisionFlags;
import org.ploxie.pathfinder.collision.Region;
import org.ploxie.pathfinder.util.Position;
import org.ploxie.pathfinder.web.Web;
import org.ploxie.pathfinder.web.WebNode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class JPSPathfinder extends AStarPathfinder {

    private static final int STEP_COST = 10;
    private static final int STEP_DIAGONAL_COST = 20;

    private HashMap<Position, Region> regionLimit;

    private Web localWeb;
    private WebNode goal;

    public JPSPathfinder(Region... regionLimit) {
        this.regionLimit = new HashMap<>();
        for (Region region : regionLimit) {
            this.regionLimit.put(region.getPosition(), region);
        }
    }

    public JPSPathfinder(List<Region> regionLimit) {
        this.regionLimit = new HashMap<>();
        for (Region region : regionLimit) {
            this.regionLimit.put(region.getPosition(), region);
        }
    }

    @Override
    public WebPath findPath(WebNode start, WebNode end) {
        localWeb = new Web();
        goal = end;
        return super.findPath(start, end);
    }


    @Override
    protected void addNeighbours(WebNode node) {
        addNeighbourJump(node, 0, 1, 0, STEP_COST);
        addNeighbourJump(node, 0, -1, 0, STEP_COST);
        addNeighbourJump(node, 1, 0, 0, STEP_COST);
        addNeighbourJump(node, -1, 0, 0, STEP_COST);

        addNeighbourJump(node, -1, 1, 0, STEP_DIAGONAL_COST);
        addNeighbourJump(node, 1, 1, 0, STEP_DIAGONAL_COST);
        addNeighbourJump(node, -1, -1, 0, STEP_DIAGONAL_COST);
        addNeighbourJump(node, 1, -1, 0, STEP_DIAGONAL_COST);
    }

    private Region getRegion(Position worldPosition) {
        Position regionPos = Region.worldToRegionPosition(worldPosition);
        return regionLimit.get(regionPos);
    }

    private int calculateGScore(WebNode newNode, WebNode oldNode)
    {
        int dx = newNode.getX() - oldNode.getX();
        int dy = newNode.getY() - oldNode.getY();

        if(dx == 0 || dy == 0) //Horizontal or vertical
            return Math.abs(10 * Math.max(dx, dy));
        else                   // Diaganol
            return Math.abs(14 * Math.max(dx, dy));
    }


    private void addNeighbourJump(WebNode current, int xTranslate, int yTranslate, int zTranslate, int cost) {

        Position targetPosition = current.translate(xTranslate, yTranslate, current.getPlane());
        WebNode targetNode = new WebNode(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ());

        if (!canGoTo(current, targetNode)) {
            return;
        }

        if (closedSet.contains(targetPosition)) {
            return;
        }

        WebNode jumpNode = jump(current, xTranslate, yTranslate, cost);

        if (jumpNode != null) {
            if (!openList.contains(jumpNode) && !closedSet.contains(jumpNode)) {
                double g = current.getGCost() + (current.distanceTo(jumpNode));
                jumpNode.calculateHeuristic(goal, 1);

                jumpNode.setParent(current);
                jumpNode.setGCost(g);
                openList.add(jumpNode);

            }

            double g = current.getGCost() + (current.distanceTo(jumpNode));
            double dist = jumpNode.calculateHeuristic(goal, 1);

            if ((g +dist) < jumpNode.getGCost()) {
                jumpNode.setParent(current);
                jumpNode.setGCost(g);
                openList.remove(jumpNode);
                openList.add(jumpNode);
            }
        }
    }

    private WebNode jump(WebNode current, int xTranslate, int yTranslate, int cost) {
        Position targetPosition = current.translate(xTranslate, yTranslate, current.getPlane());
        WebNode targetNode = new WebNode(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ());

        if (!canGoTo(current, targetNode)) {
            return null;
        }

        if (targetNode.equals(goal)) {
            //System.out.println("A");
            return targetNode;
        }

        if (closedSet.contains(targetPosition)) {
            return null;
        }

        //double g = current.getGCost() + (current.distanceTo(targetNode));

        //Node.setGCost(g);


        if (xTranslate != 0 && yTranslate != 0) {
            if (!canGoTo(targetNode, new WebNode(targetPosition.translate(-xTranslate, 0, 0))) && canGoTo(targetNode, new WebNode(targetPosition.translate(-xTranslate, yTranslate, 0)))) {
                return targetNode;
            }

            if (!canGoTo(targetNode, new WebNode(targetPosition.translate(0, -yTranslate, 0))) && canGoTo(targetNode, new WebNode(targetPosition.translate(xTranslate, -yTranslate, 0)))) {
                return targetNode;
            }

            if (jump(targetNode, xTranslate, 0, cost) != null || jump(targetNode, 0, yTranslate, cost) != null) {
                return targetNode;
            }
        } else {
            if (xTranslate != 0) {
                if (canGoTo(targetNode, new WebNode(targetPosition.translate(xTranslate, 0, 0))) && !canGoTo(targetNode, new WebNode(targetPosition.translate(0, 1, 0)))) {
                    if (canGoTo(targetNode, new WebNode(targetPosition.translate(xTranslate, 1, 0)))) {
                        return targetNode;
                    }
                }

                if (canGoTo(targetNode, new WebNode(targetPosition.translate(xTranslate, 0, 0))) && !canGoTo(targetNode, new WebNode(targetPosition.translate(0, -1, 0)))) {
                    if (canGoTo(targetNode, new WebNode(targetPosition.translate(xTranslate, -1, 0)))) {
                        return targetNode;
                    }
                }
            } else {
                if (canGoTo(targetNode, new WebNode(targetPosition.translate(0, yTranslate, 0))) && !canGoTo(targetNode, new WebNode(targetPosition.translate(1, 0, 0)))) {
                    if (canGoTo(targetNode, new WebNode(targetPosition.translate(1, yTranslate, 0)))) {
                        return targetNode;
                    }
                }

                if (canGoTo(targetNode, new WebNode(targetPosition.translate(0, yTranslate, 0))) && !canGoTo(targetNode, new WebNode(targetPosition.translate(-1, 0, 0)))) {
                    if (canGoTo(targetNode, new WebNode(targetPosition.translate(-1, yTranslate, 0)))) {
                        return targetNode;
                    }
                }
            }
        }

        //targetNode.setParent(current);

        return jump(targetNode, xTranslate, yTranslate, cost);
    }

    private boolean canGoTo(WebNode current, WebNode target) {
        int xTranslate = target.getX() - current.getX();
        int yTranslate = target.getY() - current.getY();

        Position targetPosition = current.translate(xTranslate, yTranslate, current.getPlane());
        WebNode targetNode = new WebNode(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ());

        Region currentRegion = getRegion(current);
        Region targetRegion = getRegion(targetPosition);

        if (targetRegion == null || !targetRegion.contains(targetNode)) {
            return false;
        }

        CollisionData collisionData = currentRegion.get(current);
        CollisionData targetCollisionData = targetRegion.get(targetPosition);

        if (!targetCollisionData.isWalkable()) {
            return false;
        }

        if (xTranslate > 0 && collisionData.blockedEast()) {
            return false;
        }
        if (xTranslate < 0 && collisionData.blockedWest()) {
            return false;
        }
        if (yTranslate > 0 && collisionData.blockedNorth()) {
            return false;
        }
        if (yTranslate < 0 && collisionData.blockedSouth()) {
            return false;
        }

        if (xTranslate < 0 && yTranslate > 0 && collisionData.blockedNorthWest()) {
            return false;
        }

        if (xTranslate > 0 && yTranslate > 0 && collisionData.blockedNorthEast()) {
            return false;
        }

        if (xTranslate < 0 && yTranslate < 0 && collisionData.blockedSouthWest()) {
            return false;
        }

        if (xTranslate > 0 && yTranslate < 0 && collisionData.blockedSouthEast()) {
            return false;
        }
        return true;
    }

}
