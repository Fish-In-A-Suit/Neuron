package org.tord.neuroncore.registration;

import org.tord.neuroncore.Constants;
import org.tord.neuroncore.database.DatabaseUser;
import org.tord.neuroncore.networking.DatabaseNetworking;
import org.tord.neuroncore.networking.Networkable;

public class RegisteredUser implements Networkable {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Sex sex;
    private Birthday birthday;

    //needed so it can be used to send it to firebase database
    public RegisteredUser() {

    }

    public RegisteredUser(String firstName, String lastName, String username, String password, String email, Sex sex, Birthday birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.birthday = birthday;

        System.out.println("[Neuron.NC.registration.RegisteredUser]: Displaying the created RegisteredUser instance... " + this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("First Name: " + firstName).append("\n");
        sb.append("Last Name: " + lastName).append("\n");
        sb.append("Username: " + username).append("\n");
        sb.append("Password: " + password).append("\n");
        sb.append("Email: " + email).append("\n");
        sb.append("Sex: " + sex.toString()).append("\n");
        sb.append("Birthday: " + birthday.toString()).append("\n");

        return sb.toString();
    }

    public String getName() {
        return firstName + " " + lastName;
    }
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Sex getSex() {
        return sex;
    }

    public String getUsername() {
        return username;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    @Override
    public void sendDataToDatabase() {
        System.out.println("[Neuron.NC.registration.RegisteredUser]: Sending user data for user " + username + " to database.");

        DatabaseNetworking.getDatabaseReference().child(Constants.DATABASE_USER_DATA_LOCATION).child(username).setValue(new DatabaseUser(this));
    }
}
