package org.tord.neuroncore.registrationerror;

import org.tord.neuroncore.R;

//throw if sex hasn't been specified
public class SexNotSpecifiedError extends RegistrationError {
    public SexNotSpecifiedError(int desc) {
        super(desc);
    }

    public SexNotSpecifiedError() {
        super();
        setDescription(R.string.error_sex_unspecified);
    }
}
