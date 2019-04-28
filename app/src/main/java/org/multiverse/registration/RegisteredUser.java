package org.multiverse.registration;

import org.multiverse.database.DatabaseNetworking;
import org.multiverse.database.DatabaseUser;
import org.tord.neuroncore.networking.Networkable;
import org.tord.neuroncore.registration.Birthday;
import org.tord.neuroncore.registration.Sex;

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

    /**
     * This method is called from DatabaseNetworking to send data to the firebase databasse
     */
    @Override
    public void sendDataToDatabase() {
        System.out.println("[Neuron.NC.registration.RegisteredUser]: Sending user data for user " + username + " to database.");

        //adds this user to user_data node (under a child node which is the username)
        DatabaseNetworking.writeUser(this);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * @return A DatabaseUser instance from the specified fields
     */
    public DatabaseUser buildDatabaseUser() {
        return new DatabaseUser(username, firstName + " " + lastName, email, sex, birthday);
    }
}
