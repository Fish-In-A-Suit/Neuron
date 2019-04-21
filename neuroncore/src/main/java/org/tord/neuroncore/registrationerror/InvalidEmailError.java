package org.tord.neuroncore.registrationerror;

import org.tord.neuroncore.R;

//throw if email doesn't contain @
public class InvalidEmailError  extends RegistrationError {
    public InvalidEmailError(int desc) {
        super(desc);
    }

    public InvalidEmailError() {
        super();
        setDescription(R.string.error_invalid_email);
    }
}
