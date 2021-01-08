package com.mojang.rubydung.level;

import com.mojang.rubydung.phys.AABB;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/*
 * Author: Ben Humphrey (DigiBen)
 * E-mail: digiben@gametutorials.com
 */
public class Frustum {

    // We create an enum of the sides so we don't have to call each side 0 or 1.
    // This way it makes it more understandable and readable when dealing with frustum sides.
    public static final int RIGHT = 0;            // The RIGHT side of the frustum
    public static final int LEFT = 1;            // The LEFT      side of the frustum
    public static final int BOTTOM = 2;            // The BOTTOM side of the frustum
    public static final int TOP = 3;                // The TOP side of the frustum
    public static final int BACK = 4;            // The BACK     side of the frustum
    public static final int FRONT = 5;            // The FRONT side of the frustum

    // Like above, instead of saying a number for the ABC and D of the plane, we
    // want to be more descriptive.
    public static final int A = 0;                          // The X value of the plane's normal
    public static final int B = 1;                          // The Y value of the plane's normal
    public static final int C = 2;                          // The Z value of the plane's normal
    public static final int D = 3;                          // The distance the plane is from the origin

    private static Frustum frustum = new Frustum();

    // This holds the A B C and D values for each side of our frustum.
    float[][] m_Frustum = new float[6][4];

    /**
     * FloatBuffer to get ModelView matrix.
     **/
    FloatBuffer modl_b;

    /**
     * FloatBuffer to get Projection matrix.
     **/
    FloatBuffer proj_b;


    /**
     * Frustum constructor.
     */
    public Frustum() {
        modl_b = BufferUtils.createFloatBuffer(16);
        proj_b = BufferUtils.createFloatBuffer(16);
    }

    /**
     * Calculate the frustum and return it
     *
     * @return Current frustum
     */
    public static Frustum getFrustum() {
        Frustum.frustum.calculateFrustum();
        return Frustum.frustum;
    }

    ///////////////////////////////// NORMALIZE PLANE \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    /////
    /////   This normalizes a plane (A side) from a given frustum.
    /////
    ///////////////////////////////// NORMALIZE PLANE \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    public void normalizePlane(float[][] frustum, int side) {
        // Here we calculate the magnitude of the normal to the plane (point A B C)
        // Remember that (A, B, C) is that same thing as the normal's (X, Y, Z).
        // To calculate magnitude you use the equation:  magnitude = sqrt( x^2 + y^2 + z^2)
        float magnitude = (float) Math.sqrt(frustum[side][A] * frustum[side][A] +
                frustum[side][B] * frustum[side][B] + frustum[side][C] * frustum[side][C]);

        // Then we divide the plane's values by it's magnitude.
        // This makes it easier to work with.
        frustum[side][A] /= magnitude;
        frustum[side][B] /= magnitude;
        frustum[side][C] /= magnitude;
        frustum[side][D] /= magnitude;
    }


    ///////////////////////////////// CALCULATE FRUSTUM \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    /////
    /////   This extracts our frustum from the projection and modelview matrix.
    /////
    ///////////////////////////////// CALCULATE FRUSTUM \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    public void calculateFrustum() {
        float[] proj = new float[16];           // This will hold our projection matrix
        float[] modl = new float[16];           // This will hold our modelview matrix
        float[] clip = new float[16];           // This will hold the clipping planes


        // glGetFloat() is used to extract information about our OpenGL world.
        // Below, we pass in GL_PROJECTION_MATRIX to abstract our projection matrix.
        // It then stores the matrix into an array of [16].
        proj_b.rewind();
        glGetFloat(GL_PROJECTION_MATRIX, proj_b);
        proj_b.rewind();
        proj_b.get(proj);

        // By passing in GL_MODELVIEW_MATRIX, we can abstract our model view matrix.
        // This also stores it in an array of [16].
        modl_b.rewind();
        glGetFloat(GL_MODELVIEW_MATRIX, modl_b);
        modl_b.rewind();
        modl_b.get(modl);

        // Now that we have our modelview and projection matrix, if we combine these 2 matrices,
        // it will give us our clipping planes.  To combine 2 matrices, we multiply them.

        clip[0] = modl[0] * proj[0] + modl[1] * proj[4] + modl[2] * proj[8] + modl[3] * proj[12];
        clip[1] = modl[0] * proj[1] + modl[1] * proj[5] + modl[2] * proj[9] + modl[3] * proj[13];
        clip[2] = modl[0] * proj[2] + modl[1] * proj[6] + modl[2] * proj[10] + modl[3] * proj[14];
        clip[3] = modl[0] * proj[3] + modl[1] * proj[7] + modl[2] * proj[11] + modl[3] * proj[15];

        clip[4] = modl[4] * proj[0] + modl[5] * proj[4] + modl[6] * proj[8] + modl[7] * proj[12];
        clip[5] = modl[4] * proj[1] + modl[5] * proj[5] + modl[6] * proj[9] + modl[7] * proj[13];
        clip[6] = modl[4] * proj[2] + modl[5] * proj[6] + modl[6] * proj[10] + modl[7] * proj[14];
        clip[7] = modl[4] * proj[3] + modl[5] * proj[7] + modl[6] * proj[11] + modl[7] * proj[15];

        clip[8] = modl[8] * proj[0] + modl[9] * proj[4] + modl[10] * proj[8] + modl[11] * proj[12];
        clip[9] = modl[8] * proj[1] + modl[9] * proj[5] + modl[10] * proj[9] + modl[11] * proj[13];
        clip[10] = modl[8] * proj[2] + modl[9] * proj[6] + modl[10] * proj[10] + modl[11] * proj[14];
        clip[11] = modl[8] * proj[3] + modl[9] * proj[7] + modl[10] * proj[11] + modl[11] * proj[15];

        clip[12] = modl[12] * proj[0] + modl[13] * proj[4] + modl[14] * proj[8] + modl[15] * proj[12];
        clip[13] = modl[12] * proj[1] + modl[13] * proj[5] + modl[14] * proj[9] + modl[15] * proj[13];
        clip[14] = modl[12] * proj[2] + modl[13] * proj[6] + modl[14] * proj[10] + modl[15] * proj[14];
        clip[15] = modl[12] * proj[3] + modl[13] * proj[7] + modl[14] * proj[11] + modl[15] * proj[15];

        // Now we actually want to get the sides of the frustum.  To do this we take
        // the clipping planes we received above and extract the sides from them.

        // This will extract the RIGHT side of the frustum
        m_Frustum[RIGHT][A] = clip[3] - clip[0];
        m_Frustum[RIGHT][B] = clip[7] - clip[4];
        m_Frustum[RIGHT][C] = clip[11] - clip[8];
        m_Frustum[RIGHT][D] = clip[15] - clip[12];

        // Now that we have a normal (A,B,C) and a distance (D) to the plane,
        // we want to normalize that normal and distance.

        // Normalize the RIGHT side
        normalizePlane(m_Frustum, RIGHT);

        // This will extract the LEFT side of the frustum
        m_Frustum[LEFT][A] = clip[3] + clip[0];
        m_Frustum[LEFT][B] = clip[7] + clip[4];
        m_Frustum[LEFT][C] = clip[11] + clip[8];
        m_Frustum[LEFT][D] = clip[15] + clip[12];

        // Normalize the LEFT side
        normalizePlane(m_Frustum, LEFT);

        // This will extract the BOTTOM side of the frustum
        m_Frustum[BOTTOM][A] = clip[3] + clip[1];
        m_Frustum[BOTTOM][B] = clip[7] + clip[5];
        m_Frustum[BOTTOM][C] = clip[11] + clip[9];
        m_Frustum[BOTTOM][D] = clip[15] + clip[13];

        // Normalize the BOTTOM side
        normalizePlane(m_Frustum, BOTTOM);

        // This will extract the TOP side of the frustum
        m_Frustum[TOP][A] = clip[3] - clip[1];
        m_Frustum[TOP][B] = clip[7] - clip[5];
        m_Frustum[TOP][C] = clip[11] - clip[9];
        m_Frustum[TOP][D] = clip[15] - clip[13];

        // Normalize the TOP side
        normalizePlane(m_Frustum, TOP);

        // This will extract the BACK side of the frustum
        m_Frustum[BACK][A] = clip[3] - clip[2];
        m_Frustum[BACK][B] = clip[7] - clip[6];
        m_Frustum[BACK][C] = clip[11] - clip[10];
        m_Frustum[BACK][D] = clip[15] - clip[14];

        // Normalize the BACK side
        normalizePlane(m_Frustum, BACK);

        // This will extract the FRONT side of the frustum
        m_Frustum[FRONT][A] = clip[3] + clip[2];
        m_Frustum[FRONT][B] = clip[7] + clip[6];
        m_Frustum[FRONT][C] = clip[11] + clip[10];
        m_Frustum[FRONT][D] = clip[15] + clip[14];

        // Normalize the FRONT side
        normalizePlane(m_Frustum, FRONT);
    }

    // The code below will allow us to make checks within the frustum.  For example,
    // if we want to see if a point, a sphere, or a cube lies inside of the frustum.
    // Because all of our planes point INWARDS (The normals are all pointing inside the frustum)
    // we then can assume that if a point is in FRONT of all of the planes, it's inside.

    ///////////////////////////////// POINT IN FRUSTUM \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    /////
    /////   This determines if a point is inside of the frustum
    /////
    ///////////////////////////////// POINT IN FRUSTUM \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    public boolean pointInFrustum(float x, float y, float z) {
        // Go through all the sides of the frustum
        for (int i = 0; i < 6; i++) {
            // Calculate the plane equation and check if the point is behind a side of the frustum
            if (m_Frustum[i][A] * x + m_Frustum[i][B] * y + m_Frustum[i][C] * z + m_Frustum[i][D] <= 0) {
                // The point was behind a side, so it ISN'T in the frustum
                return false;
            }
        }

        // The point was inside of the frustum (In front of ALL the sides of the frustum)
        return true;
    }


    ///////////////////////////////// SPHERE IN FRUSTUM \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    /////
    /////   This determines if a sphere is inside of our frustum by it's center and radius.
    /////
    ///////////////////////////////// SPHERE IN FRUSTUM \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    public boolean sphereInFrustum(float x, float y, float z, float radius) {
        // Go through all the sides of the frustum
        for (int i = 0; i < 6; i++) {
            // If the center of the sphere is farther away from the plane than the radius
            if (m_Frustum[i][A] * x + m_Frustum[i][B] * y + m_Frustum[i][C] * z + m_Frustum[i][D] <= -radius) {
                // The distance was greater than the radius so the sphere is outside of the frustum
                return false;
            }
        }

        // The sphere was inside of the frustum!
        return true;
    }

    ///////////////////////////////// CUBE IN FRUSTUM \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    /////
    /////   This determines if a cube is in or around our frustum by it's center and 1/2 it's length
    /////
    ///////////////////////////////// CUBE IN FRUSTUM \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
    public boolean cubeInFrustum(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        // This test is a bit more work, but not too much more complicated.
        // Basically, what is going on is, that we are given the center of the cube,
        // and half the length.  Think of it like a radius.  Then we checking each point
        // in the cube and seeing if it is inside the frustum.  If a point is found in front
        // of a side, then we skip to the next side.  If we get to a plane that does NOT have
        // a point in front of it, then it will return false.

        // *Note* - This will sometimes say that a cube is inside the frustum when it isn't.
        // This happens when all the corners of the bounding box are not behind any one plane.
        // This is rare and shouldn't effect the overall rendering speed.

        for (int i = 0; i < 6; i++) {
            if (m_Frustum[i][A] * minX + m_Frustum[i][B] * minY + m_Frustum[i][C] * minZ + m_Frustum[i][D] > 0)
                continue;
            if (m_Frustum[i][A] * maxX + m_Frustum[i][B] * minY + m_Frustum[i][C] * minZ + m_Frustum[i][D] > 0)
                continue;
            if (m_Frustum[i][A] * minX + m_Frustum[i][B] * maxY + m_Frustum[i][C] * minZ + m_Frustum[i][D] > 0)
                continue;
            if (m_Frustum[i][A] * maxX + m_Frustum[i][B] * maxY + m_Frustum[i][C] * minZ + m_Frustum[i][D] > 0)
                continue;
            if (m_Frustum[i][A] * minX + m_Frustum[i][B] * minY + m_Frustum[i][C] * maxZ + m_Frustum[i][D] > 0)
                continue;
            if (m_Frustum[i][A] * maxX + m_Frustum[i][B] * minY + m_Frustum[i][C] * maxZ + m_Frustum[i][D] > 0)
                continue;
            if (m_Frustum[i][A] * minX + m_Frustum[i][B] * maxY + m_Frustum[i][C] * maxZ + m_Frustum[i][D] > 0)
                continue;
            if (m_Frustum[i][A] * maxX + m_Frustum[i][B] * maxY + m_Frustum[i][C] * maxZ + m_Frustum[i][D] > 0)
                continue;

            // If we get here, it isn't in the frustum
            return false;
        }

        return true;
    }

    public boolean cubeInFrustum(AABB aabb) {
        return cubeInFrustum((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ,
                (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
    }
}