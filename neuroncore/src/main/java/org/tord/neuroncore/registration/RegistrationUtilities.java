package org.tord.neuroncore.registration;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.multiverse.multiversetools.GeneralTools;

import stringUtilities.StringUtilities;

/**
 * A utility class regarding to the registration process
 */
public class RegistrationUtilities {

    /**
     * Checks if the specified length is greater than the minimum username length specified in RegistrationConditions
     * @param length
     * @return
     */
    public static boolean checkUsernameLength(int length) {
        if (length >= RegistrationConditions.minimumUsernameLength) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the specified length is greater than the minimum first name length specified in RegistrationConditions
     * @param length
     * @return
     */
    public static boolean checkFirstNameLength(int length) {
        if (length >= RegistrationConditions.minimumFirstNameLength) {
            System.out.println("[Neuron.NC.RgistrationUtilities.checkFirstNameLength]: First name length is " + length + ". It is sufficent.");
            return true;
        } else {
            System.out.println("[Neuron.NC.RegistrationUtilities.checkFirstNameLength]: First name length is " + length + ". Insufficient.");
            return false;
        }
    }

    /**
     * Checks if the specified length is greater than the minimum last name length specified in RegistrationConditions
     * @param length
     * @return
     */
    public static boolean checkLastNameLength(int length) {
        if (length >= RegistrationConditions.minimumLastNameLength) {
            System.out.println("[Neuron.NC.RgistrationUtilities.checkFirstNameLength]: Last name length is " + length + ". It is sufficent.");
            return true;
        } else {
            System.out.println("[Neuron.NC.RgistrationUtilities.checkFirstNameLength]: First name length is " + length + ". It is INSUFFICIENT.");
            return false;
        }
    }

    /**
     * Checks whether the target String (name) contains only allowed characters specified in RegistrationConditions.allowedNameCharacters
     * @param target The target String to check against allowed characters
     * @return True if and only if the entire target String matches the allowed characters specified by RegistrationConditions.allowedNameCharacters
     */
    //todo: exclude whitespace!
    public static boolean checkNameAllowedCharacters(String target) {
        return StringUtilities.checkAllowedCharacters(target, RegistrationConditions.allowedNameCharacters);
    }

    /**
     * Checks whether the target String (username) contains only allowed characters specified in RegistrationConditions.allowedUsernameCharacters
     * @param target The target String to check against allowed characters for usernames
     * @return True if and only if the entire target String matches the allowed characters specified by RegistrationConditions.allowedUsernameCharacters
     */
    public static boolean checkUsernameAllowedCharacters(String target) {
        return StringUtilities.checkAllowedCharacters(target, RegistrationConditions.allowedUsernameCharacters);
    }

    /**
     * Checks whether the day specified for birthday doesn't exceed 31 and that only numbers are input
     * @param cs
     * @return
     */
    public static boolean checkBirthdayDay(CharSequence cs) {
        try {
            int number = Integer.parseInt(cs.toString());

            if (number <= 31) {
                System.out.println("[Neuron.NC.registration.RegistrationUtilities.checkBirthday]: The specified day (" + number + ") for birthday is valid.");
                return true;
            } else {
                System.out.println("[Neuron.NC.registration.RegistrationUtilities.checkBirthday]: The specified day (" + number + ") for birthday is invalid!");
                return false;
            }
        } catch (NumberFormatException nfe) {
            System.out.println("[Neuron.NC.registration.RegistrationUtilities.checkBirthday]: Invalid value (other than a number) input inside the \"day\" edittext.");
            return false;
            //todo: create custom made-error so that text turns red???
        }
    }

    /**
     * Checks whether the specified password is greater or equal than the length specified by RegistrationConditions.minimumPasswordLength
     * @param s
     * @return
     */
    public static boolean checkPasswordSufficientLength(String s) {
        if(s.length() >= RegistrationConditions.minimumPasswordLength) {
            System.out.println("[Neuron.NC.registration.RegistrationUtilities.checkPasswordSufficientLength]: Password length is sufficient: " + s.length());
            return true;
        } else {
            System.out.println("[Neuron.NC.registration.RegistrationUtilities.checkPasswordSufficientLength]: Password length is insufficient: " + s.length());
            return false;
        }
    }

    /**
     * Enables the specified button if registrationEligibility.getTotalEligibility is true
     * @param b
     */
    public static void tryToEnableRegistration(RegistrationEligibility re, Button b) {
        if (re.getTotalEligibility() == true) {
            System.out.println("[Neuron.NC.registration.RegistrationUtilities.tryToEnableRegistration]: Enabled user registration!");
            b.setEnabled(true);
        } else {
            System.out.println("[Neuron.NC.registration.RegistrationUtilities.tryToEnableRegistration]: Disabled user registration!");
            b.setEnabled(false);
        }
    }

    /**
     * Creates a RegisteredUser instance out of the data contained in the fields specified
     * @param firstName
     * @param lastName
     * @param username
     * @param password
     * @param email
     * @param male
     * @param month
     * @param day
     * @param year
     * @return
     */
    public static RegisteredUser createRegisteredUser(EditText firstName, EditText lastName, EditText username, EditText password, EditText email, RadioButton male, Spinner month, EditText day, Spinner year) {
        Sex sex;
        if(male.isChecked() == true) {
            sex = Sex.MALE;
        } else {
            sex = Sex.FEMALE;
        }

        Birthday bday = new Birthday(month.getSelectedItem().toString(), Integer.parseInt(day.getText().toString()), Integer.parseInt(year.getSelectedItem().toString()));

        //todo: fix!!
        return new RegisteredUser(firstName.getText().toString(),
                lastName.getText().toString(),
                username.getText().toString(),
                password.getText().toString(),
                email.getText().toString(),
                sex,
                bday);
    }
}
