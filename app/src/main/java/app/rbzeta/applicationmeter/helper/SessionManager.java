package app.rbzeta.applicationmeter.helper;

import android.content.Context;
import android.content.SharedPreferences;

import app.rbzeta.applicationmeter.app.AppConfig;

/**
 * Created by Robyn on 25/10/2016.
 */

public class SessionManager {
    private String TAG = SessionManager.class.getSimpleName();

    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    Context mContext;

    public SessionManager(Context context, int mode){
        mContext = context;
        preferences = mContext.getSharedPreferences(AppConfig.PREF_NAME,mode);
        editor = preferences.edit();
    }

    public String getUserName() {
        return preferences.getString(AppConfig.PREF_KEY_NAME,"");
    }

    public String getUserPhone(){
        return preferences.getString(AppConfig.PREF_KEY_PHONE_NUMBER,"");
    }

    public int getUserEmployeeId(){
        return preferences.getInt(AppConfig.PREF_KEY_EMPLOYEE_ID,0);
    }

    public void deleteSharedPreference(){
        editor.clear();
        editor.commit();
    }

    public void setUserName(String name) {
        editor.putString(AppConfig.PREF_KEY_NAME,name);
        editor.commit();
    }

    public void setUserPhone(String phone) {
        editor.putString(AppConfig.PREF_KEY_PHONE_NUMBER,phone);
        editor.commit();
    }

    public void setUserEmployeeId(int employeeId) {
        editor.putInt(AppConfig.PREF_KEY_EMPLOYEE_ID,employeeId);
        editor.commit();
    }
}
