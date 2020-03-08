package org.ploxie.gui.overlays;

import org.ploxie.gui.WorldMapViewer;
import org.ploxie.gui.map.WorldMap;
import org.ploxie.pathfinder.collision.MapData;
import org.ploxie.pathfinder.collision.Region;
import org.ploxie.pathfinder.collision.Region2;

import java.awt.*;

public class CollisionOverlay extends Overlay {

    private MapData mapData;

    public CollisionOverlay(WorldMapViewer mapViewer, MapData mapData) {
        super(mapViewer);
        this.mapData = mapData;
    }

    @Override
    public void draw(Graphics2D g) {

        Rectangle viewport = mapViewer.getViewport();

        int xOffset = (int)-viewport.getX();
        int yOffset = (int)-viewport.getY();

        g.setColor(Color.RED);
        for(Region2 region : mapData.regions.values()){
            WorldMap.WorldTile worldTile = new WorldMap.WorldTile(mapViewer.getWorldMap(), region.getBasePosition().getX(), region.getBasePosition().getY());
            WorldMap.MapTile mapTile = worldTile.toMapTile();
            WorldMap.MapPoint mapPoint = mapTile.toMapPoint();

            //g.fillRect(mapPoint.getX()+xOffset, mapPoint.getY()+yOffset, 100, 100);

            drawTile(g, region.getBasePosition());
        }

    }
}
