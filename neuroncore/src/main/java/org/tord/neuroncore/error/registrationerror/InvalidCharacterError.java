package org.tord.neuroncore.error.registrationerror;

import org.tord.neuroncore.R;

//throw always when there's an invalid character
public class InvalidCharacterError extends RegistrationError {
    public InvalidCharacterError(int desc) {
        super(desc);
    }

    public InvalidCharacterError() {
        super();
        setDescription(R.string.error_invalid_character);
    }
}
