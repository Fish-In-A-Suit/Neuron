package org.multiverse.registration;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import org.multiverse.database.DatabaseNetworking;
import org.multiverse.multiversetools.GeneralTools;
import org.tord.neuroncore.networking.UserUpdate;

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
                    //DatabaseNetworking.addNewData(registeredUser);
                    DatabaseNetworking.checkForEmailClashesAndSendToDatabase(registeredUser, currentActivty, targetActivityClass);
                } else {
                    try {
                        throw new FirebaseAuthInvalidCredentialsException("ERROR_CREDENTIAL_ALREADY_IN_USE", "[Neuron.registration.RegistrationNetworking.registerUser]: Invalid credentials");
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        e.printStackTrace();
                    }

                    //todo: check for the error code? and then determine what to show.

                    Toast.makeText(currentActivty, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
