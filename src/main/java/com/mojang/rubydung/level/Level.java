package com.mojang.rubydung.level;

import com.mojang.rubydung.phys.AABB;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Level {

    public final int width;
    public final int height;
    public final int depth;

    private final byte[] blocks;
    private final int[] lightDepths;

    /**
     * Three dimensional level containing all tiles
     *
     * @param width  Level width
     * @param height Level height
     * @param depth  Level depth
     */
    public Level(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.blocks = new byte[width * height * depth];
        this.lightDepths = new int[width * height];

        // Check if level file is available
        File levelFile = new File("level.dat");
        if (levelFile.exists()) {
            // Load existing level
            load();
        } else {
            // Fill level with tiles
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < depth; y++) {
                    for (int z = 0; z < height; z++) {
                        // Calculate index from x, y and z
                        int index = (y * this.height + z) * this.width + x;

                        // Fill level with tiles
                        this.blocks[index] = (byte) 1;
                    }
                }
            }

            // Generate caves
            for (int i = 0; i < 10000; i++) {
                int caveSize = (int) (Math.random() * 7) + 1;

                int caveX = (int) (Math.random() * width);
                int caveY = (int) (Math.random() * depth);
                int caveZ = (int) (Math.random() * height);

                // Grow cave
                for (int radius = 0; radius < caveSize; radius++) {
                    for (int sphere = 0; sphere < 1000; sphere++) {
                        int offsetX = (int) (Math.random() * radius * 2 - radius);
                        int offsetY = (int) (Math.random() * radius * 2 - radius);
                        int offsetZ = (int) (Math.random() * radius * 2 - radius);

                        // Sphere shape
                        double distance = Math.pow(offsetX, 2) + Math.pow(offsetY, 2) + Math.pow(offsetZ, 2);
                        if (distance > radius * radius)
                            continue;

                        int tileX = caveX + offsetX;
                        int tileY = caveY + offsetY;
                        int tileZ = caveZ + offsetZ;

                        // Calculate index from x, y and z
                        int index = (tileY * this.height + tileZ) * this.width + tileX;

                        // Check if tile is out of level
                        if (index >= 0 && index < this.blocks.length) {

                            // Border of level
                            if (tileX > 0 && tileY > 0 && tileZ > 0
                                    && tileX < this.width - 1 && tileY < this.depth && tileZ < this.height - 1) {

                                // Fill level with tiles
                                this.blocks[index] = (byte) 0;
                            }
                        }
                    }
                }
            }

            // Calculate light depth of the entire level
            calcLightDepths(0, 0, width, height);
        }
    }

    /**
     * Load blocks from level.dat
     */
    public void load() {
        try {
            DataInputStream dis = new DataInputStream(new GZIPInputStream(new FileInputStream("level.dat")));
            dis.readFully(this.blocks);
            calcLightDepths(0, 0, this.width, this.height);
            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Store blocks in level.dat
     */
    public void save() {
        try {
            DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream("level.dat")));
            dos.write(this.blocks);
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculate light depth of given area
     *
     * @param minX Minimum on X axis
     * @param minZ Minimum on Z axis
     * @param maxX Maximum on X axis
     * @param maxZ Maximum on Z axis
     */
    private void calcLightDepths(int minX, int minZ, int maxX, int maxZ) {
        // For each x/z position in level
        for (int x = minX; x < minX + maxX; x++) {
            for (int z = minZ; z < minZ + maxZ; z++) {

                // Get previous light depth value
                int prevDepth = this.lightDepths[x + z * this.width];

                // Calculate new light depth
                int depth = this.depth - 1;
                while (depth > 0 && !isLightBlocker(x, depth, z)) {
                    depth--;
                }

                // Set new light depth
                this.lightDepths[x + z * this.width] = depth;
            }
        }
    }

    /**
     * Return true if a tile is available at the given location
     *
     * @param x Level position x
     * @param y Level position y
     * @param z Level position z
     * @return Tile available
     */
    public boolean isTile(int x, int y, int z) {
        // Is location out of the level?
        if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.depth || z >= this.height) {
            return false;
        }

        // Calculate index from x, y and z
        int index = (y * this.height + z) * this.width + x;

        // Return true if there is a tile at this location
        return this.blocks[index] != 0;
    }

    /**
     * Returns true if tile is solid and not transparent
     *
     * @param x Tile position x
     * @param y Tile position y
     * @param z Tile position z
     * @return Tile is solid
     */
    public boolean isSolidTile(int x, int y, int z) {
        return isTile(x, y, z);
    }

    /**
     * Returns true if the tile is blocking the light
     *
     * @param x Tile position x
     * @param y Tile position y
     * @param z Tile position z
     * @return Tile blocks the light
     */
    public boolean isLightBlocker(final int x, final int y, final int z) {
        return this.isSolidTile(x, y, z);
    }

    /**
     * Get brightness of a tile
     *
     * @param x Tile position x
     * @param y Tile position y
     * @param z Tile position z
     * @return The brightness value from 0 to 1
     */
    public float getBrightness(int x, int y, int z) {
        // Define brightness
        float dark = 0.8F;
        float light = 1.0F;

        // Is light tile
        if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.depth || z >= this.height) {
            return light;
        }

        // Is dark tile
        if (y < this.lightDepths[x + z * this.width]) {
            return dark;
        }

        // Unknown brightness
        return light;
    }


    /**
     * Get bounding box of all tiles surrounded by the given bounding box
     *
     * @param boundingBox Target bounding box located in the level
     * @return List of bounding boxes representing the tiles around the given bounding box
     */
    public ArrayList<AABB> getCubes(AABB boundingBox) {
        ArrayList<AABB> boundingBoxList = new ArrayList<>();

        int minX = (int) (Math.floor(boundingBox.minX) - 1);
        int maxX = (int) (Math.ceil(boundingBox.maxX) + 1);
        int minY = (int) (Math.floor(boundingBox.minY) - 1);
        int maxY = (int) (Math.ceil(boundingBox.maxY) + 1);
        int minZ = (int) (Math.floor(boundingBox.minZ) - 1);
        int maxZ = (int) (Math.ceil(boundingBox.maxZ) + 1);

        // Minimum level position
        minX = Math.max(0, minX);
        minY = Math.max(0, minY);
        minZ = Math.max(0, minZ);

        // Maximum level position
        maxX = Math.min(this.width, maxX);
        maxY = Math.min(this.depth, maxY);
        maxZ = Math.min(this.height, maxZ);

        // Include all surrounding tiles
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    if (isSolidTile(x, y, z)) {
                        boundingBoxList.add(new AABB(x, y, z, x + 1, y + 1, z + 1));
                    }
                }
            }
        }
        return boundingBoxList;
    }
}
