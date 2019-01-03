package org.tord.neuroncore.registration;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import org.multiverse.multiversetools.GeneralTools;
import org.tord.neuroncore.R;
import org.tord.neuroncore.error.registrationerror.InvalidCharacterError;
import org.tord.neuroncore.error.registrationerror.InvalidEmailError;
import org.tord.neuroncore.error.registrationerror.InvalidLengthError;
import org.tord.neuroncore.error.registrationerror.PasswordsDontMatchError;
import org.tord.neuroncore.error.registrationerror.RegistrationErrorDisplay;
import org.tord.neuroncore.networking.RegistrationNetworking;
import org.w3c.dom.Text;

import java.lang.annotation.Target;

import core.Switcher;
import stringUtilities.StringUtilities;

/**
 * Contains client-side functionality needed during the registration process. An instance of this class keeps track of the specified fields contained in activity where the user
 * registers. It tracks eligibility of all of the specified fields (ie if the username is not too short, if the password is strong enough, etc). At the end of the registration process,
 * if all of the fields are eligible, it enables the register button and allows the user to register to the server.
 */
//TODO: check if the specified username is available!
public class Registration {
    private RegistrationEligibility registrationEligibility;
    private RegisteredUser registeredUser;

    private Context activityContext;
    //used when setting up register button
    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText password;
    private EditText repeatPassword;
    private EditText email;
    private RadioButton male;
    private RadioButton female;
    private Spinner month;
    private EditText day;
    private Spinner year;
    private CheckBox acceptTermsAndPrivacy;
    private Button registerButton;

    private TextView firstNameErrorView;
    private TextView lastNameErrorView;
    private TextView usernameErrorView;
    private TextView passwordErrorView;
    private TextView repeatPasswordErrorView;
    private TextView emailErrorView;
    private TextView sexErrorView;
    private TextView dayErrorView;
    private TextView acceptErrorView;

    private int error_color;
    private int regular_color;

    /**
     * Initializes all of the fields which are to be used during the registration process.
     * //todo: THERE IS NO WAY TO CHECK WHETHER AN EMAIL IS VALID. THEREFORE, MAKE SURE TO SET UP EMAIL VERIFICATION PROCESS
     * //todo: create better notification if the user is denied from registering to the server (if user sign up fails)
     * //todo: send all of the other data (other than email and password) to the server
     *
     * @param firstName
     * @param lastName
     * @param userName
     * @param password
     * @param repeatPassword
     * @param email
     * @param male
     * @param female
     * @param month
     * @param day
     * @param year
     * @param acceptTermAndPrivacy
     * @param registerButton
     */
    public Registration(Context context, EditText firstName, EditText lastName, EditText userName, EditText password, EditText repeatPassword, EditText email, RadioButton male, RadioButton female, Spinner month, EditText day,
                        Spinner year, CheckBox acceptTermAndPrivacy, Button registerButton) {
        activityContext = context;

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = userName;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.email = email;
        this.male = male;
        this.female = female;
        this.month = month;
        this.day = day;
        this.year = year;
        this.acceptTermsAndPrivacy = acceptTermAndPrivacy;
        this.registerButton = registerButton;

        error_color = R.color.error;
        regular_color = R.color.default_color;

        registrationEligibility = new RegistrationEligibility();

        //TODO: Check for email validity!
        //TODO: Construct all of the error boxes and an efficient system to make the app know which error to display in which error box.

    }

    /**
     * Sets up all of the listeners to the input fields and therefore starts the process of registration. It is obligatory to call this method!
     */
    public void startRegistrationProcess() {

        //check first name eligibility (sufficient length and no illegal characters)
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean sufficientLength = false; //the length of the first name
                boolean noIllegalChars = false;  //if the string doesn't contain illegal characters

                System.out.println("[Neuron.NC.Registration]: Starting to check character sequence '" + s.toString() + "' for eligibility.");

                sufficientLength = RegistrationUtilities.checkFirstNameLength(s.length());
                noIllegalChars = RegistrationUtilities.checkNameAllowedCharacters(s.toString());

                if (sufficientLength == true && noIllegalChars == true) {
                    System.out.println("[Neuron.NC.Registration]: First name is of sufficient length and doesn't contain any illegal characters! Setting eligibleFirstName in RegistrationEligibility to true.");
                    registrationEligibility.setEligibleFirstName(true);
                    firstNameErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, firstName, regular_color);
                } else {
                    if(!sufficientLength) {
                        RegistrationErrorDisplay.displayError(new InvalidLengthError(R.string.error_first_name_invalid_length), firstNameErrorView);
                        GeneralTools.setViewBackgroundTint(activityContext, firstName, error_color);
                    } else {
                        GeneralTools.setViewBackgroundTint(activityContext, firstName, regular_color);
                        firstNameErrorView.setText("");
                    }

                    if(!noIllegalChars) {
                        RegistrationErrorDisplay.displayError(new InvalidCharacterError(R.string.error_first_name_invalid_character), firstNameErrorView);
                        GeneralTools.setViewBackgroundTint(activityContext, firstName, error_color);
                    } else if (sufficientLength) {
                        firstNameErrorView.setText("");
                        GeneralTools.setViewBackgroundTint(activityContext, firstName, regular_color);
                    }

                    System.out.println("[Neuron.NC.Registration]: First name doesn't match the requirements... sufficientLength = " + sufficientLength + ", noIllegalCharacters = " + noIllegalChars + ".");
                    registrationEligibility.setEligibleFirstName(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //check last name eligibility (sufficient length and no illegal characters)
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean sufficientLength = false;
                boolean noIllegalCharacters = false;

                System.out.println("[Neuron.NC.Registration]: Starting to check character sequence '" + s.toString() + "' for eligibility.");

                sufficientLength = RegistrationUtilities.checkLastNameLength(s.length());
                noIllegalCharacters = RegistrationUtilities.checkNameAllowedCharacters(s.toString());

                if(!sufficientLength) {
                    RegistrationErrorDisplay.displayError(new InvalidLengthError(R.string.error_last_name_invalid_length), lastNameErrorView);
                    GeneralTools.setViewBackgroundTint(activityContext, lastName, error_color);
                } else {
                    lastNameErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, lastName, regular_color);
                }

                if(!noIllegalCharacters) {
                    RegistrationErrorDisplay.displayError(new InvalidCharacterError(R.string.error_last_name_invalid_character), lastNameErrorView);
                    GeneralTools.setViewBackgroundTint(activityContext, lastName, error_color);
                } else if (sufficientLength) {
                    lastNameErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, lastName, regular_color);
                }

                if(sufficientLength == true && noIllegalCharacters == true) {
                    System.out.println("[Neuron.NC.Registration]: Last name is of sufficient length and doesn't contain any illegal characters! Setting eligibleLastName in RegistrationEligibility to true.");
                    registrationEligibility.setEligibleLastName(true);
                } else {
                    System.out.println("[Neuron.NC.Registration]: Setting eligibleLastName to FALSE! sufficientLength = " + sufficientLength + ",  noIllegalCharacters = " +  noIllegalCharacters);
                    registrationEligibility.setEligibleLastName(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //check for username eligibility ... checks for sufficient length and no illegal characters
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean sufficientLength = false;
                boolean noIllegalCharacters = false;

                System.out.println("[Neuron.NC.Registration]: Starting to check character sequence '" + s.toString() + "' for eligibility.");

                sufficientLength = RegistrationUtilities.checkUsernameLength(s.length());
                noIllegalCharacters = RegistrationUtilities.checkUsernameAllowedCharacters(s.toString());

                if(!sufficientLength) {
                    RegistrationErrorDisplay.displayError(new InvalidLengthError(R.string.error_username_invalid_length), usernameErrorView);
                    GeneralTools.setViewBackgroundTint(activityContext, username, error_color);
                } else {
                    usernameErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, username, regular_color);
                }

                if(!noIllegalCharacters) {
                    RegistrationErrorDisplay.displayError(new InvalidCharacterError(R.string.error_username_invalid_character), usernameErrorView);
                    GeneralTools.setViewBackgroundTint(activityContext, username, error_color);
                } else if (sufficientLength) {
                    usernameErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, username, regular_color);
                }

                if(sufficientLength == true && noIllegalCharacters == true) {
                    System.out.println("[Neuron.NC.Registration.setListeners]: The specified username (" + s.toString() + ") is of sufficient length and contains no illegal characters. Setting eligibleUsername in RegistrationEligibility to true.");
                    registrationEligibility.setEligibleUsername(true);
                } else {
                    System.out.println("[Nueron.NC.Registration.setListeners]: The speecified username (" + s.toString() + ") isn't eligible! Setting eligibleUsername in RegistrationEligibility to FALSE.");
                    registrationEligibility.setEligibleUsername(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //check password length
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean sufficientLength = RegistrationUtilities.checkPasswordSufficientLength(password.getText().toString());

                if(!sufficientLength) {
                    RegistrationErrorDisplay.displayError(new InvalidLengthError(R.string.error_password_invalid_length), passwordErrorView);
                    GeneralTools.setViewBackgroundTint(activityContext, password, error_color);
                } else {
                    passwordErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, password, regular_color);
                }

                registrationEligibility.setPasswordSufficientLength(sufficientLength);
                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //check password length (of both password fields) and password matching
        repeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //password length check
                registrationEligibility.setPasswordSufficientLength(RegistrationUtilities.checkPasswordSufficientLength(password.getText().toString()));
                registrationEligibility.setPasswordSufficientLength(RegistrationUtilities.checkPasswordSufficientLength(repeatPassword.getText().toString()));

                //password match check
                System.out.println("[Neuron.NC.Registration.setListeners]: Starting to match password '" + password.getText().toString() + "' with repeated password '" + s.toString() + "'.");
                boolean passwordsMatch = StringUtilities.compare(s.toString(), password.getText().toString());

                if(passwordsMatch == true) {
                    System.out.println("[Neuron.NC.Registration.setListeners]: Passwords match. Setting matchingPassword in RegistrationEligibility to true.");
                    registrationEligibility.setMatchingPassword(true);

                    repeatPasswordErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, repeatPassword, regular_color);
                } else {
                    System.out.println("[Neuron.NC.Registration.setListeners]: Passwords don't match!. Setting matchingPassword in RegistrationEligibility to FALSE!");
                    registrationEligibility.setMatchingPassword(false);

                    RegistrationErrorDisplay.displayError(new PasswordsDontMatchError(R.string.password_dont_match), repeatPasswordErrorView);
                    GeneralTools.setViewBackgroundTint(activityContext, repeatPassword, error_color);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //check email eligibility (only that the email field isn't empty and that it contains an @ symbol)
        //todo: display no error if the email edittext is empty
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(GeneralTools.checkEditTextNotEmpty(email) && GeneralTools.checkEditTextFor(email, '@')) { //if it isn't empty and if it contains @
                    System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user's email is not empty and contains an @ symbol.");
                    registrationEligibility.setEligibleEmail(true);

                    emailErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, email, regular_color);
                } else {
                    System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user's email is invalid.");
                    registrationEligibility.setEligibleEmail(false);

                    RegistrationErrorDisplay.displayError(new InvalidEmailError(R.string.error_invalid_email), emailErrorView);
                    GeneralTools.setViewBackgroundTint(activityContext, email, error_color);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //if any, either male or female radio button are checked, let sex in RegistrationEligibility be true
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user selected male sex.");
                    registrationEligibility.setEligibleSex(true);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user selected female sex.");
                    registrationEligibility.setEligibleSex(true);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }
        });

        //check birthday eligibility (day, month, year)
        final Switcher dayEligibility = new Switcher(false);
        final Switcher monthEligibility = new Switcher(false);
        final Switcher yearEligibility = new Switcher(false);

        //todo: display no error if the day edittext is empty
        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nthn
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean eligibleDay = RegistrationUtilities.checkBirthdayDay(s);

                if(eligibleDay == true) {
                    dayErrorView.setText("");
                    GeneralTools.setViewBackgroundTint(activityContext, day, regular_color);
                } else {
                    RegistrationErrorDisplay.displayError(new InvalidLengthError(R.string.error_invalid_day), dayErrorView);
                    GeneralTools.setViewBackgroundTint(activityContext, day, error_color);
                }

                dayEligibility.set(eligibleDay);

                if (dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user's birthday is now fully eligible!");
                    registrationEligibility.setEligibleBirthday(true);
                } else {
                    registrationEligibility.setEligibleBirthday(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nthn
            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //when the first item is to be selected, the user won't ever have the ability to select the prompt again and therefore the month eligibility is true
                System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user has set " + month.getSelectedItem().toString() + " as their month of birth.") ;
                monthEligibility.set(true);

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user's birthday is now fully eligible!");
                    registrationEligibility.setEligibleBirthday(true);
                } else {
                    registrationEligibility.setEligibleBirthday(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nthn
            }
        });

        //todo: switch the order of the years in this spinner
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //when the first item has been selected, the user won't ever have the ability to select the prompt again and therefore the year eligibility is true
                System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user has set " + year.getSelectedItem().toString() + " as their year of birth.");
                yearEligibility.set(true);

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user's birthday is now fully eligible!");
                    registrationEligibility.setEligibleBirthday(true);
                } else {
                    registrationEligibility.setEligibleBirthday(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nthn
            }
        });

        //check whether the user has accepted Terms of Service and Privacy Policy
        acceptTermsAndPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    System.out.println("[Neuron.NC.registration.Registration.setListeners]: The user has accepted Terms of Service and Privacy Policy!");
                    registrationEligibility.setEligibleAcceptance(true);
                } else {
                    registrationEligibility.setEligibleAcceptance(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButton);
            }
        });
    }

    public void setupRegisterButton(final Button registerButton, final Activity currentActivity, final Class targetActivityClass, final FirebaseAuth fbAuth) {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("[Neuron.NC.registration.Registration.setupRegisterButton]: Register button has been clicked! Attempting to register user...");
                registeredUser = RegistrationUtilities.createRegisteredUser(firstName, lastName, username, password, email, male, month, day, year);

                try {
                    RegistrationNetworking.registerUser(currentActivity, targetActivityClass, fbAuth, registeredUser);
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    System.out.println("[Neuron.NC.registration.Registration.setupRegisterButton]: The email specified " + registeredUser.getEmail() + " is INVALID!");
                }
             }
        });
    }


    /**
     * Initializes all of the views that are used for displaying errors during the registration process (ie, too short name, etc). The reason for this
     * to be decoupled from the constructor is mainly readability and scalability.
     * @param firstNameErrorView
     * @param lastNameErrorView
     * @param usernameErrorView
     * @param passwordErrorView
     * @param repeatPasswordErrorView
     * @param emailErrorView
     * @param sexErrorView
     * @param dayErrorView
     * @param acceptErrorView
     */
    public void initalizeErrorDisplayViews(TextView firstNameErrorView, TextView lastNameErrorView, TextView usernameErrorView, TextView passwordErrorView,
                                           TextView repeatPasswordErrorView, TextView emailErrorView, TextView sexErrorView, TextView dayErrorView, TextView acceptErrorView) {
        System.out.println("\"[Neuron.NC.registration.Registration.initializeErrorDisplayViews]: Initializing view that will be used for displaying errors.");
        this.firstNameErrorView = firstNameErrorView;
        this.lastNameErrorView = lastNameErrorView;
        this.usernameErrorView = usernameErrorView;
        this.passwordErrorView = passwordErrorView;
        this.repeatPasswordErrorView = repeatPasswordErrorView;
        this.emailErrorView = emailErrorView;
        this.sexErrorView = sexErrorView;
        this.dayErrorView = dayErrorView;
        this.acceptErrorView = acceptErrorView;
    }
}
