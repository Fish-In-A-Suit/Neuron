package org.multiverse.multiversetools.valuekeepers;

public class StringKeeper {
    private String value;

    public StringKeeper(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
