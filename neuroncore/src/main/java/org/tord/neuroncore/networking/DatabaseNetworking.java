package org.tord.neuroncore.networking;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.tord.neuroncore.Constants;
import org.tord.neuroncore.database.DatabaseUser;
import org.tord.neuroncore.registration.RegisteredUser;

import stringUtilities.StringUtilities;

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

    /**
     * Adds a new user to the user_data node, under child node which is the user's first name
     * @param user
     */
    public static void writeUser(RegisteredUser user) {
        DatabaseNetworking.getDatabaseReference().child(Constants.DATABASE_USER_DATA_LOCATION).child(user.getUsername()).setValue(new DatabaseUser(user));
    }
}
