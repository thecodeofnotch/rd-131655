package com.mojang.rubydung;

import com.mojang.rubydung.level.Level;

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
}
