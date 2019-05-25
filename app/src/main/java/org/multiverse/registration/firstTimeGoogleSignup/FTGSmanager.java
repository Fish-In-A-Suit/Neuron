package org.multiverse.registration.firstTimeGoogleSignup;


import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.multiverse.registration.RegistrationUtilities;
import org.multiverse.login.LoginActivity;
import org.multiverse.MainActivity;
import org.multiverse.R;
import org.multiverse.database.DatabaseNetworking;
import org.multiverse.multiversetools.AnimationTools;
import org.multiverse.multiversetools.ButtonGroup;
import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.multiversetools.ViewTab;
import org.tord.neuroncore.Constants;
import org.multiverse.database.DatabaseUser;
import org.tord.neuroncore.registrationerror.InvalidCharacterError;
import org.tord.neuroncore.registrationerror.InvalidLengthError;
import org.tord.neuroncore.registrationerror.RegistrationErrorDisplay;
import org.tord.neuroncore.registration.RegistrationEligibility;
import org.tord.neuroncore.registration.Sex;

import java.util.ArrayList;

import core.Switcher;

/**
 * This class is responsible for managing the tabs if the user is logging in the app for the first time using google.
 */
public class FTGSmanager {
    private LoginActivity loginActivity;
    private Context context;
    private ViewTab viewTab; //the instance that holds the viewpager and tablayout connected

    private DatabaseUser databaseUser;

    private RegistrationEligibility registrationEligibility; //to check if the user is eligible for registration

    private UsernameFragment usernameFragment;
    private SexFragment sexFragment;
    private BirthdayFragment birthdayFragment;
    private PasswordFragment passwordFragment;

    private ButtonGroup registerButtons; //several register buttons are used: one in UsernameFragment, one in SexFragment and one in BirthdayFragment

    private int numTabSwitches; //switch only at the beginning the first two tabs by default, then after tht the user has to switch.

    //these switchers are used to prevent the tick animations from looping over and over again
    private Switcher birthdayTabEligibleBefore;
    private Switcher sexTabEligibleBefore;
    private Switcher usernameTabEligibleBefore;

    public FTGSmanager(LoginActivity la, Context context, ViewTab viewTab, UsernameFragment usernameFragment, PasswordFragment passwordFragment, SexFragment sexFragment, BirthdayFragment birthdayFragment, DatabaseUser databaseUser) {
        System.out.println("[Neuron.FTGSmanager]: Creating instance... UsernameFragment = " + usernameFragment + ", SexFragment = " + sexFragment + ", BirthdayFragment = " + birthdayFragment);

        loginActivity = la;
        this.context = context;

        this.viewTab = viewTab;
        this.usernameFragment = usernameFragment;
        this.passwordFragment = passwordFragment;
        this.sexFragment = sexFragment;
        this.birthdayFragment = birthdayFragment;

        this.databaseUser = databaseUser;

        birthdayTabEligibleBefore = new Switcher(false);
        sexTabEligibleBefore = new Switcher(false);
        usernameTabEligibleBefore = new Switcher(false);

        configureRegisterButtons();

        registrationEligibility = new RegistrationEligibility();
        //these all are catered for by google
        registrationEligibility.setEligibleFirstName(true);
        registrationEligibility.setEligibleLastName(true);
        //registrationEligibility.setPasswordSufficientLength(true);
        registrationEligibility.setMatchingPassword(true);
        registrationEligibility.setEligibleEmail(true);
        //todo: implement a way for the user to accept license agreement
        registrationEligibility.setEligibleAcceptance(true);
    }

    /**
     * Call this method to start waiting for events
     */
    public void start() {
        System.out.println("[Neuron.FTGSmanager.start]: Starting to log for info!");

        startLoggingUsernameTab();
        startLoggingPasswordTab();
        startLoggingSexTab();
        startLoggingBirthdayTab();

        System.out.println("[Neuron.FTGSmanager.start]: Making the viewtab visible!");
        viewTab.unhide();

    }

    private void startLoggingUsernameTab() {
        System.out.println("[Neuron.FTGSmanager.startLogginUsernameTab]: Starting to log username tab for info.");
        final EditText usernameEditText = usernameFragment.getUsernameEditText();
        final TextView errorText = usernameFragment.getErrorTextView();

        System.out.println("[Neuron.FTGSmanager.startLogginUsernameTab]: usernameEditText = " + usernameEditText + " | errorText = " + errorText);

        //todo: create a general method in some other class that configures an edit text with a set of rules ... also include corresponding error textview and which errors to include
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = s.toString();
                boolean onlyAllowedCharacters = RegistrationUtilities.checkUsernameAllowedCharacters(username);
                boolean sufficientLength = RegistrationUtilities.checkUsernameLength(s.length());

                if (sufficientLength == true && onlyAllowedCharacters == true) {
                    System.out.println("[Neuron.FTGSmanager.startLoggingUsernameTab]: First name is of sufficient length and doesn't contain any illegal characters! Setting eligibleUsername in RegistrationEligibility to true.");
                    //databaseUser.setUsername(username);
                    registrationEligibility.setEligibleUsername(true);

                    //if the state of username wasn't eligible before (false), then play the animation. Otherwise don't. This prevents continuous looping of the tick animation at each successful entry to the username field
                    if(usernameTabEligibleBefore.get() != true) {
                        usernameFragment.getTickView().setVisibility(View.VISIBLE);
                        AnimationTools.startAnimation(usernameFragment.getTickView());
                    }

                    errorText.setText("");
                    GeneralTools.setViewBackgroundTint(context, usernameEditText, R.color.default_color);
                    usernameTabEligibleBefore.set(true);
                } else {
                    if(usernameTabEligibleBefore.get() == true) {
                        usernameFragment.getTickView().setVisibility(View.INVISIBLE);
                    }

                    if(!sufficientLength) {
                        RegistrationErrorDisplay.displayError(new InvalidLengthError(R.string.error_first_name_invalid_length), errorText);
                        GeneralTools.setViewBackgroundTint(context, usernameEditText, R.color.error);
                    } else {
                        GeneralTools.setViewBackgroundTint(context, usernameEditText, R.color.default_color);
                        errorText.setText("");
                    }

                    if(!onlyAllowedCharacters) {
                        RegistrationErrorDisplay.displayError(new InvalidCharacterError(R.string.error_first_name_invalid_character), errorText);
                        GeneralTools.setViewBackgroundTint(context, usernameEditText, R.color.error);
                    } else if (sufficientLength) {
                        errorText.setText("");
                        GeneralTools.setViewBackgroundTint(context, usernameEditText, R.color.default_color);
                    }

                    System.out.println("[Neuron.FTGSmanager.startLoggingUsernameTab]: First name doesn't match the requirements... sufficientLength = " + sufficientLength + ", noIllegalCharacters = " + onlyAllowedCharacters + ".");
                    registrationEligibility.setEligibleUsername(false);
                    usernameTabEligibleBefore.set(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //if enter is pressed, move to the second fragment
        usernameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(numTabSwitches < Constants.AFTERGOOGLESIGNUP_MAXIMUM_TAB_SWITCHES_DEFAULT && keyCode == KeyEvent.KEYCODE_ENTER) {
                    viewTab.getViewPager().setCurrentItem(viewTab.getTabLayout().getSelectedTabPosition()+1);
                    numTabSwitches++;
                }

                return false;
            }
        });
    }

    private void startLoggingPasswordTab() {
        final EditText passwordField = passwordFragment.getPasswordField();
        final ImageView tickView = passwordFragment.getTickView();
        final TextView errorText = passwordFragment.getErrorText();
        final DatabaseUser dbUser = this.getDatabaseUser();

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean sufficientLength = RegistrationUtilities.checkPasswordSufficientLength(passwordField.getText().toString());

                if(!sufficientLength) {
                    errorText.setText(R.string.password_too_short);
                    tickView.setVisibility(View.INVISIBLE);

                } else {
                    dbUser.setPassword(s.toString());

                    errorText.setText("");

                    tickView.setVisibility(View.VISIBLE);
                    AnimationTools.startAnimation(tickView);
                }

                registrationEligibility.setPasswordSufficientLength(sufficientLength);
                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void startLoggingSexTab() {
        System.out.println("[Neuron.FTGSmanager.startLoggingSexTab]: Started logging sex tab.");
        //keeps tracking whether the user has specified their sex
        sexFragment.getMaleButton().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sexFragment.getMaleButton().isChecked() || sexFragment.getFemaleButton().isChecked()) {
                    System.out.println("[Neuron.FTGSmanager.startLoggingSexTab]: Sex is eligible.");

                    /*
                    if(sexFragment.getMaleButton().isChecked()) {
                        databaseUser.setSex(Sex.MALE);
                    } else {
                        databaseUser.setSex(Sex.FEMALE);
                    }
                    */

                    sexFragment.getTickView().setVisibility(View.VISIBLE);

                    if(sexTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(sexFragment.getTickView());
                    }

                    registrationEligibility.setEligibleSex(true);
                    sexTabEligibleBefore.set(true);
                } else {
                    System.out.println("[Neuron.FTGSmanager.startLoggingSexTab]: Sex is NOT eligible.");
                    registrationEligibility.setEligibleSex(false);

                    if(sexTabEligibleBefore.get() == true) {
                        sexFragment.getTickView().setVisibility(View.INVISIBLE);
                    }
                    sexTabEligibleBefore.set(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
            }
        });

        sexFragment.getFemaleButton().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sexFragment.getMaleButton().isChecked() || sexFragment.getFemaleButton().isChecked()) {
                    System.out.println("[Neuron.FTGSmanager.startLoggingSexTab]: Sex is eligible.");

                    /*
                    if(sexFragment.getMaleButton().isChecked()) {
                        databaseUser.setSex(Sex.MALE);
                    } else {
                        databaseUser.setSex(Sex.FEMALE);
                    }
                    */

                    sexFragment.getTickView().setVisibility(View.VISIBLE);

                    if(sexTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(sexFragment.getTickView());
                    }

                    registrationEligibility.setEligibleSex(true);
                    sexTabEligibleBefore.set(true);
                } else {
                    System.out.println("[Neuron.FTGSmanager.startLoggingSexTab]: Sex is NOT eligible.");
                    registrationEligibility.setEligibleSex(false);

                    if(sexTabEligibleBefore.get() == true) {
                        sexFragment.getTickView().setVisibility(View.INVISIBLE);
                    }
                    sexTabEligibleBefore.set(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
            }
        });

    }

    private void startLoggingBirthdayTab() {
        System.out.println("[Neuron.FTGSmanager.startLoggingSexTab]: Started logging birthday tab.");

        /*
        final StringKeeper monthValue = new StringKeeper("");
        final IntKeeper dayValue = new IntKeeper(-1);
        final IntKeeper yearValue = new IntKeeper(-1);
        */

        final Switcher dayEligibility = new Switcher(false);
        final Switcher monthEligibility = new Switcher(false);
        final Switcher yearEligibility = new Switcher(false);

        birthdayFragment.getDaySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("[Neuron.FTGSmanager.startLoggingBirthdayTab]: The user has successfully specified the day of their birthday");

                /*
                try {
                    dayValue.setValue(Integer.parseInt(birthdayFragment.getDaySpinner().getSelectedItem().toString()));
                } catch (NumberFormatException e) {
                    System.err.println("[Neuron.FTGSmanager.startLoggingBirthdayTab]: The day of the birthday cannot be parsed! Value: " + birthdayFragment.getDaySpinner().getSelectedItem().toString());
                }
                */


                dayEligibility.set(true);

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    //databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);

                    birthdayFragment.getTickView().setVisibility(View.VISIBLE);

                    if(birthdayTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(birthdayFragment.getTickView());
                    }

                    birthdayTabEligibleBefore.set(true);
                } else {
                    if(birthdayTabEligibleBefore.get() == true) {
                        birthdayFragment.getTickView().setVisibility(View.INVISIBLE);
                    }
                    registrationEligibility.setEligibleBirthday(false);
                    birthdayTabEligibleBefore.set(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        birthdayFragment.getMonthSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("[Neuron.FTGSmanager.startLoggingBirthdayTab]: The user has successfully specified the month of their birthday");
                monthEligibility.set(true);

                //monthValue.setValue(birthdayFragment.getMonthSpinner().getSelectedItem().toString());

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    //databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);

                    birthdayFragment.getTickView().setVisibility(View.VISIBLE);

                    if(birthdayTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(birthdayFragment.getTickView());
                    }

                    birthdayTabEligibleBefore.set(true);
                } else {
                    if(birthdayTabEligibleBefore.get() == true) {
                        birthdayFragment.getTickView().setVisibility(View.INVISIBLE);
                    }
                    birthdayTabEligibleBefore.set(false);
                    registrationEligibility.setEligibleBirthday(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        birthdayFragment.getYearSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("[Neuron.FTGSmanager.startLoggingBirthdayTab]: The user has successfully specified the year of their birthday.");
                yearEligibility.set(true);

                /*
                try {
                    yearValue.setValue(Integer.parseInt(birthdayFragment.getYearSpinner().getSelectedItem().toString()));
                } catch (NumberFormatException e) {
                    System.err.println("[Neuron.FTGSmanager.startLoggingBirthdayTab]: The year couldn't be parsed properly.");
                }
                */

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    //databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);

                    birthdayFragment.getTickView().setVisibility(View.VISIBLE);

                    if(birthdayTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(birthdayFragment.getTickView());
                    }

                    birthdayTabEligibleBefore.set(true);
                } else {
                    if(birthdayTabEligibleBefore.get() == true) {
                        birthdayFragment.getTickView().setVisibility(View.INVISIBLE);
                    }
                    registrationEligibility.setEligibleBirthday(false);
                    birthdayTabEligibleBefore.set(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configureRegisterButtons() {
        findRegisterButtons();
        setRegisterButtonsOnClickListeners();
    }

    /**
     * Finds all of the necessary register buttons (one per fragment) and defines initial parameters for registration eligibility
     */
    private void findRegisterButtons() {
        System.out.println("[Neuron.FTGSmanager.findRegisterButtons]: Finding register buttons");
        ArrayList<Button> registerButtonsList = new ArrayList<>();
        registerButtonsList.add(usernameFragment.getRegisterButton());
        registerButtonsList.add(sexFragment.getRegisterButton());
        registerButtonsList.add(birthdayFragment.getRegisterButton());
        registerButtons = new ButtonGroup(registerButtonsList);

    }

    /**
     * Each register button responds in the same way: it registers the user's additional info to the database and then starts the main activity.
     */
    private void setRegisterButtonsOnClickListeners() {
        for(Button b : registerButtons.getAsList()) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUserToDatabase();
                }
            });
        }

    }

    private DatabaseUser getDatabaseUser() {
        return databaseUser;
    }

    private void registerUserToDatabase() {
        System.out.println("[Neuron.FTGSmanager.registerUserToDatabase]: Trying to register user " + databaseUser.getFullName() + " to the database.");

        DatabaseNetworking.checkForEmailAndUsernameClashesAndSendToDatabase(databaseUser, this);
    }

    /**
     * Launches the main activity and stops login activity
     */
    public void updateUI() {
        GeneralTools.launchNewActivity(context, MainActivity.class);
        GeneralTools.stopActivity(loginActivity);
    }

    public Context getContext() {
        return context;
    }

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void notifyAboutEmailAlreadyInUse() {
        System.out.println("[Neuron.FTGSmanager.notifyAboutEmailAlreadyInUse]: Notifying the user about email already in use exception.");
        loginActivity.displayMsg(R.string.register_email_already_in_use);
    }

    public void notifyAboutUsernameAlreadyInUse() {
        System.out.println("[Neuron.FTGSmanager.notifyAboutUsernameAlreadyInUse]: Notifying the user about the username already being in use exception.");
        switchToUsernameTab();
        loginActivity.displayMsg(R.string.register_username_already_in_use);
    }

    /**
     * Builds a DatabaseUser instance from all of the fields specified using the specified email and fullname
     * @return
     */
    public DatabaseUser buildUser(String fullName, String email) {
        System.out.println("[Neuron.registration.FTGSmanager.buildUser]: Building a user instance using the specified fields...");
        Sex sex = null;

        if(sexFragment.getMaleButton().isChecked() == true) {
            sex = Sex.MALE;
        } else if (sexFragment.getFemaleButton().isChecked() == true) {
            sex = Sex.FEMALE;
        } else {
            System.out.println("[Neuron.registration.FTGSmanager.buildUser]: None of the buttons in sextab is checked! The sex for the user isn't specified!!!");
        }

        DatabaseUser user = new DatabaseUser(usernameFragment.getUsernameEditText().getText().toString(), fullName, email, sex, birthdayFragment.getBirthday());
        user.display();
        return user;
    }

    public String getUsername() {
        return usernameFragment.getUsernameEditText().getText().toString();
    }

    public void switchToUsernameTab() {
        viewTab.getViewPager().setCurrentItem(0);
    }
}
