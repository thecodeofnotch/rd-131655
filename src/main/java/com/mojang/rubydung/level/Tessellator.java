package com.mojang.rubydung.level;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Tessellator {

    private final FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(300000);

    private int vertices = 0;

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

        this.vertices++;
    }

    /**
     * Render the buffer
     */
    public void flush() {
        this.vertexBuffer.flip();

        glVertexPointer(GL_LINE_STRIP, GL_POINTS, this.vertexBuffer);
        glEnableClientState(GL_VERTEX_ARRAY);

        glDrawArrays(GL11.GL_QUADS, GL_POINTS, this.vertices);
        glDisableClientState(GL_VERTEX_ARRAY);

        // Reset after rendering
        clear();
    }

    /**
     * Reset vertex buffer
     */
    private void clear() {
        this.vertexBuffer.clear();
        this.vertices = 0;
    }
}