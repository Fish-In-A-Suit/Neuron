package org.multiverse;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.multiverse.alternative.LoginPagerAdapter;
import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.multiversetools.Padding;
import org.tord.neuroncore.networking.DatabaseNetworking;
import org.tord.neuroncore.networking.LoginNetworking;
import org.tord.neuroncore.system.SystemUtilities;

public class LoginActivity extends AppCompatActivity implements UsernameTab.OnFragmentInteractionListener, SexTab.OnFragmentInteractionListener, BirthdayTab.OnFragmentInteractionListener {
    private FirebaseAuth firebaseAuth;

    private Button loginButton;
    private SignInButton googleSignInBtn;
    private Button registerButton;

    private EditText email;
    private EditText password;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SystemUtilities.getSystemSpecifications(this);

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseNetworking.setupDatabaseNetworking();

        //todo: remove this later!!!
        LoginNetworking.signUserOut(firebaseAuth);

        email = findViewById(R.id.login_email_field);
        password = findViewById(R.id.login_password_field);
        googleSignInBtn = findViewById(R.id.login_google_sign_in_button);
        tabLayout = (TabLayout) findViewById(R.id.login_tablayout);

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

        GeneralTools.setGoogleButtonTextProperties(googleSignInBtn, 18, "moon_bold", Typeface.BOLD, new Padding(64,12,0,20));
        GeneralTools.inspectGoogleBtn(googleSignInBtn);

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginNetworking.signUserInWithGoogle(LoginActivity.this.getBaseContext(), LoginActivity.this);
            }
        });

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.login_after_google_signup_collection);
        final LoginPagerAdapter loginPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(loginPagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("[Neuron.LoginActivity]: Setting viewpager tab to tab with position " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        LoginNetworking.determineSignInSuccess(requestCode, data, LoginActivity.this, MainActivity.class);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
