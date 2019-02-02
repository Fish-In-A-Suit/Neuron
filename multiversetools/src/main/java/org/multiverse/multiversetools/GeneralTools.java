package org.multiverse.multiversetools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.signin.SignIn;

import org.multiverse.multiversetools.alternative.SpinnerWithTitle;
import org.multiverse.utilities.ArrayUtilities;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import stringUtilities.StringUtilities;

public class GeneralTools {

    /**
     * Starts the specified activity using the view-based context, which can be queried by findViewById(R.id.ID_NAME).getContext().
     * For example, here is the process to switch from class LoginActivity to MainActivity:
     *   - declare id for LoginActivity inside the xml dofiniton of it's layout: android:id="@+id/login_activity"
     *   - specify view-based context for login activity as the context parameter to this method: findViewById(R.id.login_activity).getContext()
     *   - specify the target activity class
     *   - done.
     *
     * This method requires setting no flags to the intent object and is more memory-friendly.
     * @param context The context of a view
     * @param target The class of the activity to start
     */
    public static void launchNewActivity(Context context, Class target) {
        Intent intent = new Intent(context, target);
        context.startActivity(intent);
    }

    /**
     * Launches the specified activity from the global context by setting FLAG_ACTIVITY_NEW_TASK to the
     * intent object. The context is queried by getApplicationContext()
     * @param context The global application context
     * @param target The class of the activity to start
     */
    public static void launchNewActivityFromGlobalContext(Context context, Class target) {
        Intent intent = new Intent(context, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //this flag enables the to call this intent by startActivity() outside of the application context
        context.startActivity(intent);
    }

    /**
     * Changes the colour of the status bar of this activity to the specified colour by the colorId (this should be the pointer to the color in resources)
     * @param activity The activity whose status bar colour to change
     * @param colorId The id in res/values/color (queried by R.color.COLOR_NAME)
     */
    public static void changeStatusBarColor(Activity activity, int colorId) {
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(colorId));
    }

    /**
     * Changes the color of the bottom navigation bar (ie where the home button, back button and show other application buttons are)
     * @param activity
     * @param colorId
     */
    public static void changeNavigationBarColor(Activity activity, int colorId) {
        activity.getWindow().setNavigationBarColor(activity.getResources().getColor(colorId));
    }

    /**
     * Removes the status bar of the specified activity
     * @param activity The activity whose status bar to remove
     */
    public static void removeStatusBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Removes the action bar of the specified activity
     * @param activity The activity whose action bar to remove
     */
    public static void removeActionBar(Activity activity) {
        if(null!=activity.getActionBar()) {
            activity.getActionBar().hide();
        } else {
            System.err.println("Action bar for activity " + activity.toString() + " couldn't be found!");
        }
    }

    /**
     * Sets the switch color for a radio button.
     * @param rb The radio button to set switching colors to
     * @param unchecked The color (from org.multiverse.multiversetools.Color) which should be used when the radio button is unchecked
     * @param checked The color (from org.multiverse.multiversetools.Color) which should be used when the radio button is checked
     */
    public static void setRadioButtonOnSelectedColor(RadioButton rb, org.multiverse.multiversetools.Color unchecked, org.multiverse.multiversetools.Color checked) {
        ColorStateList csl = new ColorStateList(
                new int[][] {
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[] {
                        Color.rgb(unchecked.getR(), unchecked.getG(), unchecked.getB()),
                        Color.rgb(checked.getR(), checked.getG(), checked.getB())
                }
        );

        rb.setButtonTintList(csl);
    }

    /**
     * Sets the color of the chechbox when selected (checked) and when unselected (unchecked)
     * @param cb
     * @param unchecked
     * @param checked
     */
    public static void setCheckboxOnSelectedColor(CheckBox cb, org.multiverse.multiversetools.Color unchecked, org.multiverse.multiversetools.Color checked) {
        ColorStateList csl = new ColorStateList(
                new int[][] {
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[] {
                        Color.rgb(unchecked.getR(), unchecked.getG(), unchecked.getB()),
                        Color.rgb(checked.getR(), checked.getG(), checked.getB())
                }
        );

        cb.setButtonTintList(csl);
    }

    /**
     * Queries an array from R.array.__________ and returns it as a String array
     * @param c The context ... type "this" when calling and this does the trick
     * @param id For example: R.array.years (make sure it is defines as an array-list in strings.xml
     */
    public static ArrayList<String> getArrayFromResources(Context c, int id) {
        System.out.println("[Neuron.MT.GeneralTools.getArrayFromResources]: Extracting string array from id " + id);
        ArrayUtilities.displayArray(c.getResources().getStringArray(id));
        return StringUtilities.stringToArrayList(c.getResources().getStringArray(id));
    }

    /**
     * Checks whether the specified EditText is or isn't empty
     * @param et
     * @return True if the specified EditText isn't empty or false if it's empty
     */
    public static boolean checkEditTextNotEmpty(EditText et) {
        if(et.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the text withing EditText contains the specified character
     * @param et
     * @param c
     * @return True, if text in EditText contains the specified character or false if it doesn't
     */
    public static boolean checkEditTextFor(EditText et, Character c) {
        String text = et.getText().toString();

        if(text.contains(c.toString())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Changes the background tint of the specified view in the specified context to the color specified by the id (R.color.__________)
     * @param c
     * @param v
     * @param colorId
     */
    public static void setViewBackgroundTint(Context c, View v, int colorId) {
        DrawableCompat.setTint(v.getBackground(), ContextCompat.getColor(c, colorId));
    }

    /**
     * Sets the text of a google sign-in button to the specified text
     * @param btn
     * @param text
     */
    public static void setGoogleButtonText(SignInButton btn, String text) {
        //find the TextView that is inside SignInButton and set it's text
        for(int i = 0; i<btn.getChildCount(); i++) {
            View v = btn.getChildAt(i);

            if(v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(text);
            }
        }
    }

    /**
     * Sets the text size, font family and the style of the font of the specified sign in button
     * @param signInButton
     * @param textSize The size of the text size
     * @param fontName The name of the font (ex sans-serif-light)
     * @param typeFaceStyle One of Typeface.BOLD, TypeFace.ITALIC, Typeface.NORMAL, Typeface.BOLD_ITALIC, etc
     */
    public static void setGoogleButtonTextProperties(SignInButton signInButton, int textSize, String fontName, int typeFaceStyle, Padding padding) {
        for(int i = 0; i<signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if(v instanceof  TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(textSize);
                tv.setTypeface(Typeface.create(fontName, typeFaceStyle));

                //64,12,0,20
                tv.setPadding(padding.left, padding.top, padding.right, padding.bottom);
            }
        }
    }

    //todo: finish
    public static void setGoogleButtonBackgroundDrawable(SignInButton signInButton, int drawableLocation) {
        for(int i = 0; i<signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
        }
    }

    public static void inspectGoogleBtn(SignInButton signInButton) {
        System.out.println("[Neuron.MT.GeneralTools.inspectGoogleBtn]: The specified sign in button had " + signInButton.getChildCount() + " children.");

        if(Button.class.isAssignableFrom(SignInButton.class)) {
            System.out.println("[Neuron.MT.GeneralTools.inspectGoogleBtn]: SignInButton is a subclass of Button");
        } else if (ImageButton.class.isAssignableFrom(SignInButton.class)) {
            System.out.println("[Neuron.MT.GeneralTools.inspectGoogleBtn]: SignInButton is a subclass of ImageButton");
        }

        for(int i = 0; i<signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if(v instanceof TextView) {
                System.out.println("[Neuron.MT.GeneralTools.inspectGoogleBtn]: Found a TextView child");
            } else if (v instanceof ImageView) {
                System.out.println("[Neuron.MT.GeneralTools.inspectGoogleBtn]: Found an ImageView child");
            }
        }

        System.out.println("[Neuron.MT.GeneralTools.inspectGoogleBtn]: Comprises of " + signInButton.getDrawableState().length + " drawables.");

        System.out.println("[Neuron.MT.GeneralTools.inspectGoogleBtn]: Button background: " + signInButton.getBackground());
    }

    /**
     * Sets the margins of between the elements of the specified tab layout by the specified values
     * @param tabLayout
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setTabLayoutMargins(TabLayout tabLayout, int left, int top, int right, int bottom) {
        for(int i=0; i<tabLayout.getTabCount() - 1; i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            tab.requestLayout();
        }
    }

    /**
     * Populates the specified Spinner with the values from arrayId specified, using the default android provided layouts to display elements
     * @param context
     * @param spinner
     * @param arrayId
     */
    public static void populateSpinner(Context context, Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Sets the maximum length of characters allowed by the specified TextView
     * @param v
     * @param length
     */
    public static void setMaxTextViewLength(TextView v, int length) {
        InputFilter[] filter = new InputFilter[1];
        filter[0] = new InputFilter.LengthFilter(length);
        v.setFilters(filter);
    }

    /**
     * This method sets the specified count of offscreen pages to one less than the amount of "pages" the viewpager contains.
     * This prevents the viewpager from "dumping" obsolete pages and their state.
     * @param vp
     */
    public static void makeViewPagerNotForget(ViewPager vp) {
        vp.setOffscreenPageLimit(vp.getAdapter().getCount()-1);
    }

    /**
     * @param vp
     * @return A list of all of the fragments associated with the specified instance of ViewPager
     */
    public static List<Fragment> findFragmentsInViewPager(ViewPager vp) {
        List<Fragment> allFragments = new ArrayList<>();
        for(int i = 0; i<vp.getAdapter().getCount(); i++) {
            FragmentPagerAdapter fpa = (FragmentPagerAdapter) vp.getAdapter();
            Fragment f = fpa.getItem(i);

            System.out.println("[Neuron.Login.onActivityResult]: Adding fragment " + f + " to the list of all fragments");
            allFragments.add(f);
        }

        return allFragments;
    }


}
