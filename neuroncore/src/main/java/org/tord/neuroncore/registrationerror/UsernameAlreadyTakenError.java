package org.tord.neuroncore.registrationerror;

import org.tord.neuroncore.R;

public class UsernameAlreadyTakenError extends RegistrationError {
    public UsernameAlreadyTakenError(int desc) {
        super(desc);
    }

    public UsernameAlreadyTakenError() {
        super();
        setDescription(R.string.error_username_already_taken);
    }
}
