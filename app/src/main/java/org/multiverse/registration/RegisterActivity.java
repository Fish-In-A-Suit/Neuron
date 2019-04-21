package org.multiverse.registration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.multiverse.MainActivity;
import org.multiverse.R;
import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.multiversetools.Color;
import org.multiverse.multiversetools.alternative.SpinnerWithTitle;
import org.multiverse.registration.Registration;


public class RegisterActivity extends AppCompatActivity {
    private Registration registration;

    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText password;
    private EditText repeatPassword;
    private EditText email;
    private RadioButton male;
    private RadioButton female;
    private SpinnerWithTitle year;
    private EditText day;
    private SpinnerWithTitle month;
    private CheckBox acceptTermsAndPrivacy;

    private TextView firstNameErrorView;
    private TextView lastNameErrorView;
    private TextView usernameErrorView;
    private TextView passwordErrorView;
    private TextView repeatPasswordErrorView;
    private TextView emailErrorView;
    private TextView sexErrorView;
    private TextView dayErrorView;
    private TextView acceptErrorView;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralTools.removeActionBar(this);
        GeneralTools.changeStatusBarColor(this, R.color.login_background_gradient_top);
        setContentView(R.layout.activity_register);

        firstName = (EditText) findViewById(R.id.register_first_name);
        lastName = (EditText) findViewById(R.id.register_last_name);
        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password);
        repeatPassword = (EditText) findViewById(R.id.register_repeat_password);
        email = (EditText) findViewById(R.id.register_email);
        male = (RadioButton) findViewById(R.id.register_male);
        female = (RadioButton) findViewById(R.id.register_female);
        month = (SpinnerWithTitle) findViewById(R.id.register_month);
        day = (EditText) findViewById(R.id.register_day);
        year = (SpinnerWithTitle) findViewById(R.id.register_year);
        acceptTermsAndPrivacy = (CheckBox) findViewById(R.id.register_accept_service_privacy);

        firstNameErrorView = (TextView) findViewById(R.id.register_error_first_name);
        lastNameErrorView = (TextView) findViewById(R.id.register_error_last_name);
        usernameErrorView = (TextView) findViewById(R.id.register_error_username);
        passwordErrorView = (TextView) findViewById(R.id.register_error_password);
        repeatPasswordErrorView = (TextView) findViewById(R.id.register_error_repeat_password);
        emailErrorView = (TextView) findViewById(R.id.register_error_email);
        sexErrorView = (TextView) findViewById(R.id.register_error_sex);
        dayErrorView = (TextView) findViewById(R.id.register_error_day);
        acceptErrorView = (TextView) findViewById(R.id.register_error_accept);

        registerButton = (Button) findViewById(R.id.register_registerButton);

        month.setTitle(getString(R.string.month));
        month.setSpinnerItemLayoutId(R.layout.spinner_item_register);
        month.setValues(this, R.array.months);

        year.setTitle(getString(R.string.year));
        year.setSpinnerItemLayoutId(R.layout.spinner_item_register);
        year.setValues(this, R.array.years);

        GeneralTools.setRadioButtonOnSelectedColor(male, new Color(52, 73, 94), new Color(59, 214, 235));
        GeneralTools.setRadioButtonOnSelectedColor(female, new Color(52, 73, 94), new Color(59, 214, 235));
        GeneralTools.setCheckboxOnSelectedColor(acceptTermsAndPrivacy, new Color(52, 73, 94), new Color(59, 214, 235));

        System.out.println("[Neuron.app.RegisterActivity.onCreate]: Initializing registration...");
        registration = new Registration(this, firstName, lastName, username, password, repeatPassword, email, male, female,
        month, day, year, acceptTermsAndPrivacy, registerButton);
        registration.initalizeErrorDisplayViews(firstNameErrorView, lastNameErrorView, usernameErrorView, passwordErrorView, repeatPasswordErrorView, emailErrorView,
                sexErrorView, dayErrorView, acceptErrorView);
        registration.startRegistrationProcess();
        registration.setupRegisterButton(registerButton, this, MainActivity.class, FirebaseAuth.getInstance());
    }
}
