package org.multiverse.multiversetools;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.View;

/**
 * This class is used for easier integration of a ViewPager with a TabLayout
 */
public class ViewTab {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int tabCount;

    private View backgroundLayout; //the layout in which these two views are placed... used only for hiding and unhiding it's background

    public ViewTab(ViewPager vp, TabLayout tl) {
        viewPager = vp;
        tabLayout = tl;

        tabCount = tl.getTabCount();
    }

    public void setBackgroundLayout(View bl) {
        backgroundLayout = bl;
    }

    /**
     * Must be supplied with your own FragmentPagerAdapter instance, so that it is set and used by the ViewPager
     * @param fpa
     */
    public void configureViewPager(FragmentPagerAdapter fpa) {
        viewPager.setAdapter(fpa);
    }

    /**
     * Must provide access to tabLayout so a listener can be implemented in code which reacts to the changes of this tab layout
     * @return
     */
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * @return The number of tabs the tablayout instance has
     */
    public int getTabCount() {
        return tabCount;
    }

    /**
     * Hides the instance of tablayout and viewpager
     */
    public void hide() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        backgroundLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * Unhides the instances of tablayout and viewpager
     */
    public void unhide() {
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        backgroundLayout.setVisibility(View.VISIBLE);
    }
}
