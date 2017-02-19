package com.buoybit.smspence.counter;

import android.content.SharedPreferences;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.complications.ComplicationManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ComplicationText;

import java.text.NumberFormat;


public class CountProviderService extends ComplicationProviderService {

    @Override
    public void onComplicationActivated(int complicationId, int type, ComplicationManager manager) {
        super.onComplicationActivated(complicationId, type, manager);
    }

    @Override
    public void onComplicationUpdate(int complicationId, int dataType, ComplicationManager complicationManager) {

        ComplicationData complicationData = null;

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(Constants.COUNTER_SHARED_PREFS_KEY, 0);
        final int currentCount = sharedPrefs.getInt(Constants.COUNT_KEY, 0);

        final String countString = NumberFormat.getIntegerInstance().format(currentCount);

        switch (dataType) {

            case ComplicationData.TYPE_SHORT_TEXT:
                complicationData = new ComplicationData.Builder(ComplicationData.TYPE_SHORT_TEXT)
                        .setShortText(ComplicationText.plainText(countString))
                        .setShortTitle(ComplicationText.plainText(getResources().getString(R.string.count)))
                        .build();
                break;

            default:
                // Unhandled complication data type
                break;
        }

        if (complicationData != null) {
            complicationManager.updateComplicationData(complicationId, complicationData);
        }
    }
}
