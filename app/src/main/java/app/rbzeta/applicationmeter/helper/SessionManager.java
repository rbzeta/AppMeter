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
}
