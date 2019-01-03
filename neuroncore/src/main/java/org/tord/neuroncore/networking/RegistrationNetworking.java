package org.tord.neuroncore.networking;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import org.multiverse.multiversetools.GeneralTools;
import org.tord.neuroncore.Constants;
import org.tord.neuroncore.registration.RegisteredUser;

/**
 * This class provides functionality for registering a user to the server.
 */
public class RegistrationNetworking {

    /**
     * This method attempts to sign up (register) the specified RegisteredUser instance and also update the display name of the created FirebaseUser and then, if
     * the registration process is successful, start the activity specified by targetActivityClass.
     * @param currentActivty
     * @param targetActivityClass
     * @param fbAuth
     * @param registeredUser
     */
    public static void registerUser(final Activity currentActivty, final Class targetActivityClass, final FirebaseAuth fbAuth, final RegisteredUser registeredUser) throws FirebaseAuthInvalidCredentialsException {
        fbAuth.createUserWithEmailAndPassword(registeredUser.getEmail(), registeredUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //todo: store user-related info to the database
                    DatabaseNetworking.addNewData(registeredUser);

                    FirebaseUser user = fbAuth.getCurrentUser();
                    UserUpdate.setDisplayName(user, registeredUser.getName());
                    updateUIOnSuccessfulRegistration(user, currentActivty, targetActivityClass);
                } else {
                    Toast.makeText(currentActivty, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //updates the UI (ie starts the target activity) ... call this if registration process is successful
    private static void updateUIOnSuccessfulRegistration(FirebaseUser user, Activity current, Class targetClass) {
        //todo: greet the user
        GeneralTools.launchNewActivity(current.getBaseContext(), targetClass);
        //Toast.makeText(targetClass, "Authentication successful! Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}
