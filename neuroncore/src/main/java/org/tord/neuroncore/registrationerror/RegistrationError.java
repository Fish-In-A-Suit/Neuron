package org.tord.neuroncore.registrationerror;

import android.widget.TextView;

import org.tord.neuroncore.R;

/**
 * The base class for all registration-related errors. Note that the description is passed in as an integer, which should point to a string value in strings.xml
 */
public class RegistrationError {
    int description_location;

    public RegistrationError(int desc) {
        description_location = desc;
    }

    public RegistrationError() {

    }

    public int getDescription() {
        return description_location;
    }

    public void setDescription(int desc) {
        description_location = desc;
    }
}
