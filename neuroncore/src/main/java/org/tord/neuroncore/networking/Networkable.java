package org.tord.neuroncore.networking;

import org.tord.neuroncore.databaseerror.EmailAlreadyInUseException;

public interface Networkable {
    void sendDataToDatabase() throws EmailAlreadyInUseException;
}
