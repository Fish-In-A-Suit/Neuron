package org.tord.neuroncore.networking;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseNetworking {

    private static DatabaseReference databaseReference;

    //shouldn't be instantiated
    private DatabaseNetworking() {

    }

    /**
     * It is obligatory to call this method for this class to work.
     */
    public static void setupDatabaseNetworking() {
        System.out.println("[Neuron.NC.networking.DatabaseNetworking]: Establishing database references.");
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * @return The current root database reference of firebase
     */
    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    /**
     * Adds the specified data to the database. The databaseRef might be modified further inside the custom implementation of sendDataToDatabase of the specified Networkable instancee
     * @param data
     */
    public static void addNewData(Networkable data) {
        System.out.println("[Neuron.NC.networking.DatabaseNetworking]: Starting to send data to the database.");
        data.sendDataToDatabase();
    }
}
