package org.tord.neuroncore;

public class Constants {
    //used to correctly position the google maps watermark
    public static int GOOGLE_MAPS_WATERMARK_WIDTH = 240;
    public static int GOOGLE_MAP_WATERMARK_TOP_OFFSET = 330;

    public static int MARGIN_DEFAULT = 8;
    public static int MARGIN_SIXTEEN = 16;

    public static int MAIN_SEARCH_OFFSET = 4; //the offset used to set the padding of main_search edittext correctly

    public static String DATABASE_USER_DATA_LOCATION = "user_data"; //for storing information about the users
    public static String DATABASE_EMAILS_USED_LOCATION = "emails_used"; //for storing the already registrated emails
    public static String DATABASE_USERNAMES_USED_LOCATION = "usernames_used";

    public static int LOGIN_BIRTHDAY_TAB_MONTH_SPINNER_MAX_LENGTH = 3;

    public static int AFTERGOOGLESIGNUP_MAXIMUM_TAB_SWITCHES_DEFAULT = 2;

    //public static int RC_SIGN_IN_REGULAR = 100; //used during the sign-in process with email and password
    public static int RC_SIGN_IN_GOOGLE = 101; //used during the sign-in process with google account
}
