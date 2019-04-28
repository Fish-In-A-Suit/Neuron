package org.multiverse.database;

import com.google.firebase.database.Exclude;

import org.tord.neuroncore.Constants;
import org.tord.neuroncore.networking.Networkable;
import org.tord.neuroncore.registration.Birthday;
import org.multiverse.registration.RegisteredUser;
import org.tord.neuroncore.registration.Sex;

/**
 * This class is a container that contains all of the information about the user which is to be sent to the database during the registration process
 */
public class DatabaseUser implements Networkable {
    private String username;
    private String fullName;
    private String email;
    private String sex;
    private String birthday;

    public DatabaseUser() {

    }

    public DatabaseUser(String username, String name, String email, Sex sex, Birthday birthday) {
        this.username = username;
        fullName = name;
        this.email = email;

        if(null != sex && null != birthday) {
            if(sex == Sex.MALE) {
                this.sex = "Male";
            } else {
                this.sex = "Female";
            }

            this.birthday = birthday.toString();
        }
    }

    public DatabaseUser(RegisteredUser ru) {
        username = ru.getUsername();
        fullName = ru.getName();
        email = ru.getEmail();

        if(ru.getSex() == Sex.MALE) {
            sex = "Male";
        } else {
            sex = "Female";
        }

        birthday = ru.getBirthday().toString();
    }

    @Override
    public void sendDataToDatabase() {
        System.out.println("[Neuron.NC.database.DatabaseUser]: Sending user data for user " + username + " to the server.");
        DatabaseNetworking.getDatabaseReference().child(Constants.DATABASE_USER_DATA_LOCATION).child(username).setValue(this);
        DatabaseNetworking.getDatabaseReference().child(Constants.DATABASE_EMAILS_USED_LOCATION).child(username).setValue(email);
        DatabaseNetworking.getDatabaseReference().child(Constants.DATABASE_USERNAMES_USED_LOCATION).child(fullName).setValue(username);
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    @Exclude
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        System.out.println("[Neuron.NC.database.DatabaseUser.setUsername]: Setting username to: " + username);
        this.username = username;
    }

    public void setSex(Sex sex) {
        System.out.println("[Neuron.NC.database.DatabaseUser.setUsername]: Setting sex to: " + sex);
        if(sex == Sex.MALE) {
            this.sex = "Male";
        } else {
            this.sex = "Female";
        }
    }

    public void setBirthday(Birthday birthday) {
        System.out.println("[Neuron.NC.database.DatabaseUser.setUsername]: Setting birthday to: " + birthday);
        this.birthday = birthday.toString();
    }

    public void display() {
        System.out.println("[Neuon.DatabaseUser.display]: Displaying DatabaseUser:");
        System.out.println("[Neuron]    - username: " + username);
        System.out.println("[Neuron]    - full_name: " + fullName);
        System.out.println("[Neuron]    - sex: " + sex);
        System.out.println("[Neuron]    - birthday: " + birthday);
    }
}
