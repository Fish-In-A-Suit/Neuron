package org.multiverse.multiversetools;

import android.support.design.widget.TabLayout;

/**
 * Used to set a tab instance so that it can be used within inner classes
 */
public class TabSwitcher {
    private TabLayout.Tab currentTab;

    public TabSwitcher(TabLayout.Tab tab) {
        currentTab = tab;
    }

    public TabLayout.Tab getTab() {
        return currentTab;
    }

    public void setTab(TabLayout.Tab tab) {
        currentTab = tab;
    }
}
