package br.com.apuri.utils.activities;

import android.app.Activity;

public class ApuriActivityUtils {

    /**
     * Runs the runnable in the main thread if the activity is not null
     * @param activity The activity to run a task
     * @param runnable The runnable
     */
    public static void runOnUiThread(Activity activity, Runnable runnable){
        if(activity != null && runnable != null)
            activity.runOnUiThread(runnable);
    }
}
