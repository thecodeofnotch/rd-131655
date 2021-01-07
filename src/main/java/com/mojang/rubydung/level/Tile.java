package com.mojang.rubydung.level;

public class Tile {

    public static Tile rock = new Tile();

    /**
     * Render a tile at the given position
     *
     * @param tessellator Tessellator for rendering
     * @param level       Level to check for surrounding tiles
     * @param x           Tile position x
     * @param y           Tile position y
     * @param z           Tile position z
     */
    public void render(Tessellator tessellator, Level level, int x, int y, int z) {
        float minX = x + 0.0F;
        float maxX = x + 1.0F;
        float minY = y + 0.0F;
        float maxY = y + 1.0F;
        float minZ = z + 0.0F;
        float maxZ = z + 1.0F;

        // Render bottom face
        if (!level.isSolidTile(x, y - 1, z)) {
            tessellator.vertex(minX, minY, maxZ);
            tessellator.vertex(minX, minY, minZ);
            tessellator.vertex(maxX, minY, minZ);
            tessellator.vertex(maxX, minY, maxZ);
        }

        // Render top face
        if (!level.isSolidTile(x, y + 1, z)) {
            tessellator.vertex(maxX, maxY, maxZ);
            tessellator.vertex(maxX, maxY, minZ);
            tessellator.vertex(minX, maxY, minZ);
            tessellator.vertex(minX, maxY, maxZ);
        }

        // Render side faces Z
        if (!level.isSolidTile(x, y, z - 1)) {
            tessellator.vertex(minX, maxY, minZ);
            tessellator.vertex(maxX, maxY, minZ);
            tessellator.vertex(maxX, minY, minZ);
            tessellator.vertex(minX, minY, minZ);
        }
        if (!level.isSolidTile(x, y, z + 1)) {
            tessellator.vertex(minX, maxY, maxZ);
            tessellator.vertex(minX, minY, maxZ);
            tessellator.vertex(maxX, minY, maxZ);
            tessellator.vertex(maxX, maxY, maxZ);
        }

        // Render side faces X
        if (!level.isSolidTile(x - 1, y, z)) {
            tessellator.vertex(minX, maxY, maxZ);
            tessellator.vertex(minX, maxY, minZ);
            tessellator.vertex(minX, minY, minZ);
            tessellator.vertex(minX, minY, maxZ);
        }
        if (!level.isSolidTile(x + 1, y, z)) {
            tessellator.vertex(maxX, minY, maxZ);
            tessellator.vertex(maxX, minY, minZ);
            tessellator.vertex(maxX, maxY, minZ);
            tessellator.vertex(maxX, maxY, maxZ);
        }
    }
}
