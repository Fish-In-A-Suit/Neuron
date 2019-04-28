package org.multiverse.database;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.registration.firstTimeGoogleSignup.AfterGoogleSignUpManager;
import org.multiverse.multiversetools.valuekeepers.StringKeeper;
import org.multiverse.multiversetools.valuekeepers.BooleanKeeper;
import org.multiverse.registration.firstTimeGoogleSignup.GoogleSignUpAccountManager;
import org.multiverse.utilities.ArrayUtilities;
import org.tord.neuroncore.Constants;
import org.tord.neuroncore.databaseerror.EmailAlreadyInUseException;
import org.tord.neuroncore.databaseerror.UsedEmailsLoadedCallback;
import org.tord.neuroncore.databaseerror.UsedUsernamesCallback;
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
                System.out.println("[Neuron.database.DatabaseNetworking.getUsedUsernames]: Fetching used emails from the database");

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

    private static void getUsedUsernames(final UsedUsernamesCallback usedUsernamesCallback) {
        final ArrayList<String> usedUsernames = new ArrayList<>();

        getDatabaseReference().child(Constants.DATABASE_USERNAMES_USED_LOCATION).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("[Neuron.database.DatabaseNetworking.getUsedUsernames]: Fetching used usernames from the database");
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot currentDataSnapshot : children) {
                    usedUsernames.add(currentDataSnapshot.getValue().toString());
                }

                usedUsernamesCallback.onCallback(usedUsernames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Checks for possible email collisions and sends the specified DatabaseUser to the database (and updates the ui) if there are no email clashes. This should be the preferred way of sending users to the database!
     * This method is used when the user chooses to sign up using Google.
     * @param dbUser
     */
    //todo: stop current activity
    public static void checkForEmailClashesAndSendToDatabase(final DatabaseUser dbUser, final AfterGoogleSignUpManager agsm) {
        System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Checking for email clashes and trying to send user data for " + dbUser.getFullName() + " to the database.");
        final StringKeeper userEmail = new StringKeeper(dbUser.getEmail());
        final BooleanKeeper noEmailClashes = new BooleanKeeper(true);

        final StringKeeper usedUsername = new StringKeeper(agsm.getUsername());
        final BooleanKeeper noUsernameClashes = new BooleanKeeper(true);

        /*
        The below method is a callback. It executes asynchronously. Therefore, the if statements below this inner class execute before the methods within this inner class (which then always finds no email
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

                DatabaseNetworking.getUsedUsernames(new UsedUsernamesCallback() {
                    @Override
                    public void onCallback(ArrayList<String> usedUsernames) {
                        System.out.println("[Neuron.database.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Searching for username clashes");
                        ArrayUtilities.displayArray(usedUsernames);

                        for(String username : usedUsernames) {
                            if(usedUsername.getValue().equals(username)) {
                                System.out.println("[Neuron.database.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: A username clash has been found!");
                                noUsernameClashes.set(false);
                                break;
                            } else {
                                System.out.println("[Neuron.database.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Username " + usedUsername.getValue() + " doesn't clash with " + username);
                            }
                        }

                        if(noEmailClashes.get() && noUsernameClashes.get()) {
                            System.out.println("[Neuron.database.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: No email or username clashes. Sending user data to the server.");
                            agsm.buildUser(dbUser.getFullName(), dbUser.getEmail()).sendDataToDatabase();
                            agsm.updateUI();
                        } else {

                            if(GoogleSignUpAccountManager.getCurrentUser().isConnected()) {
                                //this call makes it so that the currently used account by the GoogleSignInApi is forgotten, so that other google accounts are displayed on the next button click. Without this call, the previous account would be used, therefore disabling the user to select an another account
                                System.out.println("[Neuron.database.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Signing out the user from the GoogleSignInApi (allowing the user to specify another account upon button click).");
                                Auth.GoogleSignInApi.signOut(GoogleSignUpAccountManager.getCurrentUser());
                            }


                            GoogleSignUpAccountManager.disconnectAndFreeMemory();

                            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                            if(null != fbUser) {
                                System.out.println("[Neuron.database.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Deleting user from firebase.");
                                fbUser.delete();
                            }

                            if(noEmailClashes.get() == false) {
                                System.out.println("[Neuron.database.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Email clash has occurred!");
                                agsm.notifyAboutEmailAlreadyInUse();
                                agsm.getLoginActivity().enableGoogleButton(true);
                            }

                            if(noUsernameClashes.get() == false) {
                                System.out.println("[Neuron.database.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Username clash has occurred!");
                                agsm.notifyAboutUsernameAlreadyInUse();
                            }
                        }
                    }
                });

            }
        });
    }

    /**
     * Checks for possible email collisions and sends the specified DatabaseUser to the database (and updates the ui - switches it to the activity specified by targetActivityClass from the current context) if there are no email clashes. This should be the preferred way of sending users to the database!
     * This method is used when using RegisterActivity to sign up.
     *
     * @param registeredUser
     */
    //todo: stop current activity
    public static void checkForEmailClashesAndSendToDatabase(final RegisteredUser registeredUser, final Context currentContext, final Class targetActivityClass) {
        System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Checking for email clashes and trying to send user data for " + registeredUser.getFullName() + " to the database.");
        final StringKeeper userEmail = new StringKeeper(registeredUser.getEmail());
        final BooleanKeeper noEmailClashes = new BooleanKeeper(true);

        DatabaseNetworking.getUsedEmails(new UsedEmailsLoadedCallback() {
            @Override
            public void onCallback(ArrayList<String> usedEmails) {
                for (String email : usedEmails) {
                    if(userEmail.getValue().equals(email)) {
                        System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: Clashing emails for email " + email + " found!");
                        noEmailClashes.set(false);
                        break;
                    } else {
                        System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: No clashes for email " + email + " have been found.");
                    }
                }

                if (noEmailClashes.get() == true) {
                    System.out.println("[Neuron.NC.networking.DatabaseNetworking.checkForEmailClashesAndSendToDatabase]: No email clashes. Sending user data to the server.");
                    registeredUser.buildDatabaseUser().sendDataToDatabase();
                    GeneralTools.launchNewActivity(currentContext, targetActivityClass);
                }
            }
        });
    }
}
