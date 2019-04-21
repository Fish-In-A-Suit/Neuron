package org.multiverse.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.multiverse.registration.AfterGoogleSignUpManager;
import org.multiverse.multiversetools.valuekeepers.StringKeeper;
import org.multiverse.multiversetools.alternative.BooleanKeeper;
import org.tord.neuroncore.Constants;
import org.tord.neuroncore.databaseerror.EmailAlreadyInUseException;
import org.tord.neuroncore.databaseerror.UsedEmailsLoadedCallback;
import org.tord.neuroncore.networking.Networkable;
import org.multiverse.registration.RegisteredUser;

import java.util.ArrayList;

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
        try {
            data.sendDataToDatabase();
        } catch (EmailAlreadyInUseException e) {
            //todo: HANDLE EMAILALREADYINUSE EXCEPTION!
        }
    }

    /**
     * Adds a new user to the user_data node, under child node which is the user's first name
     * @param user
     */
    public static void writeUser(RegisteredUser user) {
        DatabaseNetworking.getDatabaseReference().child(Constants.DATABASE_USER_DATA_LOCATION).child(user.getUsername()).setValue(new DatabaseUser(user));
    }

    private static void getUsedEmails(final UsedEmailsLoadedCallback usedEmailsLoadedCallback){
        final ArrayList<String> usedEmails = new ArrayList<>();

        getDatabaseReference().child(Constants.DATABASE_EMAILS_USED_LOCATION).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                System.out.println("[Neuron.NC.networking.DatabaseNetworking.getUsedEmails]: Amount of children of the dataSnapshot: " + dataSnapshot.getChildrenCount());

                for(DataSnapshot currentDataSnapshot : children) {
                    usedEmails.add(currentDataSnapshot.getValue().toString());
                }
                usedEmailsLoadedCallback.onCallback(usedEmails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Checks for possible email collisions and sends the specified DatabaseUser to the database (and updates the ui) if there are no email clashes. This should be the preferred way of sending users to the database!
     * @param du
     */
    public static void checkForEmailClashesAndSendToDatabase(final DatabaseUser du, final AfterGoogleSignUpManager agsm) {
        System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Checking for email clashes and trying to send user data for " + du.getFullName() + " to the database.");
        final StringKeeper userEmail = new StringKeeper(du.getEmail());
        final BooleanKeeper noEmailClashes = new BooleanKeeper(true);

        /*
        PROBLEM: The below method is a callback. It executes asynchronously. Therefore, the if statements below this inner class execute before the methods within this inner class (which then always finds no email
        clashes). Therefore, the if statements have to be processes withing the inner class. But, then again, no callback for the method can be thrown from the inner class.
         */
        DatabaseNetworking.getUsedEmails(new UsedEmailsLoadedCallback() {
            @Override
            public void onCallback(ArrayList<String> usedEmails) {
                for(String email : usedEmails) {
                    if(userEmail.getValue().equals(email)) {
                        System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Clashing emails for email " + email + " found!");
                        noEmailClashes.set(false);
                        break;
                    } else {
                        System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: No clashes for email " + email + " have been found.");
                    }
                }

                if(noEmailClashes.get() == true) { //if there are no email clashes, send the user to the database
                    System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: No email clashes. Sending user data to the server.");
                    du.sendDataToDatabase();

                    agsm.updateUI(); //changes the ui, ie starts the main activity
                } else {
                    System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Email clash has been found!");

                    //todo: START FROM HERE! NOTIFY THE USER PROPERLY IF THE SPECIFIED EMAIL IS ALREADY IN USE!
                    agsm.notifyAboutEmailAlreadyInUse();
                }
            }
        });


    }
}
