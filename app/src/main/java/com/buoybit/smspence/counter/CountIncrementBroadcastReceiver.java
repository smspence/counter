package com.buoybit.smspence.counter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.wearable.complications.ProviderUpdateRequester;


public class CountIncrementBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.COUNTER_SHARED_PREFS_KEY, 0);
        int currentCount = sharedPrefs.getInt(Constants.COUNT_KEY, 0);

        // Increment the current count and then save it back out to shared prefs

        currentCount++;

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.COUNT_KEY, currentCount);
        editor.apply();

        ComponentName complicationProviderComponentName =
                new ComponentName(context, CountProviderService.class);

        ProviderUpdateRequester providerUpdateRequester =
                new ProviderUpdateRequester(context, complicationProviderComponentName);

        // Send update to any complications that
        //  might currently be using this data
        providerUpdateRequester.requestUpdateAll();
    }
}
