package org.tord.neuroncore.registration;

/**
 * Contains conditions which are used in the process of registration
 */
public class RegistrationConditions {
    public static String illegalCharacters = "\\W"; //non-word regular expression... used to determine any non-word characters which are illegal in firstname, lastname, username, etc.
    public static String allowedNameCharacters ="[a-zA-Z]+"; //word regular expression... used to determine characters which are legal in firstname and lastname
    public static String allowedUsernameCharacters="[\\w-]+";
    public static int minimumFirstNameLength = 2;
    public static int minimumLastNameLength = 2;
    public static int minimumUsernameLength = 3;
    public static int minimumPasswordLength = 6;
}
