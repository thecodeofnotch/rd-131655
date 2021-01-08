package com.mojang.rubydung;

public class Timer {

    private static final long NS_PER_SECOND = 1000000000L;
    private static final long MAX_NS_PER_UPDATE = 1000000000L;
    private static final int MAX_TICKS_PER_UPDATE = 100;

    /**
     * Amount of ticks per second
     */
    private final float ticksPerSecond;

    /**
     * Last time updated in nano seconds
     */
    private long lastTime = System.nanoTime();

    /**
     * Scale the tick speed
     */
    public float timeScale = 1.0F;

    /**
     * Framerate of the advanceTime update
     */
    public float fps = 0.0F;

    /**
     * Passed time since last game update
     */
    public float passedTime = 0.0F;

    /**
     * The amount of ticks for the current game update.
     * It's the passed time as an integer
     */
    public int ticks;

    /**
     * The overflow of the current tick, caused by casting the passed time to an integer
     */
    public float partialTicks;

    /**
     * Timer to control the tick speed independently of the framerate
     *
     * @param ticksPerSecond Amount of ticks per second
     */
    public Timer(float ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
    }

    /**
     * This function calculates the amount of ticks required to reach the ticksPerSecond.
     * Call this function in the main render loop of the game
     */
    public void advanceTime() {
        long now = System.nanoTime();
        long passedNs = now - this.lastTime;

        // Store nano time of this update
        this.lastTime = now;

        // Maximum and minimum
        passedNs = Math.max(0, passedNs);
        passedNs = Math.min(MAX_NS_PER_UPDATE, passedNs);

        // Calculate fps
        this.fps = (float) (NS_PER_SECOND / passedNs);

        // Calculate passed time and ticks
        this.passedTime += passedNs * this.timeScale * this.ticksPerSecond / NS_PER_SECOND;
        this.ticks = (int) this.passedTime;

        // Maximum ticks per update
        this.ticks = Math.min(MAX_TICKS_PER_UPDATE, this.ticks);

        // Calculate the overflow of the current tick
        this.passedTime -= this.ticks;
        this.partialTicks = this.passedTime;
    }
}
