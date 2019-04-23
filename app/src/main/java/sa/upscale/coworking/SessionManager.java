package sa.upscale.coworking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

@SuppressLint("CommitPrefEdits")
public class SessionManager {

    public static final String user_Id = "uid";
    public static final String user_name = "uname";
    public static final String user_email = "uemail";
    public static final String user_mobile = "umobile";
    public static final String user_profile = "upofile";
    public static final String user_languageCode = "langcode";
    public static final String user_essal_cust_id = "essal_cust_id";

    public static final String user_langStatus = "status";
    public static final String selected_tag_id = "selected_tag_id";

    // Sharedpref file name
    private static final String PREF_NAME = "userdetail";
    // All Shared Preferences Keys-
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void  setLanguagestate(String status){

        try {
            editor.putString(user_languageCode,status);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public HashMap<String, String> getlanguageCode() {

        HashMap<String, String> lang = new HashMap<String, String>();
        lang.put(user_languageCode,pref.getString(user_languageCode,null));

        return lang;
    }

    public void createLoginSession(String u_id,String u_name, String u_email,String u_mobile,String profilePic,String langCOde,String essal_cust_id) {
        // Storing login value as TRUE
        try {


            editor.putBoolean(IS_LOGIN, true);
            // Storing name in pref
            editor.putString(user_Id, u_id);
            editor.putString(user_name, u_name);
            editor.putString(user_email, u_email);
            editor.putString(user_mobile, u_mobile);
            editor.putString(user_profile,profilePic);
            editor.putString(user_languageCode,langCOde);
            editor.putString(user_essal_cust_id,essal_cust_id);


            // commit changes
            editor.commit();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

	/**/

    /**
     * Check login method wil check user login status If false it will redirect
     * user to login page Else won't do anything
     */
    public int checkLogin() {
        // Check login status

        int flag;

        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            // Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            // _context.startActivity(i);

            flag = 0;
        } else {

            flag = 1;
        }

        return flag;
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(user_Id, pref.getString(user_Id, null));
        user.put(user_name, pref.getString(user_name, null));
        user.put(user_email, pref.getString(user_email, null));
        user.put(user_mobile, pref.getString(user_mobile, null));
        user.put(user_profile,pref.getString(user_profile,null));
        user.put(user_languageCode,pref.getString(user_languageCode,null));
        user.put(user_essal_cust_id,pref.getString(user_essal_cust_id,null));

        return user;
    }

    /**
     * Clear session details
     */
    public void clearUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, NavigationActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void  setTegid(String tagid){

        try {
            editor.putString(selected_tag_id,tagid);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTagid(){

        return pref.getString(selected_tag_id, null);
    }
}
