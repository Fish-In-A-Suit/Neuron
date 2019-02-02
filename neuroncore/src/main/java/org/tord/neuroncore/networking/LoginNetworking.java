package org.tord.neuroncore.networking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.v4.app.FragmentActivity;

import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.multiversetools.ViewTab;
import org.tord.neuroncore.Constants;
import org.tord.neuroncore.R;
import org.tord.neuroncore.database.DatabaseUser;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import core.Switcher;

public class LoginNetworking {
    private static GoogleApiClient googleApiClient;
    private static int RC_SIGN_IN = 100; //can be any number

    /**
     * Attempts to sign in a user with the specified email and password. If the sign in is complete, activity specified by targetActivityClass is launched.
     * @param callingActivity The activity from which this method is called
     * @param fbAuth Instance of FirebaseAuth
     * @param email
     * @param password
     * @return True if sign in process is successful or false if it has failed.
     */
    public static void signUserIn(final Activity callingActivity, final Class targetActivityClass, FirebaseAuth fbAuth, final String email, final String password) {
        if(null != email && null != password && !email.equals("") && !password.equals("")) {
            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callingActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        System.out.println("[Neuron.NC.networking.LoginNetworking.signUserIn]: Sign in process for email '" + email + "' and password '" + password + "' is successful.");
                        GeneralTools.launchNewActivity(callingActivity.getBaseContext(), targetActivityClass);
                    } else {
                        System.out.println("[Neuron.NC.networking.LoginNetworking.signUserIn]: Sign in process for email '" + email + "' and password '" + password + "' FAILED.");
                        Toast.makeText(callingActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        //todo: better inform the user about what went wrong
                    }
                }
            });
        } else {
            System.out.println("[Neuron.NC.networking.LoginNetworking.signUserIn]: Can't sign the user in! Email or password isn't specified!");
            //todo: notify the user that he has to specify email or password... return an ErrorSet instance which contains all of the errors thrown to the calling activity???
        }
    }

    /**
     * Starts the google-sign-in process by calling setActivityForResult on the specified activity, which results in the method onActivityResult being called,
     * which in turn calls determineSignInSuccess. If the user isn't added to the firebase server (ie hasn't registered), these two methods also create a new user
     * on the servers.
     * @param activityContext
     * @param activity
     */
    public static void signUserInWithGoogle(Context activityContext, FragmentActivity activity) {
        System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Starting the process of google-sign-in.");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activityContext.getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(activityContext).enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Failed to connect to Google services!");
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Determines the success of the sign in. Should be called inside onActivityResult of the activity which contains the functionality to sign the user in using google.
     * If the sign in is successful, firebaseAuthWithGoogle is called, which tries to authenticate the user to the firebase server (and if the authentication is successful,
     * launch activity specified by targetActivityClass).
     * @param requestCode
     * @param data
     * @param sourceActivity
     * @param targetActivityClass
     */
    public static void determineSignInSuccess(int requestCode, Intent data, Activity sourceActivity, Class targetActivityClass) {
        //check which request we're responding to
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()) {
                System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Google sign in result is successful");
                GoogleSignInAccount account = result.getSignInAccount();

                //to use
                String accountName = account.getDisplayName();
                String accountEmail = account.getEmail();
                String accountId = account.getId();
                Uri accountPhoto = account.getPhotoUrl();
                String accountPhoneURL = account.getPhotoUrl().toString();

                System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Account info:" + "\n" +
                        "Name: " + accountName + "\n" +
                        "Email: " + accountEmail + "\n" +
                        "Id: " + accountId );

                System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Sending data for account " + accountName + " to the database in user_data.");

                //DatabaseNetworking.addNewData(new DatabaseUser());

                //maybe check if a user isn't in the database and then create a new user
                //User user = new User();
                //user.setUsername(personName);
                //user.setEmail(personEmail);
                //user.setPersonId(personId);
                //user.setPersonProfileUrl(personPhoneURL);
                //user.setSignedInWithGoogle(true);
                //updateFirebaseData(user, accountEmail);

                firebaseAuthWithGoogle(account, sourceActivity, targetActivityClass);
            } else {
                System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Unsuccessful sign in!");
            }
        }
    }

    /**
     * Determines the success of the sign in. Should be called inside onActivityResult of the activity which contains the functionality to sign the user in using google.
     * If the sign in is successful, the specified viewTab is shown inside the activity, which enables you to prompt the user to specify further information prior to registering
     * them to the firebase servers.
     * @param requestCode
     * @param data
     * @param sourceActivity
     * @param targetActivityClass
     * @return An incomplete instance of DatabaseUser (should be completed by the additional registration process if dealing with a first-time user
     */
    public static DatabaseUser determineSignInSuccess(int requestCode, Intent data, final Activity sourceActivity, final Class targetActivityClass, final ViewTab viewTab) {
        //check which request we're responding to
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()) {
                System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Google sign in result is successful");
                final GoogleSignInAccount account = result.getSignInAccount();

                //to use
                String accountName = account.getDisplayName();
                final String accountEmail = account.getEmail();
                String accountId = account.getId();
                Uri accountPhoto = account.getPhotoUrl();
                String accountPhoneURL = account.getPhotoUrl().toString();

                System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Account info:" + "\n" +
                        "Name: " + accountName + "\n" +
                        "Email: " + accountEmail + "\n" +
                        "Id: " + accountId );


                firebaseAuthWithGoogle(account, sourceActivity, targetActivityClass, viewTab);

                return new DatabaseUser("", accountName, accountEmail, null, null);

            } else {
                System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Unsuccessful sign in!");
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * This method attempts to authenticate a google-sign-in user to firebase. If the user is a new user (hasn't logged in with google before), then the
     * specified ViewTab is shown (ie the login_after_google_signup_collection), which makes the user specify further data about their account. If the user
     * has already logged in with google before, then this method automatically proceeds to the MainActivity.
     * @param account
     * @param sourceActivity
     * @param targetActivityClass
     * @param tab
     */
    private static void firebaseAuthWithGoogle(GoogleSignInAccount account , final Activity sourceActivity, final Class targetActivityClass, final ViewTab tab) {
        System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Attempting to authenticate the user to Firebase.");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if sign in fails, display a message to the user. If sign in succeeds, the auth state listener will be notified
                // and logic to handle the signed in user can be handled to the listener

                if(task.isSuccessful()) {
                    //if this is a new user, then show the viewtab
                    //todo: Also add an "or" condition here to check if the user HAS actually been registered to the database with additional info. If not, show the user the tab (with additional info collection fragments again)
                    //todo: Also add an "or" condition here to check if the user HAS verified their email. show the verification panel if necessary (must be yet added)
                    if(task.getResult().getAdditionalUserInfo().isNewUser()) {
                        tab.unhide();
                        //todo: if user exits the application while not having completed the tab, then deauthenticate their email.
                        //todo: somehow get AfterGoogleSignUpManager in here and call the start() method here.
                    } else { //if this is not a new user, then go to main activity
                        System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Authentication SUCCESSFUL!");
                        Toast.makeText(sourceActivity, "Authentication pass.", Toast.LENGTH_SHORT).show();
                        GeneralTools.launchNewActivity(sourceActivity, targetActivityClass);

                        //this finishes the activity and disables the user from returning to it!
                        sourceActivity.finish();
                    }
                } else {
                    System.out.println("[Neuron.NC.networking.LoginNetworking.firebaseAuthWithGoogle]: Authentication FAILED! Why? " + task.getException());
                    Toast.makeText(sourceActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static void firebaseAuthWithGoogle(GoogleSignInAccount account , final Activity sourceActivity, final Class targetActivityClass) {
        System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Attempting to authenticate the user to Firebase.");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if sign in fails, display a message to the user. If sign in succeeds, the auth state listener will be notified
                // and logic to handle the signed in user can be handled to the listener

                if(task.isSuccessful()) {
                    //if this is a new user, then show the viewtab
                    System.out.println("[Neuron.NC.networking.LoginNetworking.signUserInWithGoogle]: Authentication SUCCESSFUL!");
                    Toast.makeText(sourceActivity, "Authentication pass.", Toast.LENGTH_SHORT).show();
                    GeneralTools.launchNewActivity(sourceActivity, targetActivityClass);

                    //this finishes the activity and disables the user from returning to it!
                    sourceActivity.finish();
                } else {
                    System.out.println("[Neuron.NC.networking.LoginNetworking.firebaseAuthWithGoogle]: Authentication FAILED! Why? " + task.getException());
                    Toast.makeText(sourceActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Signs the user out.
     * @param fbAuth
     */
    public static void signUserOut(FirebaseAuth fbAuth, Context activityContext) {
        if(null != fbAuth.getCurrentUser()) {
            System.out.println("[Neuron.NC.networking.LoginNetworking.signUserOut]: Signing " + fbAuth.getCurrentUser().getDisplayName() + " out.");
        }
        //signs out of firebase auth
        fbAuth.signOut();

        //signs out the client from google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activityContext.getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(activityContext, gso);
        gsc.signOut();
    }
}
