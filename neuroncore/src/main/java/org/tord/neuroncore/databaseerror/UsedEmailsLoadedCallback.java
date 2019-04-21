package org.tord.neuroncore.databaseerror;

import java.util.ArrayList;

/**
 * Used when querying used emails from the database using DatabaseNetworking.getUsedEmails(...)
 */
public interface UsedEmailsLoadedCallback {
    void onCallback(ArrayList<String> usedEmails);
}
