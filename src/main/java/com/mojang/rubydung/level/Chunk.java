package com.mojang.rubydung.level;

import com.mojang.rubydung.Textures;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;


public class Chunk {

    private static final int TEXTURE = Textures.loadTexture("/terrain.png", GL_NEAREST);
    private static final Tessellator TESSELLATOR = new Tessellator();

    private final Level level;

    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;

    private final int lists;
    private boolean dirty = true;

    /**
     * Chunk containing a part of the tiles in a level
     *
     * @param level The game level
     * @param minX  Minimal location X
     * @param minY  Minimal location Y
     * @param minZ  Minimal location Z
     * @param maxX  Maximal location X
     * @param maxY  Maximal location Y
     * @param maxZ  Maximal location Z
     */
    public Chunk(Level level, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.level = level;

        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;

        // Generate lists id
        this.lists = glGenLists(2);
    }

    /**
     * Render all tiles in this chunk
     *
     * @param layer The layer of the chunk (For shadows)
     */
    public void rebuild(int layer) {
        this.dirty = false;

        // Setup tile rendering
        glNewList(this.lists + layer, GL_COMPILE);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE, TEXTURE);
        TESSELLATOR.init();

        // For each tile in this chunk
        for (int x = this.minX; x < this.maxX; ++x) {
            for (int y = this.minY; y < this.maxY; ++y) {
                for (int z = this.minZ; z < this.maxZ; ++z) {
                    // Is a tile at this location?
                    if (this.level.isTile(x, y, z)) {

                        // Grass is only on the first 7 tiles if the brightness is on maximum
                        if (y > this.level.depth - 7 && this.level.getBrightness(x, y, z) == 1.0F) {
                            // Render the tile
                            Tile.grass.render(TESSELLATOR, this.level, layer, x, y, z);
                        } else {
                            // Render the tile
                            Tile.rock.render(TESSELLATOR, this.level, layer, x, y, z);
                        }
                    }
                }
            }
        }

        // Finish tile rendering
        TESSELLATOR.flush();
        glDisable(GL_TEXTURE_2D);
        glEndList();
    }

    /**
     * Render all tiles in this chunk
     *
     * @param layer The render layer (Shadow layer)
     */
    public void render(int layer) {
        // Rebuild chunk if dirty
        if (this.dirty) {
            rebuild(0);
            rebuild(1);
        }

        // Call lists id to render the chunk
        glCallList(this.lists + layer);
    }

    /**
     * Mark chunk as dirty. The chunk will rebuild in the next frame
     */
    public void setDirty() {
        this.dirty = true;
    }
}
