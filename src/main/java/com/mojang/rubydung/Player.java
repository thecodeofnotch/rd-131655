package com.mojang.rubydung;

import com.mojang.rubydung.level.Level;
import com.mojang.rubydung.phys.AABB;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Player {

    private final Level level;

    public double x, y, z;
    public double motionX, motionY, motionZ;
    public float xRotation, yRotation;

    private boolean onGround;

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
     * @param x Rotate the camera using yaw
     * @param y Rotate the camera using pitch
     */
    public void turn(float x, float y) {
        this.yRotation += x * 0.15F;
        this.xRotation -= y * 0.15F;

        // Pitch limit
        this.xRotation = Math.max(-90.0F, this.xRotation);
        this.xRotation = Math.min(90.0F, this.xRotation);
    }

    public void tick() {
        float forward = 0.0F;
        float vertical = 0.0F;

        // Reset the position of the player
        if (Keyboard.isKeyDown(19)) { // R
            resetPosition();
        }

        // Player movement
        if (Keyboard.isKeyDown(200) || Keyboard.isKeyDown(17)) { // Up, W
            forward--;
        }
        if (Keyboard.isKeyDown(208) || Keyboard.isKeyDown(31)) { // Down, S
            forward++;
        }
        if (Keyboard.isKeyDown(203) || Keyboard.isKeyDown(30)) { // Left, A
            vertical--;
        }
        if (Keyboard.isKeyDown(205) || Keyboard.isKeyDown(32)) {  // Right, D
            vertical++;
        }
        if ((Keyboard.isKeyDown(57) || Keyboard.isKeyDown(219))) { // Space, Windows Key
            if (this.onGround) {
                this.motionY = 0.12F;
            }
        }

        // Add motion to the player using keyboard input
        moveRelative(vertical, forward, this.onGround ? 0.02F : 0.005F);

        // Apply gravity motion
        this.motionY -= 0.005D;

        // Move the player using the motion
        move(this.motionX, this.motionY, this.motionZ);

        // Decrease motion
        this.motionX *= 0.91F;
        this.motionY *= 0.98F;
        this.motionZ *= 0.91F;

        // Decrease motion on ground
        if (this.onGround) {
            this.motionX *= 0.8F;
            this.motionZ *= 0.8F;
        }
    }

    /**
     * Move player relative in level with collision check
     *
     * @param x Relative x
     * @param y Relative y
     * @param z Relative z
     */
    public void move(double x, double y, double z) {
        double prevX = x;
        double prevY = y;
        double prevZ = z;

        // Get surrounded tiles
        List<AABB> aABBs = this.level.getCubes(this.boundingBox.expand(x, y, z));

        // Check for Y collision
        for (AABB abb : aABBs) {
            y = abb.clipYCollide(this.boundingBox, y);
        }
        this.boundingBox.move(0.0F, y, 0.0F);

        // Check for X collision
        for (AABB aABB : aABBs) {
            x = aABB.clipXCollide(this.boundingBox, x);
        }
        this.boundingBox.move(x, 0.0F, 0.0F);

        // Check for Z collision
        for (AABB aABB : aABBs) {
            z = aABB.clipZCollide(this.boundingBox, z);
        }
        this.boundingBox.move(0.0F, 0.0F, z);

        // Update on ground state
        this.onGround = prevY != y && prevY < 0.0F;

        // Stop motion on collision
        if (prevX != x) this.motionX = 0.0D;
        if (prevY != y) this.motionY = 0.0D;
        if (prevZ != z) this.motionZ = 0.0D;

        // Move the actual player position
        this.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
        this.y = this.boundingBox.minY + 1.62D;
        this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
    }


    /**
     * Add motion to the player in the facing direction with given speed
     *
     * @param x     Motion to add on X axis
     * @param z     Motion to add on Z axis
     * @param speed Strength of the added motion
     */
    private void moveRelative(float x, float z, float speed) {
        float distance = x * x + z * z;

        // Stop moving if too slow
        if (distance < 0.01F)
            return;

        // Apply speed to relative movement
        distance = speed / (float) Math.sqrt(distance);
        x *= distance;
        z *= distance;

        // Calculate sin and cos of player rotation
        double sin = Math.sin(Math.toRadians(this.yRotation));
        double cos = Math.cos(Math.toRadians(this.yRotation));

        // Move the player in facing direction
        this.motionX += x * cos - z * sin;
        this.motionZ += z * cos + x * sin;
    }
}
