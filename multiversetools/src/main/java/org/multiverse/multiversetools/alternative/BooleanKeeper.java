package org.multiverse.multiversetools.alternative;

public class BooleanKeeper {
    private boolean value;

    public BooleanKeeper(boolean value) {
        this.value = value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    public boolean get() {
        return value;
    }
}
