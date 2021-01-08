package com.mojang.rubydung.level;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Tessellator {

    private static final int MAX_VERTICES = 100000;

    private final FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * 3);
    private final FloatBuffer textureCoordinateBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * 2);
    private final FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * 3);

    private int vertices = 0;

    // Texture
    private boolean hasTexture = false;
    private float textureU;
    private float textureV;

    // Color
    private boolean hasColor;
    private float red;
    private float green;
    private float blue;

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

        // Color coordinate
        if (this.hasColor) {
            this.colorBuffer.put(this.vertices * 3, this.red);
            this.colorBuffer.put(this.vertices * 3 + 1, this.green);
            this.colorBuffer.put(this.vertices * 3 + 2, this.blue);
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

    public void color(float red, float green, float blue) {
        this.hasColor = true;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Render the buffer
     */
    public void flush() {
        this.vertexBuffer.flip();
        this.textureCoordinateBuffer.flip();

        // Set points
        glVertexPointer(3, GL_POINTS, this.vertexBuffer);
        if (this.hasTexture) {
            glTexCoordPointer(2, GL_POINTS, this.textureCoordinateBuffer);
        }
        if (this.hasColor) {
            glColorPointer(3, GL_POINTS, this.colorBuffer);
        }

        // Enable client states
        glEnableClientState(GL_VERTEX_ARRAY);
        if (this.hasTexture) {
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        }
        if (this.hasColor) {
            glEnableClientState(GL_COLOR_ARRAY);
        }

        // Draw quads
        glDrawArrays(GL_QUADS, GL_POINTS, this.vertices);

        // Reset after rendering
        glDisableClientState(GL_VERTEX_ARRAY);
        if (this.hasTexture) {
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        }
        if (this.hasColor) {
            glDisableClientState(GL_COLOR_ARRAY);
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
        this.hasColor = false;
    }
}