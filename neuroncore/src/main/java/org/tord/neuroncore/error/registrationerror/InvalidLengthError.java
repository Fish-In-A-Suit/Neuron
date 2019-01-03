package org.tord.neuroncore.error.registrationerror;

import org.tord.neuroncore.R;

///throw always when there's invalid length
public class InvalidLengthError extends RegistrationError {
    public InvalidLengthError(int desc) {
        super(desc);
    }

    public InvalidLengthError() {
        super();
        setDescription(R.string.error_invalid_length);
    }
}
