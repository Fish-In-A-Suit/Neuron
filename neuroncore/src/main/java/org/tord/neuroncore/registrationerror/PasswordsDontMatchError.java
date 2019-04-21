package org.tord.neuroncore.registrationerror;

import org.tord.neuroncore.R;

public class PasswordsDontMatchError extends RegistrationError {
    public PasswordsDontMatchError(int desc) {
        super(desc);
    }

    public PasswordsDontMatchError() {
        super();
        setDescription(R.string.password_dont_match);
    }
}
