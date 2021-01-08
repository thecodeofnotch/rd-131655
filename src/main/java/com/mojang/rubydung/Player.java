package com.mojang.rubydung;

import com.mojang.rubydung.level.Level;
import org.lwjgl.input.Keyboard;

public class Player {

    private final Level level;

    public float x, y, z;
    public float xRotation, yRotation;

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
        this.x = x;
        this.y = y;
        this.z = z;
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
            this.y++;
        }

        moveRelative(motionX, motionZ, 0.02F);
    }

    private void moveRelative(float motionX, float motionZ, float speed) {
        // Calculate sin and cos of player rotation
        float sin = (float) Math.sin(Math.toRadians(this.yRotation));
        float cos = (float) Math.cos(Math.toRadians(this.yRotation));

        // Move the player in facing direction
        this.x += motionX * cos - motionZ * sin;
        this.z += motionZ * cos + motionX * sin;
    }
}
