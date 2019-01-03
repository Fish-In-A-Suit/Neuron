package org.tord.neuroncore.database;

import org.tord.neuroncore.Constants;
import org.tord.neuroncore.networking.DatabaseNetworking;
import org.tord.neuroncore.networking.Networkable;
import org.tord.neuroncore.registration.Birthday;
import org.tord.neuroncore.registration.RegisteredUser;
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

        if(sex == Sex.MALE) {
            this.sex = "Male";
        } else {
            this.sex = "Female";
        }

        this.birthday = birthday.toString();
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
        DatabaseNetworking.getDatabaseReference().child(Constants.DATABASE_USER_DATA_LOCATION).child(username).setValue(this);
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
}
