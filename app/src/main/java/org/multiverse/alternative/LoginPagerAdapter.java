package org.multiverse.alternative;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.multiverse.BirthdayTab;
import org.multiverse.SexTab;
import org.multiverse.UsernameTab;
import org.tord.neuroncore.registration.Sex;

/**
 * Pager adapter used to display tabs if the user decides to sign up with google so as to get user's username, sex and birthday
 */
public class LoginPagerAdapter extends FragmentPagerAdapter {
    int numberOfTabs;

    public LoginPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        System.out.println("[Neuron.LoginPagerAdapter]: Creating a new LoginPagerAdapter with " + numberOfTabs + " tabs.");
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                UsernameTab usernameTab = new UsernameTab();
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 0. Returning username tab: " + usernameTab.toString());
                return usernameTab;
            case 1:
                SexTab sexTab = new SexTab();
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 1. Returning sex tab: " + sexTab.toString());
                return sexTab;
            case 2:
                BirthdayTab birthdayTab = new BirthdayTab();
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