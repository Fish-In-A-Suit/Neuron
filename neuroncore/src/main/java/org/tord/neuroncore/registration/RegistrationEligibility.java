package org.tord.neuroncore.registration;

/**
 * This class contains only boolean values which are consequentially set when the user is registering, and at the end
 * determine if the user can or can't register
 */
public class RegistrationEligibility {
    private boolean eligibleFirstName; //if user has eligible first name (no invalid characters and sufficient length)
    private boolean eligibleLastName; //if user has eligible last name (no invalid characters and sufficient length)
    private boolean eligibleUsername;
    private boolean passwordSufficientLength; //if password is long enough
    private boolean matchingPassword; //if the password and repeat_password match
    private boolean eligibleEmail; //if user has eligible email (isn't already used and is in existence)
    private boolean eligibleSex; //if the user has specified their sex
    private boolean eligibleBirthday; //if the day doesn't exceed number 31
    private boolean eligibleAcceptance; //if user has accepted terms of service and privacy policy

    private boolean totalEligibility; //if all of the above booleans are true

    public RegistrationEligibility() {
        eligibleEmail = true; //email validity is checked only after the user presses the register button!
    }

    public void setEligibleFirstName(boolean eligibleFirstName) {
        this.eligibleFirstName = eligibleFirstName;
    }

    public void setEligibleLastName(boolean eligibleLastName) {
        this.eligibleLastName = eligibleLastName;
    }

    public void setEligibleUsername(boolean eligibleUsername) {
        this.eligibleUsername = eligibleUsername;
    }

    public void setPasswordSufficientLength(boolean passwordSufficientLength) {
        this.passwordSufficientLength = passwordSufficientLength;
    }

    public void setMatchingPassword(boolean matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public void setEligibleEmail(boolean eligibleEmail) {
        this.eligibleEmail = eligibleEmail;
    }

    public void setEligibleSex(boolean eligibleSex) {
        this.eligibleSex = eligibleSex;
    }

    public void setEligibleBirthday(boolean eligibleBirthday) {
        this.eligibleBirthday = eligibleBirthday;
    }

    public void setEligibleAcceptance(boolean eligibleAcceptance) {
        this.eligibleAcceptance = eligibleAcceptance;
    }

    public boolean getTotalEligibility() {
        if (eligibleFirstName && eligibleLastName && eligibleUsername && passwordSufficientLength && matchingPassword && eligibleEmail && eligibleSex && eligibleBirthday && eligibleAcceptance) {
            System.out.println("[Neuron.NC.registration.RegistrationEligibility]: The user can be registered!");
            return true;
        } else {
            System.out.println("[Neuron.NC.registration.RegistrationEligibility]: The user can't be registered!");
            return false;
        }
    }

    public void display() {
        System.out.println("[Neuron.NC.registration.RegistrationEligibility]: Displaying instance of RegistrationEligibility:");
        System.out.println("    - eligibleFirstName = " + eligibleFirstName + "\n" +
        "   - eligibleLastName = " + eligibleLastName + "\n" +
        "   - eligibleUsername= " + eligibleUsername + "\n" +
        "   - passwordSufficientLength = " + passwordSufficientLength + "\n" +
        "   - matchingPassword = " + matchingPassword + "\n" +
        "   - eligibleEmail = " + eligibleEmail + "\n" +
        "   - eligibleSex = " + eligibleSex + "\n" +
        "   - eligibleBirthday = " + eligibleBirthday + "\n" +
        "   - eligibleAcceptance = " + eligibleAcceptance);
    }
}
