package org.tord.neuroncore.system;

/**
 * Represents the screen of the device
 */
public class Screen {
    private int width;
    private int height;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
