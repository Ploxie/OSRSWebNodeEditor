package org.ploxie.pathfinder2.collision;

import org.ploxie.pathfinder2.util.Position;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class RegionFileIO {

    public static void saveRegion(Region region) {
        Position regionPos = region.getPosition();
        File file = new File("C:\\Users\\Ploxie\\.rspeer\\" +Region.REGION_WIDTH+"\\" +  regionPos + ".wbrb");
        try {
            FileOutputStream writer = new FileOutputStream(file);

            int baseX = region.getPosition().getX();
            int baseY = region.getPosition().getY();
            int baseZ = region.getPosition().getZ();

            writer.write(baseX);
            writer.write(baseY);
            writer.write(baseZ);


            for (int y = 0; y < Region.REGION_WIDTH; y++) {
                for (int x = 0; x < Region.REGION_WIDTH; x++) {
                    Position worldPos = new Position((baseX * Region.REGION_WIDTH) + x, (baseY * Region.REGION_WIDTH) + y, baseZ);

                    CollisionData collisionData = region.get(worldPos);

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
        }
    }

    public static Region loadRegion(Position regionPosition) {

        File file = new File("C:\\Users\\Ploxie\\.rspeer\\"+Region.REGION_WIDTH+"\\" + regionPosition + ".wbrb");

        Region region = null;
        try {
            FileInputStream reader = new FileInputStream(file);

            int baseX = reader.read();
            int baseY = reader.read();
            int baseZ = reader.read();

            int x = 0;
            int y = 0;

            byte[] data = new byte[reader.available()];
            reader.read(data);
            IntBuffer buffer = ByteBuffer.wrap(data).asIntBuffer();
            int[] collisionData = new int[buffer.remaining()];
            buffer.get(collisionData);
            region = new Region(regionPosition, collisionData);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return region;
    }
}
