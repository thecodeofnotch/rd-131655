package com.mojang.rubydung;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class RubyDung implements Runnable {

    /**
     * Initialize the game.
     * Setup display, keyboard, mouse, rendering, projection and perspective
     *
     * @throws LWJGLException Game could not be initialized
     */
    public void init() throws LWJGLException {
        int width = 1024;
        int height = 768;

        // Set screen size
        Display.setDisplayMode(new DisplayMode(width, height));

        // Setup I/O
        Display.create();
        Keyboard.create();
        Mouse.create();

        // Setup rendering
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glClearColor(0.5F, 0.8F, 1.0F, 0.0F);
        glClearDepth(1.0);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        // Setup projection
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        // Setup perspective
        gluPerspective(70, width / (float) height, 0.05F, 1000);
        glMatrixMode(GL_MODELVIEW);

        Mouse.setGrabbed(true);
    }

    /**
     * Destroy mouse, keyboard and display
     */
    public void destroy() {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    /**
     * Main game thread
     * Responsible for the game loop
     */
    @Override
    public void run() {
        try {
            // Initialize the game
            init();
        } catch (Exception e) {
            // Show error message dialog and stop the game
            JOptionPane.showMessageDialog(null, e, "Failed to start RubyDung", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        try {
            // Start the game loop
            while (!Keyboard.isKeyDown(1) && !Display.isCloseRequested()) {
                // Framerate limit
                Thread.sleep(5);

                // Update the display
                Display.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Destroy I/O and save game
            destroy();
        }
    }

    /**
     * Entry point of the game
     * @param args Program arguments (unused)
     */
    public static void main(String[] args) {
        new Thread(new RubyDung()).start();
    }
}
