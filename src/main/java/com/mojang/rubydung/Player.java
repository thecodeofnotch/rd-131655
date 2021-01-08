package com.mojang.rubydung;

import com.mojang.rubydung.level.Level;
import com.mojang.rubydung.phys.AABB;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Player {

    private final Level level;

    public double x, y, z;
    public float xRotation, yRotation;

    public AABB boundingBox;

    /**
     * The player that is controlling the camera of the game
     *
     * @param level Level of the player
     */
    public Player(Level level) {
        this.level = level;

        resetPosition();
    }

    /**
     * Set the player to a specific location
     *
     * @param x Position x
     * @param y Position y
     * @param z Position z
     */
    private void setPosition(float x, float y, float z) {
        // Set the position of the player
        this.x = x;
        this.y = y;
        this.z = z;

        // Player size
        float width = 0.3F;
        float height = 0.9F;

        // Set the position of the bounding box
        this.boundingBox = new AABB(x - width, y - height,
                z - width, x + width,
                y + height, z + width);
    }

    /**
     * Reset the position of the player to a random location on the level
     */
    private void resetPosition() {
        float x = (float) Math.random() * this.level.width;
        float y = (float) (this.level.depth + 3);
        float z = (float) Math.random() * this.level.height;

        setPosition(x, y, z);
    }

    /**
     * Turn the camera using motion yaw and pitch
     *
     * @param motionX Rotate the camera using yaw
     * @param motionY Rotate the camera using pitch
     */
    public void turn(float motionX, float motionY) {
        this.yRotation += motionX * 0.15F;
        this.xRotation -= motionY * 0.15F;
    }

    public void tick() {
        float motionX = 0.0F;
        float motionZ = 0.0F;

        // Reset the position of the player
        if (Keyboard.isKeyDown(19)) { // R
            resetPosition();
        }

        // Player movement
        if (Keyboard.isKeyDown(200) || Keyboard.isKeyDown(17)) { // Up, W
            motionZ--;
        }
        if (Keyboard.isKeyDown(208) || Keyboard.isKeyDown(31)) { // Down, S
            motionZ++;
        }
        if (Keyboard.isKeyDown(203) || Keyboard.isKeyDown(30)) { // Left, A
            motionX--;
        }
        if (Keyboard.isKeyDown(205) || Keyboard.isKeyDown(32)) {  // Right, D
            motionX++;
        }
        if ((Keyboard.isKeyDown(57) || Keyboard.isKeyDown(219))) { // Space, Windows Key
            move(0, 1, 0);
        }

        moveRelative(motionX, motionZ, 0.02F);

        move(0, -0.5F, 0);
    }

    /**
     * Move player relative in level with collision check
     *
     * @param x Relative x
     * @param y Relative y
     * @param z Relative z
     */
    public void move(double x, double y, double z) {
        // Get surrounded tiles
        List<AABB> aABBs = this.level.getCubes(this.boundingBox.expand(x, y, z));

        // Check for Y collision
        for (AABB abb : aABBs) {
            y = (float) abb.clipYCollide(this.boundingBox, y);
        }
        this.boundingBox.move(0.0F, y, 0.0F);

        // Check for X collision
        for (AABB aABB : aABBs) {
            x = (float) aABB.clipXCollide(this.boundingBox, x);
        }
        this.boundingBox.move(x, 0.0F, 0.0F);

        // Check for Z collision
        for (AABB aABB : aABBs) {
            z = (float) aABB.clipZCollide(this.boundingBox, z);
        }
        this.boundingBox.move(0.0F, 0.0F, z);

        // Move the actual player position
        this.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0F;
        this.y = this.boundingBox.minY + 1.62F;
        this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0F;
    }


    /**
     * Move player relative in facing direction with given speed
     *
     * @param x     Relative movement on X axis
     * @param z     Relative movement on Z axis
     * @param speed Speed of the player
     */
    private void moveRelative(float x, float z, float speed) {
        // Calculate sin and cos of player rotation
        float sin = (float) Math.sin(Math.toRadians(this.yRotation));
        float cos = (float) Math.cos(Math.toRadians(this.yRotation));

        // Move the player in facing direction
        double mX = x * cos - z * sin;
        double mZ = z * cos + x * sin;

        move(mX, 0, mZ);
    }
}
