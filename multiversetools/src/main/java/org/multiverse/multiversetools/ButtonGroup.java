package org.multiverse.multiversetools;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to hold several buttons
 */
public class ButtonGroup {
    private ArrayList<Button> buttons;

    public ButtonGroup() {

    }

    public ButtonGroup(Button b1, Button b2, Button b3) {
        buttons = new ArrayList<>();
        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);
    }

    public ButtonGroup(ArrayList<Button> btns) {
        buttons = btns;
    }

    public void enableAll() {
        for(Button b : buttons) {
            if(null!=b) {
                b.setEnabled(true);
            }
        }
    }

    public void disableAll() {
        for(Button b : buttons) {
            if(null!=b) {
                b.setEnabled(false);
            }
        }
    }

    public int getCount() {
        return buttons.size();
    }

    public List<Button> getAsList() {
        return buttons;
    }
}
