package org.multiverse.multiversetools.valuekeepers;

import java.util.ArrayList;

public class ArrayListKeeper {
    private ArrayList<?> al;

    public ArrayListKeeper(ArrayList<?> al) {
        this.al = al;
    }

    public void set(ArrayList<?> al) {
        this.al = al;
    }

    public ArrayList<?> get() {
        return al;
    }
}
