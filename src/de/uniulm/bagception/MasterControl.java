package de.uniulm.bagception;

import android.app.Application;
import android.content.Context;

public class MasterControl extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        MasterControl.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MasterControl.context;
    }

}
