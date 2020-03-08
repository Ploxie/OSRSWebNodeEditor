package org.ploxie.pathfinder.collision;


import org.ploxie.pathfinder.utils.Position;
import org.rspeer.ui.Log;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Predicate;

public class RegionFileIO {

    public static boolean saveRegion(Region2 region) {
        File file = new File("C:\\Users\\Ploxie\\.rspeer\\" +Region.REGION_WIDTH+"\\" +  region.getID() +"_"+region.getPlane()+ ".wbrb");
        try {
            FileOutputStream writer = new FileOutputStream(file);

            int baseX = region.getBasePosition().getX();
            int baseY = region.getBasePosition().getY();
            int baseZ = region.getBasePosition().getZ();

            for (int y = 0; y < Region.REGION_WIDTH; y++) {
                for (int x = 0; x < Region.REGION_WIDTH; x++) {
                    Position worldPos = new Position((baseX) + x, (baseY) + y, baseZ);

                    CollisionData collisionData = region.getCollisionData(worldPos);

                    if(collisionData == null){
                        continue;
                    }

                    int value = collisionData.getCollisionValue();

                    writer.write(new byte[] {
                            (byte)((value >> 24) & 0xff),
                            (byte)((value >> 16) & 0xff),
                            (byte)((value >> 8) & 0xff),
                            (byte)((value >> 0) & 0xff),
                    });

                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Region2 loadRegion(Position regionPosition) {
        File file = new File("C:\\Users\\Ploxie\\.rspeer\\"+Region.REGION_WIDTH+"\\" + regionPosition + ".wbrb");
        return loadRegion(file);
    }

    public static Region2 loadRegion(File regionFile) {
        int regionID = Integer.parseInt(regionFile.getName().split("_")[0]);
        int plane = Integer.parseInt(regionFile.getName().split("_")[1].replace(".wbrb", ""));
        Region2 region = null;
        try {
            FileInputStream reader = new FileInputStream(regionFile);

            byte[] data = new byte[reader.available()];
            reader.read(data);
            IntBuffer buffer = ByteBuffer.wrap(data).asIntBuffer();
            int[][] collisionData = new int[Region.REGION_WIDTH][Region.REGION_WIDTH];
            for(int i = 0; i < Region.REGION_WIDTH;i++){
                buffer.get(collisionData[i], 0, Region.REGION_WIDTH);
            }

            region = new Region2(regionID, plane, collisionData);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return region;
    }

    public static MapData loadMapData(){
        File folder = new File("C:\\Users\\Ploxie\\.rspeer\\"+Region.REGION_WIDTH);
        File[] regionFiles = folder.listFiles(pathname -> pathname.getName().endsWith(".wbrb"));
        MapData mapData = new MapData();

        if(regionFiles == null){
            return mapData;
        }

        for(File file : regionFiles){
            Region2 region = loadRegion(file);
            mapData.addRegion(region);
            //Log.info("Loaded Region: "+region.getID());
            System.out.println("Loaded Region: "+region.getID());
        }

        return mapData;
    }
}
