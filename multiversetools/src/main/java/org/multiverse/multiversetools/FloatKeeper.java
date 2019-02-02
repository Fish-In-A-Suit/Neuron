package org.multiverse.multiversetools;

/**
 * Class for keeping a float value so that this value can be changed in inner classes
 */
public class FloatKeeper {
    private float value;

    public FloatKeeper(float vlaue) {
        this.value = value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}