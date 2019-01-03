package core;

/**
 * This class is used to use boolean-based expressions where Java doesn't allow them. A typical case is where you have to use booleans from outer classes within inner classes.
 * For example, checking for birthday eligibility: You have to have three booleans from outer class for each of EditText-s(dayEligibility, monthEligibility, yearEligibility), which are then
 * all used and assigned different values in three different inner classes (you need three different listeners). Booleans from the outer class need to be declared final if they are to be used
 * within an innner class and therefore can't change their value.
 */
public class Switcher {
    private boolean value;

    public Switcher(boolean val) {
        value = val;
    }

    public Switcher() {

    }

    public void set(boolean val) {
        value = val;
    }

    public boolean get() {
        return value;
    }
}
