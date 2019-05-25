package org.multiverse.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.multiverse.MainActivity;
import org.multiverse.R;
import org.multiverse.alternative.LoginPagerAdapter;
import org.multiverse.database.DatabaseNetworking;
import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.multiversetools.Padding;
import org.multiverse.multiversetools.TabSwitcher;
import org.multiverse.multiversetools.ViewTab;
import org.multiverse.database.DatabaseUser;
import org.multiverse.registration.firstTimeGoogleSignup.FTGSmanager;
import org.multiverse.registration.RegisterActivity;
import org.multiverse.registration.firstTimeGoogleSignup.BirthdayFragment;
import org.multiverse.registration.firstTimeGoogleSignup.PasswordFragment;
import org.multiverse.registration.firstTimeGoogleSignup.SexFragment;
import org.multiverse.registration.firstTimeGoogleSignup.UsernameFragment;
import org.tord.neuroncore.Constants;
import org.tord.neuroncore.system.SystemUtilities;
import java.util.List;

public class LoginActivity extends FragmentActivity implements UsernameFragment.OnFragmentInteractionListener, PasswordFragment.OnFragmentInteractionListener, SexFragment.OnFragmentInteractionListener, BirthdayFragment.OnFragmentInteractionListener {
    private FirebaseAuth firebaseAuth;

    private Button loginButton;
    private SignInButton googleSignInBtn;
    private Button registerButton;

    private EditText email;
    private EditText password;

    private ViewTab FTGStab;
    private FTGSmanager FTGSmanager;

    private final Activity loginActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SystemUtilities.getSystemSpecifications(this);

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseNetworking.setupDatabaseNetworking();

        //todo: remove this later!!!
        LoginNetworking.signUserOut(firebaseAuth, this);

        email = findViewById(R.id.login_email_field);
        password = findViewById(R.id.login_password_field);
        loginButton = findViewById(R.id.login_loginbutton);
        googleSignInBtn = findViewById(R.id.login_google_sign_in_button);

        loginButton = findViewById(R.id.login_loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginNetworking.signUserIn(LoginActivity.this, MainActivity.class, firebaseAuth, email.getText().toString(), password.getText().toString());
            }
        });

        registerButton = findViewById(R.id.login_registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralTools.launchNewActivity(findViewById(R.id.login_activity).getContext(), RegisterActivity.class);
            }
        });

        //configures the google sign in button
        configureGoogleButton();

        //configures the appearance of the viewpager which should pop up if the user is first time registering using google
        configureViewTab((ViewPager) findViewById(R.id.login_FTGS_viewpager), (TabLayout) findViewById(R.id.login_FTGS_tablayout));
    }

    //on start is called after onCreate is completed
    @Override
    public void onStart() {
        super.onStart();

        //check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);

    }

    /**
     * Either starts the main activity if user isn't null or persists in the login activity if user is null
     * @param user
     */
    private void updateUI(FirebaseUser user) {
        if(null!=user) {
            System.out.println("[Neuron.Login.updateUI]: User exists" + user.getDisplayName() + ")! Starting main activity!");

            //todo: enable this!
            //GeneralTools.launchNewActivity(this, MainActivity.class);
            //this.finish();
        } else {
            System.out.println("[Neuron.Login.updateUI]: User doesn't exist. Remaining in login activity!");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("[Neuron.Login.onActivityResult]: In onActivityResult");
        System.out.println("[Neuron.Login.onActivityResult]: requestCode = " + requestCode + " | resultCode = " + resultCode);

        if(requestCode == Constants.RC_SIGN_IN_GOOGLE) {
            DatabaseUser incompleteDatabaseUser = LoginNetworking.determineSignInSuccess(requestCode, data, LoginActivity.this, MainActivity.class, FTGStab);
            System.out.println("[Neuron.Login.onActivityResult]: Displaying incompleteDatabaseUser");
            incompleteDatabaseUser.display();

            System.out.println("[Neuron.Login.onActivityResult]: Starting to query for individual fragments in login_FTGS_viewPagerLayout");
            List<Fragment> allFragments = GeneralTools.findFragmentsInViewPager(FTGStab.getViewPager());

            UsernameFragment usernameFragment = (UsernameFragment) allFragments.get(0);
            PasswordFragment passwordFragment = (PasswordFragment) allFragments.get(1);
            SexFragment sexFragment = (SexFragment) allFragments.get(2);
            BirthdayFragment birthdayFragment = (BirthdayFragment) allFragments.get(3);
            System.out.println("[Neuron.login.LoginActivity.onActivityResult]: #5");

            //TODO: START FROM HERE!!! SEND THE PASSWORD FRAGMENT TO AGSM AND THEN PROCESS THE PASSWORD AND SEND IT TO THE SERVER!!!
            //TODO rename FTGSmanager to FTGSmanager
            FTGSmanager = new FTGSmanager(this, this.getApplicationContext(), FTGStab, usernameFragment, passwordFragment, sexFragment,
                    birthdayFragment, incompleteDatabaseUser);
            //start the signup manager
            FTGSmanager.start();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void configureViewTab(ViewPager vp, TabLayout tl) {
        //sets the properties of the input tab
        tl.setTabGravity(TabLayout.GRAVITY_FILL);
        GeneralTools.setTabLayoutMargins(tl, 0, 0, 20, 0);

        FTGStab = new ViewTab(vp, tl);
        FTGStab.setBackgroundLayout((View) findViewById(R.id.login_FTGS_viewPagerLayout));
        FTGStab.hide(); //hidden by default
        LoginPagerAdapter lpa = new LoginPagerAdapter(getSupportFragmentManager(), FTGStab.getTabCount());
        FTGStab.configureViewPager(lpa);

        //this makes the viewpager not dump the fragments when scrolling through them
        GeneralTools.makeViewPagerNotForget(FTGStab.getViewPager());

        //this configures that the dot is displayed in the tablayout to indicate which tab the user is currently in
        final TabSwitcher tabSwitcher = new TabSwitcher(null);

        TabLayout.Tab initialTab = FTGStab.getTabLayout().getTabAt(0);
        initialTab.setIcon(R.drawable.login_googleregister_tabitem_background_position); //set the position of the first tab
        tabSwitcher.setTab(initialTab);

        FTGStab.getTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //called each time a new tab is selected
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                System.out.println("[Neuron.LoginActivity]: Setting viewpager item to tab with position " + tab.getPosition());
                FTGStab.getViewPager().setCurrentItem(tab.getPosition());

                if(null != tabSwitcher.getTab()) {
                    tabSwitcher.getTab().setIcon(R.drawable.login_googleregister_tabitem_background);
                }

                tab.setIcon(R.drawable.login_googleregister_tabitem_background_position);
                tabSwitcher.setTab(tab);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FTGStab.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            //called each time a new page is selected by scrolling to it in viewpager
            @Override
            public void onPageSelected(int i) {
                TabLayout.Tab currentTab = FTGStab.getTabLayout().getTabAt(i);

                FTGStab.getViewPager().setCurrentItem(i);

                if(null != tabSwitcher.getTab()) {
                    tabSwitcher.getTab().setIcon(R.drawable.login_googleregister_tabitem_background);
                }

                currentTab.setIcon(R.drawable.login_googleregister_tabitem_background_position);
                tabSwitcher.setTab(currentTab);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    //everything that has to deal with the google sign in button
    private void configureGoogleButton() {
        GeneralTools.setGoogleButtonTextProperties(googleSignInBtn, 18, "moon_bold", Typeface.BOLD, new Padding(64,12,0,20));
        GeneralTools.inspectGoogleBtn(googleSignInBtn);

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginNetworking.signUserInWithGoogle(LoginActivity.this.getBaseContext(), LoginActivity.this);
                googleSignInBtn.setEnabled(false); //disables the google btn to prevent further clicks
            }
        });
    }

    private void configureLoginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginNetworking.signUserIn(loginActivity, MainActivity.class, FirebaseAuth.getInstance(), email.getText().toString(), password.getText().toString());
            }
        });
    }

    public void enableGoogleButton(boolean enable) {
        if(enable == true) {
            googleSignInBtn.setEnabled(true);
        } else {
            googleSignInBtn.setEnabled(false);
        }
    }

    /**
     * Displays the specified localized message using a toast
     * @param msg
     */
    public void displayMsg(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
