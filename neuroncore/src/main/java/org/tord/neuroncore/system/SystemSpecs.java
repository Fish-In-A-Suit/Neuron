package org.tord.neuroncore.system;

/**
 * Contains the specification of the device that is running the app
 */
public class SystemSpecs {
    private static Screen screen;

    public static void setScreen(Screen screen1) {
        screen = screen1;
    }

    public static Screen getScreen() {
        return screen;
    }
}
