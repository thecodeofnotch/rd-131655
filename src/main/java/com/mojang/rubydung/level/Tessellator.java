package com.mojang.rubydung.level;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Tessellator {

    private static final int MAX_VERTICES = 100000;

    private final FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * 3);
    private final FloatBuffer textureCoordinateBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * 2);

    private int vertices = 0;

    private boolean hasTexture = false;
    private float textureU;
    private float textureV;

    /**
     * Reset the buffer
     */
    public void init() {
        clear();
    }

    /**
     * Add a vertex point to buffer
     *
     * @param x Vertex point x
     * @param y Vertex point y
     * @param z Vertex point z
     */
    public void vertex(float x, float y, float z) {
        // Vertex
        this.vertexBuffer.put(this.vertices * 3, x);
        this.vertexBuffer.put(this.vertices * 3 + 1, y);
        this.vertexBuffer.put(this.vertices * 3 + 2, z);

        // Texture coordinate
        if (this.hasTexture) {
            this.textureCoordinateBuffer.put(this.vertices * 2, this.textureU);
            this.textureCoordinateBuffer.put(this.vertices * 2 + 1, this.textureV);
        }

        this.vertices++;

        // Flush if there are too many vertices in the buffer
        if (this.vertices == MAX_VERTICES) {
            flush();
        }
    }

    public void texture(float textureU, float textureV) {
        this.hasTexture = true;
        this.textureU = textureU;
        this.textureV = textureV;
    }

    /**
     * Render the buffer
     */
    public void flush() {
        this.vertexBuffer.flip();
        this.textureCoordinateBuffer.flip();

        // Set points
        glVertexPointer(GL_LINE_STRIP, GL_POINTS, this.vertexBuffer);
        if (this.hasTexture) {
            GL11.glTexCoordPointer(2, 0, this.textureCoordinateBuffer);
        }

        // Enable vertex array state
        glEnableClientState(GL_VERTEX_ARRAY);
        if (this.hasTexture) {
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        }

        // Draw quads
        glDrawArrays(GL11.GL_QUADS, GL_POINTS, this.vertices);

        // Reset after rendering
        glDisableClientState(GL_VERTEX_ARRAY);
        if (this.hasTexture) {
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        }
        clear();
    }

    /**
     * Reset vertex buffer
     */
    private void clear() {
        this.vertexBuffer.clear();
        this.textureCoordinateBuffer.clear();
        this.vertices = 0;

        this.hasTexture = false;
    }
}