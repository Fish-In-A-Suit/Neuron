package org.multiverse.registration.firstTimeGoogleSignup;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.multiverse.registration.RegistrationUtilities;
import org.multiverse.registration.firstTimeGoogleSignup.BirthdayTab;
import org.multiverse.login.LoginActivity;
import org.multiverse.MainActivity;
import org.multiverse.R;
import org.multiverse.registration.firstTimeGoogleSignup.SexTab;
import org.multiverse.registration.firstTimeGoogleSignup.UsernameTab;
import org.multiverse.database.DatabaseNetworking;
import org.multiverse.multiversetools.AnimationTools;
import org.multiverse.multiversetools.ButtonGroup;
import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.multiversetools.valuekeepers.IntKeeper;
import org.multiverse.multiversetools.valuekeepers.StringKeeper;
import org.multiverse.multiversetools.ViewTab;
import org.tord.neuroncore.Constants;
import org.multiverse.database.DatabaseUser;
import org.tord.neuroncore.registrationerror.InvalidCharacterError;
import org.tord.neuroncore.registrationerror.InvalidLengthError;
import org.tord.neuroncore.registrationerror.RegistrationErrorDisplay;
import org.tord.neuroncore.registration.Birthday;
import org.tord.neuroncore.registration.RegistrationEligibility;
import org.tord.neuroncore.registration.Sex;

import java.util.ArrayList;

import core.Switcher;

/**
 * This class is responsible for managing the tabs if the user is logging in the app for the first time using google.
 */
public class AfterGoogleSignUpManager {
    private LoginActivity loginActivity;
    private Context context;
    private ViewTab viewTab; //the instance that holds the viewpager and tablayout connected

    private DatabaseUser databaseUser;

    private RegistrationEligibility registrationEligibility; //to check if the user is eligible for registration

    private UsernameTab usernameTab;
    private SexTab sexTab;
    private BirthdayTab birthdayTab;

    private ButtonGroup registerButtons; //several register buttons are used: one in UsernameTab, one in SexTab and one in BirthdayTab

    private int numTabSwitches; //switch only at the beginning the first two tabs by default, then after tht the user has to switch.

    //these switchers are used to prevent the tick animations from looping over and over again
    private Switcher birthdayTabEligibleBefore;
    private Switcher sexTabEligibleBefore;
    private Switcher usernameTabEligibleBefore;

    public AfterGoogleSignUpManager(LoginActivity la, Context context, ViewTab viewTab, UsernameTab usernameTab, SexTab sexTab, BirthdayTab birthdayTab, DatabaseUser databaseUser) {
        System.out.println("[Neuron.AfterGoogleSignUpManager]: Creating instance... UsernameTab = " + usernameTab + ", SexTab = " + sexTab + ", BirthdayTab = " + birthdayTab);

        loginActivity = la;
        this.context = context;

        this.viewTab = viewTab;
        this.usernameTab = usernameTab;
        this.sexTab = sexTab;
        this.birthdayTab = birthdayTab;

        this.databaseUser = databaseUser;

        birthdayTabEligibleBefore = new Switcher(false);
        sexTabEligibleBefore = new Switcher(false);
        usernameTabEligibleBefore = new Switcher(false);

        configureRegisterButtons();

        registrationEligibility = new RegistrationEligibility();
        //these all are catered for by google
        registrationEligibility.setEligibleFirstName(true);
        registrationEligibility.setEligibleLastName(true);
        registrationEligibility.setPasswordSufficientLength(true);
        registrationEligibility.setMatchingPassword(true);
        registrationEligibility.setEligibleEmail(true);
        //todo: implement a way for the user to accept license agreement
        registrationEligibility.setEligibleAcceptance(true);
    }

    /**
     * Call this method to start waiting for events
     */
    public void start() {
        System.out.println("[Neuron.AfterGoogleSignUpManager.start]: Starting to log for info!");

        startLoggingUsernameTab();
        startLoggingSexTab();
        startLoggingBirthdayTab();

        System.out.println("[Neuron.AfterGoogleSignUpManager.start]: Making the viewtab visible!");
        viewTab.unhide();

    }

    private void startLoggingUsernameTab() {
        System.out.println("[Neuron.AfterGoogleSignUpManager.startLogginUsernameTab]: Starting to log username tab for info.");
        final EditText usernameEditText = usernameTab.getUsernameEditText();
        final TextView errorText = usernameTab.getErrorTextView();

        System.out.println("[Neuron.AfterGoogleSignUpManager.startLogginUsernameTab]: usernameEditText = " + usernameEditText + " | errorText = " + errorText);

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
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingUsernameTab]: First name is of sufficient length and doesn't contain any illegal characters! Setting eligibleUsername in RegistrationEligibility to true.");
                    //databaseUser.setUsername(username);
                    registrationEligibility.setEligibleUsername(true);

                    //if the state of username wasn't eligible before (false), then play the animation. Otherwise don't. This prevents continuous looping of the tick animation at each successful entry to the username field
                    if(usernameTabEligibleBefore.get() != true) {
                        usernameTab.getTickView().setVisibility(View.VISIBLE);
                        AnimationTools.startAnimation(usernameTab.getTickView());
                    }

                    errorText.setText("");
                    GeneralTools.setViewBackgroundTint(context, usernameEditText, R.color.default_color);
                    usernameTabEligibleBefore.set(true);
                } else {
                    if(usernameTabEligibleBefore.get() == true) {
                        usernameTab.getTickView().setVisibility(View.INVISIBLE);
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

                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingUsernameTab]: First name doesn't match the requirements... sufficientLength = " + sufficientLength + ", noIllegalCharacters = " + onlyAllowedCharacters + ".");
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

    private void startLoggingSexTab() {
        System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Started logging sex tab.");
        //keeps tracking whether the user has specified their sex
        sexTab.getMaleButton().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sexTab.getMaleButton().isChecked() || sexTab.getFemaleButton().isChecked()) {
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Sex is eligible.");

                    /*
                    if(sexTab.getMaleButton().isChecked()) {
                        databaseUser.setSex(Sex.MALE);
                    } else {
                        databaseUser.setSex(Sex.FEMALE);
                    }
                    */

                    sexTab.getTickView().setVisibility(View.VISIBLE);

                    if(sexTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(sexTab.getTickView());
                    }

                    registrationEligibility.setEligibleSex(true);
                    sexTabEligibleBefore.set(true);
                } else {
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Sex is NOT eligible.");
                    registrationEligibility.setEligibleSex(false);

                    if(sexTabEligibleBefore.get() == true) {
                        sexTab.getTickView().setVisibility(View.INVISIBLE);
                    }
                    sexTabEligibleBefore.set(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
            }
        });

        sexTab.getFemaleButton().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sexTab.getMaleButton().isChecked() || sexTab.getFemaleButton().isChecked()) {
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Sex is eligible.");

                    /*
                    if(sexTab.getMaleButton().isChecked()) {
                        databaseUser.setSex(Sex.MALE);
                    } else {
                        databaseUser.setSex(Sex.FEMALE);
                    }
                    */

                    sexTab.getTickView().setVisibility(View.VISIBLE);

                    if(sexTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(sexTab.getTickView());
                    }

                    registrationEligibility.setEligibleSex(true);
                    sexTabEligibleBefore.set(true);
                } else {
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Sex is NOT eligible.");
                    registrationEligibility.setEligibleSex(false);

                    if(sexTabEligibleBefore.get() == true) {
                        sexTab.getTickView().setVisibility(View.INVISIBLE);
                    }
                    sexTabEligibleBefore.set(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
            }
        });

    }

    private void startLoggingBirthdayTab() {
        System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Started logging birthday tab.");

        /*
        final StringKeeper monthValue = new StringKeeper("");
        final IntKeeper dayValue = new IntKeeper(-1);
        final IntKeeper yearValue = new IntKeeper(-1);
        */

        final Switcher dayEligibility = new Switcher(false);
        final Switcher monthEligibility = new Switcher(false);
        final Switcher yearEligibility = new Switcher(false);

        birthdayTab.getDaySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingBirthdayTab]: The user has successfully specified the day of their birthday");

                /*
                try {
                    dayValue.setValue(Integer.parseInt(birthdayTab.getDaySpinner().getSelectedItem().toString()));
                } catch (NumberFormatException e) {
                    System.err.println("[Neuron.AfterGoogleSignUpManager.startLoggingBirthdayTab]: The day of the birthday cannot be parsed! Value: " + birthdayTab.getDaySpinner().getSelectedItem().toString());
                }
                */


                dayEligibility.set(true);

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    //databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);

                    birthdayTab.getTickView().setVisibility(View.VISIBLE);

                    if(birthdayTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(birthdayTab.getTickView());
                    }

                    birthdayTabEligibleBefore.set(true);
                } else {
                    if(birthdayTabEligibleBefore.get() == true) {
                        birthdayTab.getTickView().setVisibility(View.INVISIBLE);
                    }
                    registrationEligibility.setEligibleBirthday(false);
                    birthdayTabEligibleBefore.set(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        birthdayTab.getMonthSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingBirthdayTab]: The user has successfully specified the month of their birthday");
                monthEligibility.set(true);

                //monthValue.setValue(birthdayTab.getMonthSpinner().getSelectedItem().toString());

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    //databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);

                    birthdayTab.getTickView().setVisibility(View.VISIBLE);

                    if(birthdayTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(birthdayTab.getTickView());
                    }

                    birthdayTabEligibleBefore.set(true);
                } else {
                    if(birthdayTabEligibleBefore.get() == true) {
                        birthdayTab.getTickView().setVisibility(View.INVISIBLE);
                    }
                    birthdayTabEligibleBefore.set(false);
                    registrationEligibility.setEligibleBirthday(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        birthdayTab.getYearSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingBirthdayTab]: The user has successfully specified the year of their birthday.");
                yearEligibility.set(true);

                /*
                try {
                    yearValue.setValue(Integer.parseInt(birthdayTab.getYearSpinner().getSelectedItem().toString()));
                } catch (NumberFormatException e) {
                    System.err.println("[Neuron.AfterGoogleSignUpManager.startLoggingBirthdayTab]: The year couldn't be parsed properly.");
                }
                */

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    //databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);

                    birthdayTab.getTickView().setVisibility(View.VISIBLE);

                    if(birthdayTabEligibleBefore.get() != true) {
                        AnimationTools.startAnimation(birthdayTab.getTickView());
                    }

                    birthdayTabEligibleBefore.set(true);
                } else {
                    if(birthdayTabEligibleBefore.get() == true) {
                        birthdayTab.getTickView().setVisibility(View.INVISIBLE);
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
        System.out.println("[Neuron.AfterGoogleSignUpManager.findRegisterButtons]: Finding register buttons");
        ArrayList<Button> registerButtonsList = new ArrayList<>();
        registerButtonsList.add(usernameTab.getRegisterButton());
        registerButtonsList.add(sexTab.getRegisterButton());
        registerButtonsList.add(birthdayTab.getRegisterButton());
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

    private void registerUserToDatabase() {
        System.out.println("[Neuron.AfterGoogleSignUpManager.registerUserToDatabase]: Trying to register user " + databaseUser.getFullName() + " to the database.");

        DatabaseNetworking.checkForEmailClashesAndSendToDatabase(databaseUser, this);
    }

    public void updateUI() {
        GeneralTools.launchNewActivity(context, MainActivity.class);
    }

    public Context getContext() {
        return context;
    }

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void notifyAboutEmailAlreadyInUse() {
        System.out.println("[Neuron.AfterGoogleSignUpManager.registerUserToDatabase]: Notifying the user about email already in use exception.");
        loginActivity.displayMsg(R.string.register_email_already_in_use);
    }

    /**
     * Builds a DatabaseUser instance from all of the fields specified using the specified email and fullname
     * @return
     */
    public DatabaseUser buildUser(String fullName, String email) {
        System.out.println("[Neuron.registration.AfterGoogleSignUpManager.buildUser]: Building a user instance using the specified fields...");
        Sex sex = null;

        if(sexTab.getMaleButton().isChecked() == true) {
            sex = Sex.MALE;
        } else if (sexTab.getFemaleButton().isChecked() == true) {
            sex = Sex.FEMALE;
        } else {
            System.out.println("[Neuron.registration.AfterGoogleSignUpManager.buildUser]: None of the buttons in sextab is checked! The sex for the user isn't specified!!!");
        }

        DatabaseUser user = new DatabaseUser(usernameTab.getUsernameEditText().getText().toString(), fullName, email, sex, birthdayTab.getBirthday());
        user.display();
        return user;
    }
}
