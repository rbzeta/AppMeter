package app.rbzeta.applicationmeter.app;

import android.app.Application;

import app.rbzeta.applicationmeter.bc.ConnectivityReceiver;

/**
 * Created by Robyn on 27/10/2016.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate(){
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance(){return mInstance;}

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
