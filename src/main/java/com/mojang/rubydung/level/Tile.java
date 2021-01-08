package com.mojang.rubydung.level;

public class Tile {

    public static Tile grass = new Tile(0);
    public static Tile rock = new Tile(1);

    private final int textureId;

    public Tile(int textureId) {
        this.textureId = textureId;
    }

    /**
     * Render a tile at the given position
     *
     * @param tessellator Tessellator for rendering
     * @param level       Level to check for surrounding tiles
     * @param layer       The layer which decides if it's a shadow or not
     * @param x           Tile position x
     * @param y           Tile position y
     * @param z           Tile position z
     */
    public void render(Tessellator tessellator, Level level, int layer, int x, int y, int z) {
        float minU = this.textureId / 16.0F;
        float maxU = minU + 16 / 256F;
        float minV = 0.0F;
        float maxV = minV + 16 / 256F;

        float shadeX = 0.6f;
        float shadeY = 1.0f;
        float shadeZ = 0.8f;

        float minX = x + 0.0F;
        float maxX = x + 1.0F;
        float minY = y + 0.0F;
        float maxY = y + 1.0F;
        float minZ = z + 0.0F;
        float maxZ = z + 1.0F;

        // Render bottom face
        if (!level.isSolidTile(x, y - 1, z)) {
            // Get the brightness of the tile below
            float brightness = level.getBrightness(x, y - 1, z) * shadeY;

            // Don't render face if both conditions are the same (isShadowLayer != isFullBrightness)
            if (layer == 1 ^ brightness == shadeY) {
                tessellator.color(brightness, brightness, brightness);
                tessellator.texture(minU, maxV);
                tessellator.vertex(minX, minY, maxZ);
                tessellator.texture(minU, minV);
                tessellator.vertex(minX, minY, minZ);
                tessellator.texture(maxU, minV);
                tessellator.vertex(maxX, minY, minZ);
                tessellator.texture(maxU, maxV);
                tessellator.vertex(maxX, minY, maxZ);
            }
        }

        // Render top face
        if (!level.isSolidTile(x, y + 1, z)) {
            // Get the brightness of the tile above
            float brightness = level.getBrightness(x, y + 1, z) * shadeY;

            // Don't render face if both conditions are the same (isShadowLayer != isFullBrightness)
            if (layer == 1 ^ brightness == shadeY) {
                tessellator.color(brightness, brightness, brightness);
                tessellator.texture(maxU, maxV);
                tessellator.vertex(maxX, maxY, maxZ);
                tessellator.texture(maxU, minV);
                tessellator.vertex(maxX, maxY, minZ);
                tessellator.texture(minU, minV);
                tessellator.vertex(minX, maxY, minZ);
                tessellator.texture(minU, maxV);
                tessellator.vertex(minX, maxY, maxZ);
            }
        }

        // Render side faces Z
        if (!level.isSolidTile(x, y, z - 1)) {
            // Get the brightness of the tile next to it
            float brightness = level.getBrightness(x, y, z - 1) * shadeZ;

            // Don't render face if both conditions are the same (isShadowLayer != isFullBrightness)
            if (layer == 1 ^ brightness == shadeZ) {
                tessellator.color(brightness, brightness, brightness);
                tessellator.texture(maxU, minV);
                tessellator.vertex(minX, maxY, minZ);
                tessellator.texture(minU, minV);
                tessellator.vertex(maxX, maxY, minZ);
                tessellator.texture(minU, maxV);
                tessellator.vertex(maxX, minY, minZ);
                tessellator.texture(maxU, maxV);
                tessellator.vertex(minX, minY, minZ);
            }
        }
        if (!level.isSolidTile(x, y, z + 1)) {
            // Get the brightness of the tile next to it
            float brightness = level.getBrightness(x, y, z + 1) * shadeZ;

            // Don't render face if both conditions are the same (isShadowLayer != isFullBrightness)
            if (layer == 1 ^ brightness == shadeZ) {
                tessellator.color(brightness, brightness, brightness);
                tessellator.texture(minU, minV);
                tessellator.vertex(minX, maxY, maxZ);
                tessellator.texture(minU, maxV);
                tessellator.vertex(minX, minY, maxZ);
                tessellator.texture(maxU, maxV);
                tessellator.vertex(maxX, minY, maxZ);
                tessellator.texture(maxU, minV);
                tessellator.vertex(maxX, maxY, maxZ);
            }
        }

        // Render side faces X
        if (!level.isSolidTile(x - 1, y, z)) {
            // Get the brightness of the tile next to it
            float brightness = level.getBrightness(x - 1, y, z) * shadeX;

            // Don't render face if both conditions are the same (isShadowLayer != isFullBrightness)
            if (layer == 1 ^ brightness == shadeX) {
                tessellator.color(brightness, brightness, brightness);
                tessellator.texture(maxU, minV);
                tessellator.vertex(minX, maxY, maxZ);
                tessellator.texture(minU, minV);
                tessellator.vertex(minX, maxY, minZ);
                tessellator.texture(minU, maxV);
                tessellator.vertex(minX, minY, minZ);
                tessellator.texture(maxU, maxV);
                tessellator.vertex(minX, minY, maxZ);
            }
        }
        if (!level.isSolidTile(x + 1, y, z)) {
            // Get the brightness of the tile next to it
            float brightness = level.getBrightness(x + 1, y, z) * shadeX;

            // Don't render face if both conditions are the same (isShadowLayer != isFullBrightness)
            if (layer == 1 ^ brightness == shadeX) {
                tessellator.color(brightness, brightness, brightness);
                tessellator.texture(minU, maxV);
                tessellator.vertex(maxX, minY, maxZ);
                tessellator.texture(maxU, maxV);
                tessellator.vertex(maxX, minY, minZ);
                tessellator.texture(maxU, minV);
                tessellator.vertex(maxX, maxY, minZ);
                tessellator.texture(minU, minV);
                tessellator.vertex(maxX, maxY, maxZ);
            }
        }
    }
}
