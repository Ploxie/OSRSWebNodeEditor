package org.ploxie.scripts;

import org.ploxie.pathfinder.collision.*;
import org.ploxie.pathfinder.utils.Position;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.Projection;
import org.rspeer.runetek.api.scene.Scene;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(name = "Map Recorder", desc = "", developer = "Ploxie", category = ScriptCategory.OTHER, version = 1.0)
public class MapRecorder extends Script implements RenderListener {

    private MapData mapData = new MapData();

    @Override
    public void onStart(){
        mapData = RegionFileIO.loadMapData();
    }

    private void record(){

        int currentRegionID = new Position(Players.getLocal().getX(), Players.getLocal().getY(), Players.getLocal().getFloorLevel()).getRegionID();
        Position regionBase = Region2.getRegionBase(currentRegionID);

        int[][] collisionValues = new int[Region.REGION_WIDTH][Region.REGION_WIDTH];

        for(int y = 0 ; y < Region.REGION_WIDTH;y++){
            for(int x = 0 ; x < Region.REGION_WIDTH;x++){
                org.rspeer.runetek.api.movement.position.Position position = new org.rspeer.runetek.api.movement.position.Position(regionBase.getX()+x, regionBase.getY()+y, Players.getLocal().getFloorLevel());
                if(!position.isLoaded()){
                    continue;
                }
                int collisionValue = Scene.getCollisionFlag(position);
                collisionValues[y][x] = collisionValue;
            }
        }

        Region2 region = new Region2(currentRegionID, Players.getLocal().getFloorLevel(), collisionValues);
        if(mapData.addRegion(region) && RegionFileIO.saveRegion(region)){
            Log.info("Saved Region: "+region.getID()+"_"+region.getPlane());
        }

    }

    @Override
    public int loop() {
        record();


        return 100;
    }


    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics g = renderEvent.getSource();
        g.setColor(Color.GREEN);

        for(Region2 region : mapData.regions.values()){
            for(int y = 0 ; y < Region.REGION_WIDTH; y++){
                for(int x = 0 ; x < Region.REGION_WIDTH; x++){
                    org.rspeer.runetek.api.movement.position.Position p = new org.rspeer.runetek.api.movement.position.Position(region.getBasePosition().getX()+x, region.getBasePosition().getY()+y, region.getBasePosition().getZ());
                    Point point = Projection.toMinimap(p);
                    Polygon s = Projection.getTileShape(p);

                    if (point != null) {


                        CollisionData collisionData = region.getCollisionData(new Position(region.getBasePosition().getX()+x, region.getBasePosition().getY()+y, region.getBasePosition().getZ()));
                        if(collisionData.isWalkable()){
                            g.fillRect(point.x, point.y, 3, 3);
                        }

                    }

                    if(s != null){
                        //g.fillPolygon(s);
                    }
                }
            }
        }

    }
}
