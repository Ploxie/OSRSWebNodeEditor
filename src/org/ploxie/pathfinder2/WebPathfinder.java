package org.ploxie.pathfinder2;

import org.ploxie.pathfinder2.collision.Region;
import org.ploxie.pathfinder2.collision.RegionFileIO;
import org.ploxie.pathfinder2.util.Position;
import org.ploxie.pathfinder2.web.WebNode;

import java.util.ArrayList;
import java.util.List;

public class WebPathfinder extends AStarPathfinder{

    public Pathfinder nodePathfinder;
    public Pathfinder localPathfinder;

    public WebPathfinder(){
        this.nodePathfinder = new AStarPathfinder();
    }

    @Override
    public WebPath findPath(WebNode start, WebNode end) {

        //Start = nearest to start

        long startTime = System.currentTimeMillis();
        WebPath nodePath = nodePathfinder.findPath(start, end);
        System.out.println("Loaded node path in: "+(System.currentTimeMillis()-startTime)+" ms");

        startTime = System.currentTimeMillis();
        List<Position> regionPositions = getRegionPositionsInNodePath(nodePath);
        System.out.println("Found regions path in: "+(System.currentTimeMillis()-startTime)+" ms");

        startTime = System.currentTimeMillis();
        List<Region> loadedRegions = loadRegionsInPath(regionPositions);
        System.out.println("Loaded regions in: "+(System.currentTimeMillis()-startTime)+" ms");
        System.out.println("Searching "+loadedRegions.size()+" regions");

        localPathfinder = new ThreadedLocalPathfinder(loadedRegions);

        startTime = System.currentTimeMillis();
        WebPath localPath = localPathfinder.findPath(start, end);
        System.out.println("Loaded path in: "+(System.currentTimeMillis()-startTime)+" ms");
        System.out.println(localPath);

        return nodePath;
    }

    private List<Region> loadRegionsInPath(List<Position> regionPathPositions){
        List<Region> regions = new ArrayList<>();

        for(Position position : regionPathPositions){
            Region region = RegionFileIO.loadRegion(position);
            if(region == null){
                throw new RuntimeException("Could not load region: "+position);
            }
            regions.add(region);
        }

        return regions;
    }

    private List<Position> getRegionPositionsInNodePath(WebPath nodePath){
        List<Position> positions = new ArrayList<>();

        Position lastRegionPosition = null;
        for(int i = 0; i < nodePath.size();i++){
            Position nodePosition = nodePath.get(i);
            Position currentRegionPosition = Region.worldToRegionPosition(nodePosition);

            if(lastRegionPosition == null){
                lastRegionPosition = currentRegionPosition;
                continue;
            }

            int minX = Math.min(currentRegionPosition.getX(), lastRegionPosition.getX());
            int maxX = Math.max(currentRegionPosition.getX(), lastRegionPosition.getX());
            int minY = Math.min(currentRegionPosition.getY(), lastRegionPosition.getY());
            int maxY = Math.max(currentRegionPosition.getY(), lastRegionPosition.getY());


            int m = (maxY - minY);
            int error = m - (maxX - minX);


            for(int x = minX, y = minY; x <= maxX && y <= maxY;){
                Position regionPosition = new Position(x,y,lastRegionPosition.getZ());
                if(!positions.contains(regionPosition)){
                    positions.add(regionPosition);
                }

                if(error > 0){
                    y++;
                    error -= 2 * (maxX - minX);
                }else{
                    error += m;
                    x++;
                }
            }

            lastRegionPosition = currentRegionPosition;
        }

        return positions;
    }

}
