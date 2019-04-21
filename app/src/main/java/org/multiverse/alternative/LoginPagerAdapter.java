package org.multiverse.alternative;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.multiverse.registration.firstTimeGoogleSignup.BirthdayTab;
import org.multiverse.registration.firstTimeGoogleSignup.SexTab;
import org.multiverse.registration.firstTimeGoogleSignup.UsernameTab;

/**
 * Pager adapter used to display tabs if the user decides to sign up with google so as to get user's username, sex and birthday
 */
public class LoginPagerAdapter extends FragmentPagerAdapter {
    int numberOfTabs;

    private UsernameTab usernameTab;
    private SexTab sexTab;
    private BirthdayTab birthdayTab;

    public LoginPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        System.out.println("[Neuron.LoginPagerAdapter]: Creating a new LoginPagerAdapter with " + numberOfTabs + " tabs.");
        this.numberOfTabs = numberOfTabs;

        usernameTab = new UsernameTab();
        sexTab = new SexTab();
        birthdayTab = new BirthdayTab();
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 0. Returning username tab: " + usernameTab.toString());
                return usernameTab;
            case 1:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 1. Returning sex tab: " + sexTab.toString());
                return sexTab;
            case 2:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 2. Returning birthday tab: " + birthdayTab.toString());
                return birthdayTab;
            default:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected " + i + " Returning default null.");
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}