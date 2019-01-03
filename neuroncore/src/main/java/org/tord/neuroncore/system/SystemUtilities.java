package org.tord.neuroncore.system;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Contains system-related functionality, such as querying system-related information
 */
public class SystemUtilities {
    /**
     * This method should be called at the very start of the application, so all the system-related classes are updated with the specifications
     * of the system that is running the application.
     */
    public static void getSystemSpecifications(Activity activity) {
        updateDisplaySize(activity);
    }

    private static void updateDisplaySize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        System.out.println("[Neuron.NC.system.SystemUtilities]: Screen width = " + width + ", screen height = " + height);
        SystemSpecs.setScreen(new Screen(width, height));
    }
}
