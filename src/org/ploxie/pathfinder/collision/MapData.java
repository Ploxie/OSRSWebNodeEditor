package org.ploxie.pathfinder.collision;

import org.ploxie.pathfinder.utils.Position;

import java.util.HashMap;
import java.util.Map;

public class MapData {

    public Map<Position, Region2> regions = new HashMap<>();



    public boolean addRegion(Region2 region){
        if(regions.containsKey(region.getBasePosition())){
            return false;
        }

        regions.put(region.getBasePosition(), region);
        return true;
    }
}
