package com.mojang.rubydung.level;

public class Level {

    public final int width;
    public final int height;
    public final int depth;

    private final byte[] blocks;

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

        // Create level with tiles
        this.blocks = new byte[width * height * depth];
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
            int caveX = (int) (Math.random() * width);
            int caveY = (int) (Math.random() * depth);
            int caveZ = (int) (Math.random() * height);

            int caveSize = (int) (Math.random() * 5) + 3;

            // Grow cave
            for (int radius = 0; radius < caveSize; radius++) {
                for (int sphere = 0; sphere < 1000; sphere++) {
                    int offsetX = (int) (Math.random() * radius * 2 - radius);
                    int offsetY = (int) (Math.random() * radius * 2 - radius);
                    int offsetZ = (int) (Math.random() * radius * 2 - radius);

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
}
