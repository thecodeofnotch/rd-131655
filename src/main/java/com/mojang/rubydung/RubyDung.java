package com.mojang.rubydung;

import org.lwjgl.LWJGLException;

public class RubyDung implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello World!");
    }

    public static void main(String[] args) throws LWJGLException {
        new Thread(new RubyDung()).start();
    }
}
