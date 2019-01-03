package org.multiverse.multiversetools;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

public class AnimationTools {
    static int numCallbacksThreshold = 0;
    /**
     * This method starts targetActivity from activityContext when the animation specified by target is finished
     * @param target The animation the end of which to register as a callback
     * @param activityContext The context of the current activity
     * @param targetActivity The class of the activity to start
     */
    public static void setAVDOnEndCallbackLauncher(AnimatedVectorDrawable target, final Context activityContext, final Class targetActivity) {
        int i = numCallbacksThreshold++; //to prevent android from setting multiple onEndCallbacks by consecutively calling this method ... bug?

        if(Build.VERSION.SDK_INT >= 23 && i < 1) {
            target.registerAnimationCallback(new Animatable2.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    GeneralTools.launchNewActivity(activityContext, targetActivity);
                }
            });
        }

    }
}
