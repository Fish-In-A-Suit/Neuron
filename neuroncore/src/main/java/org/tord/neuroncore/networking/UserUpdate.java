package org.tord.neuroncore.networking;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * This class is responsible for providing easy-to-use methods for updating instances of FirebaseUser
 */

//todo: provide a method to send user verification email
//todo: provide a method to update user photo url
//todo: provide other methods for all of the cases in https://firebase.google.com/docs/auth/android/manage-users

public class UserUpdate {

    /**
     * Sets the specified name as the display name of the specified firebaseUser
     * @param firebaseUser
     * @param name
     */
    public static void setDisplayName(final FirebaseUser firebaseUser, final String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    System.out.println("[Neuron.NC.networking.UserUpdate.setDisplayName]: Setting display name to user with email " + firebaseUser.getEmail() + " to " + name + " is successful.");
                } else {
                    System.out.println("[Neuron.NC.networking.UserUpdate.setDisplayName]: Setting display name to user with email " + firebaseUser.getEmail() + " to " + name + " FAILED.");
                }
            }
        });
    }
}
