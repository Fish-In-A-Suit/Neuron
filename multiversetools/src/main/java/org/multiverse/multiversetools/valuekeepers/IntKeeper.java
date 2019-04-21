package org.multiverse.multiversetools.valuekeepers;

/**
 * Class for keeping an int value so that this value can be changed in inner classes
 */
public class IntKeeper {
    private int value;

    public IntKeeper(int vlaue) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
