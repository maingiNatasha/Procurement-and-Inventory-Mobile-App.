package com.example.prototype;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "Session";
    private static final String KEY_SUPPLIER_ID = "supplierId";
    private static final String KEY_FARMER_ID = "farmerId";
    private static final String KEY_USERNAME = "username";

    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_EMAIL = "email";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static SessionManager instance; // Singleton instance

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveSupplierId(int supplierId) {
        editor.putInt(KEY_SUPPLIER_ID, supplierId);
        editor.apply();
    }

    public int getSupplierId() {
        return sharedPreferences.getInt(KEY_SUPPLIER_ID, 0);
    }

    public void saveFarmerId(int farmerId) {
        editor.putInt(KEY_FARMER_ID, farmerId);
        editor.apply();
    }

    public int getFarmerId() {
        return sharedPreferences.getInt(KEY_FARMER_ID, 0);
    }

    public void saveUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void saveFullname(String fullname) {
        editor.putString(KEY_FULLNAME, fullname);
        editor.apply();
    }

    public String getFullname() {return sharedPreferences.getString(KEY_FULLNAME, "");}

    public void saveEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail() {return sharedPreferences.getString(KEY_EMAIL,"");}

    public void clearSession() {
        editor.remove(KEY_SUPPLIER_ID);
        editor.remove(KEY_FARMER_ID);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_FULLNAME);
        editor.remove(KEY_EMAIL);
        editor.apply();
    }
}


