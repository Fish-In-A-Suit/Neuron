package org.multiverse;


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

import org.multiverse.multiversetools.ButtonGroup;
import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.multiversetools.IntKeeper;
import org.multiverse.multiversetools.StringKeeper;
import org.multiverse.multiversetools.ViewTab;
import org.tord.neuroncore.Constants;
import org.tord.neuroncore.database.DatabaseUser;
import org.tord.neuroncore.error.registrationerror.InvalidCharacterError;
import org.tord.neuroncore.error.registrationerror.InvalidLengthError;
import org.tord.neuroncore.error.registrationerror.RegistrationErrorDisplay;
import org.tord.neuroncore.registration.Birthday;
import org.tord.neuroncore.registration.RegistrationEligibility;
import org.tord.neuroncore.registration.RegistrationUtilities;
import org.tord.neuroncore.registration.Sex;

import java.util.ArrayList;

import core.Switcher;

/**
 * This class is responsible for managing the tabs if the user is logging in the app for the first time using google.
 */
public class AfterGoogleSignUpManager {
    private Context context;
    private ViewTab viewTab; //the instance that holds the viewpager and tablayout connected

    private DatabaseUser databaseUser;

    private RegistrationEligibility registrationEligibility; //to check if the user is eligible for registration

    private UsernameTab usernameTab;
    private SexTab sexTab;
    private BirthdayTab birthdayTab;

    private ButtonGroup registerButtons; //several register buttons are used: one in UsernameTab, one in SexTab and one in BirthdayTab

    private int numTabSwitches; //switch only at the beginning the first two tabs by default, then after tht the user has to switch.

   //TODO: ADD ANIMATED HIDDEN ICONS AT THE TOP OF EACH OF THE FRAGMENTS, WHICH ARE GREENISH CIRCLES WITH A TICK AND ANIMATE WHEN THE USER SUCCESSFULLY FILLS OUT THEIR INFO

    public AfterGoogleSignUpManager(Context context, ViewTab viewTab, UsernameTab usernameTab, SexTab sexTab, BirthdayTab birthdayTab, DatabaseUser databaseUser) {
        System.out.println("[Neuron.AfterGoogleSignUpManager]: Creating instance... UsernameTab = " + usernameTab + ", SexTab = " + sexTab + ", BirthdayTab = " + birthdayTab);

        this.context = context;

        this.viewTab = viewTab;
        this.usernameTab = usernameTab;
        this.sexTab = sexTab;
        this.birthdayTab = birthdayTab;

        this.databaseUser = databaseUser;

        configureRegisterButtons();
    }

    /**
     * Call this method to start waiting for events
     */
    public void start() {
        System.out.println("[Neuron.AfterGoogleSignUpManager.start]: Starting to log for info!");

        startLoggingUsernameTab();
        startLoggingSexTab();
        startLoggingBirthdayTab();

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
                    databaseUser.setUsername(username);
                    registrationEligibility.setEligibleUsername(true);
                    errorText.setText("");
                    GeneralTools.setViewBackgroundTint(context, usernameEditText, R.color.default_color);

                } else {
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
        //keeps tracking whether the user has specified their sexssss
        sexTab.getMaleButton().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sexTab.getMaleButton().isChecked() || sexTab.getFemaleButton().isChecked()) {
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Sex is eligible.");

                    if(sexTab.getMaleButton().isChecked()) {
                        databaseUser.setSex(Sex.MALE);
                    } else {
                        databaseUser.setSex(Sex.FEMALE);
                    }

                    registrationEligibility.setEligibleSex(true);
                } else {
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Sex is NOT eligible.");
                    registrationEligibility.setEligibleSex(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
            }
        });

        sexTab.getFemaleButton().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sexTab.getMaleButton().isChecked() || sexTab.getFemaleButton().isChecked()) {
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Sex is eligible.");

                    if(sexTab.getMaleButton().isChecked()) {
                        databaseUser.setSex(Sex.MALE);
                    } else {
                        databaseUser.setSex(Sex.FEMALE);
                    }

                    registrationEligibility.setEligibleSex(true);
                } else {
                    System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingSexTab]: Sex is NOT eligible.");
                    registrationEligibility.setEligibleSex(false);
                }

                RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
            }
        });

    }

    private void startLoggingBirthdayTab() {
        final StringKeeper monthValue = new StringKeeper("");
        final IntKeeper dayValue = new IntKeeper(-1);
        final IntKeeper yearValue = new IntKeeper(-1);

        final Switcher dayEligibility = new Switcher(false);
        final Switcher monthEligibility = new Switcher(false);
        final Switcher yearEligibility = new Switcher(false);

        birthdayTab.getDaySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("[Neuron.AfterGoogleSignUpManager.startLoggingBirthdayTab]: The user has successfully specified the day of their birthday");

                try {
                    dayValue.setValue(Integer.parseInt(birthdayTab.getDaySpinner().getSelectedItem().toString()));
                } catch (NumberFormatException e) {
                    System.err.println("[Neuron.AfterGoogleSignUpManager.startLoggingBirthdayTab]: The day of the birthday cannot be parsed! Value: " + birthdayTab.getDaySpinner().getSelectedItem().toString());
                }


                dayEligibility.set(true);

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
                } else {
                    registrationEligibility.setEligibleBirthday(false);
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

                monthValue.setValue(birthdayTab.getMonthSpinner().getSelectedItem().toString());

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
                } else {
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

                try {
                    yearValue.setValue(Integer.parseInt(birthdayTab.getYearSpinner().getSelectedItem().toString()));
                } catch (NumberFormatException e) {
                    System.err.println("[Neuron.AfterGoogleSignUpManager.startLoggingBirthdayTab]: The year couldn't be parsed properly.");
                }

                if(dayEligibility.get() && monthEligibility.get() && yearEligibility.get()) {
                    databaseUser.setBirthday(new Birthday(monthValue.getValue(), dayValue.getValue(), yearValue.getValue()));
                    registrationEligibility.setEligibleBirthday(true);
                    RegistrationUtilities.tryToEnableRegistration(registrationEligibility, registerButtons);
                } else {
                    registrationEligibility.setEligibleBirthday(false);
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
     * Each register button responds in the same way: it registers the user's additional info to the database and then starts the main activity.
     */
    private void setRegisterButtonsOnClickListeners() {
        for(Button b : registerButtons.getAsList()) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUserToDatabase();
                    updateUI();
                }
            });
        }

    }

    private void registerUserToDatabase() {
        databaseUser.sendDataToDatabase();
    }

    private void updateUI() {
        GeneralTools.launchNewActivity(context, MainActivity.class);
    }
}
