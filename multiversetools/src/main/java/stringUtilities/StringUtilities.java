package stringUtilities;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtilities {

    /**
     * This method checks if teh target string matches the characters specified by allowed characters (a regular expression)
     * @param target The string to match against allowedCharacters
     * @param allowedCharacters A regular expression defining allowed characters in the target string
     * @return True if the string consists only of characters specified with allowedCharacters and false if the string contains one or more characters which aren't specified in allowedCharacters
     */
    public static boolean checkAllowedCharacters(String target, String allowedCharacters) {
        System.out.println("[Neuron.MT.StringUtilities.checkIllegalCharacters]: Checking target string ' + target +' for allowed characters" + allowedCharacters+ "'.");

        Pattern pattern = Pattern.compile(allowedCharacters);
        Matcher matcher = pattern.matcher(target);
        boolean matches = matcher.matches();

        if (matches == true) {
            System.out.println("[Neuron.MT.StringUtilities.checkAllowedCharacters]: All characters in the target string are allowed! Returning true.");
            return true;
        } else {
            System.out.println("[Neuron.MT.StringUtilities.checkAllowedCharacters]: One or more characters in the target string aren't allowed. Returning false.");
            return false;
        }
    }

    public static boolean compare(String s1, String s2) {
        if(s1.equals(s2)) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<String> stringToArrayList(String[] target) {
        System.out.println("[Neuron.MT.StringUtilities.stringToArrayList]: Converting string array to an arraylist");
        ArrayList<String> res = new ArrayList<>();

        for(String s:target) {
            res.add(s);
        }
        return res;
    }

    /**
     * Firebase database doesn't allow the path to consist of a dot character. Therefore, if you want to use an email as a path in the database,
     * you have to encode it - ie. replace the dots with sth else, for example an underscore. Encoded emails are going to be stored with underscores.
     * @param email
     * @return
     */
    public static String encodeEmail(String email) {
        return email.replace(".", "_");
    }

    /**
     * Decodes the encoded email (underscores instead of dots) to contain the dots.
     * @param email
     * @return
     */
    public static String decodeEmail(String email) {
        return email.replace("_", ".");
    }
}
