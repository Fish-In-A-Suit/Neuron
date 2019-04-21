package org.tord.neuroncore.registrationerror;

import android.widget.TextView;

/**
 * Deals with displaying errors.
 */
public class RegistrationErrorDisplay {

    /**
     * Displays the description of the specified error in the specified text view.
     * @param re
     * @param textView
     */
    public static void displayError(RegistrationError re, TextView textView) {
        textView.setText(re.getDescription());
    }
}
