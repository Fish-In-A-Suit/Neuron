package org.multiverse.alternative;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.multiverse.registration.firstTimeGoogleSignup.BirthdayFragment;
import org.multiverse.registration.firstTimeGoogleSignup.PasswordFragment;
import org.multiverse.registration.firstTimeGoogleSignup.SexFragment;
import org.multiverse.registration.firstTimeGoogleSignup.UsernameFragment;

/**
 * Pager adapter used to display tabs if the user decides to sign up with google so as to get user's username, sex and birthday
 */
public class LoginPagerAdapter extends FragmentPagerAdapter {
    int numberOfFragments;

    private UsernameFragment usernameFragment;
    private PasswordFragment passwordFragment;
    private SexFragment sexFragment;
    private BirthdayFragment birthdayFragment;

    public LoginPagerAdapter(FragmentManager fm, int numberOfFragments) {
        super(fm);
        System.out.println("[Neuron.LoginPagerAdapter]: Creating a new LoginPagerAdapter with " + numberOfFragments + " tabs.");
        this.numberOfFragments = numberOfFragments;

        usernameFragment = new UsernameFragment();
        passwordFragment = new PasswordFragment();
        sexFragment = new SexFragment();
        birthdayFragment = new BirthdayFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 0. Returning username tab: " + usernameFragment.toString());
                return usernameFragment;
            case 1:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 1. Returning sex tab: " + passwordFragment.toString());
                return passwordFragment;
            case 2:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 2. Returning birthday tab: " + sexFragment.toString());
                return sexFragment;
            case 3:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected 3. Returning birthday tab: "+ birthdayFragment.toString());
                return birthdayFragment;
            default:
                System.out.println("[Neuron.LoginPagerAdapter]: Selected " + i + " Returning default null.");
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfFragments;
    }
}