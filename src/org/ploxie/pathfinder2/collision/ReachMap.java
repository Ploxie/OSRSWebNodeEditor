package org.ploxie.pathfinder2.collision;

import org.ploxie.pathfinder2.util.Position;

import java.util.HashMap;

public class ReachMap {

    private Position base;

    public HashMap<Position, Boolean> reachMap = new HashMap<>();

    public ReachMap(Position base){
        this.base = base;
    }

    public void put(Position position, boolean canReach){
        reachMap.put(position, canReach);
    }

    public boolean get(Position worldPosition){
        if(!reachMap.containsKey(worldPosition)){
            return false;
        }
        return get(worldPosition);
    }

}
