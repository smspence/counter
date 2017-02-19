package com.buoybit.smspence.counter;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.complications.ProviderUpdateRequester;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends WearableActivity {

    private BoxInsetLayout containerView;
    private TextView currentCountTextView;

    private Button buttonReset;
    private Button buttonDecrement;
    private Button buttonIncrement;

    private static final int backgroundColorInteractive = Color.DKGRAY;
    private static final int backgroundColorAmbient = Color.BLACK;

    private SharedPreferences sharedPrefs;

    private ProviderUpdateRequester providerUpdateRequester;

    private int currentCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        containerView = (BoxInsetLayout) findViewById(R.id.container);
        containerView.setBackgroundColor(backgroundColorInteractive);

        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonDecrement = (Button) findViewById(R.id.buttonDecrement);
        buttonIncrement = (Button) findViewById(R.id.buttonIncrement);

        currentCountTextView = (TextView) findViewById(R.id.currentCountTextView);

        sharedPrefs = getApplicationContext().getSharedPreferences(Constants.COUNTER_SHARED_PREFS_KEY, 0);
        currentCount = sharedPrefs.getInt(Constants.COUNT_KEY, 0);

        updateCountTextView();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentCount = 0;

                updateSavedData();

                updateCountTextView();
            }
        });

        buttonDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentCount > 0) {
                    currentCount--;

                    updateSavedData();

                    updateCountTextView();
                }
            }
        });

        buttonIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentCount++;

                updateSavedData();

                updateCountTextView();
            }
        });

        ComponentName complicationProviderComponentName =
                new ComponentName(getApplicationContext(), CountProviderService.class);

        providerUpdateRequester =
                new ProviderUpdateRequester(getApplicationContext(), complicationProviderComponentName);
    }

    private void updateSavedData() {

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.COUNT_KEY, currentCount);
        editor.apply();

        // Send update to any complications that
        //  might currently be using this data
        providerUpdateRequester.requestUpdateAll();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateCountTextView() {
        final String countString = NumberFormat.getIntegerInstance().format(currentCount);
        currentCountTextView.setText(countString);
    }

    private void updateButtonVisibility(boolean isInAmbientMode) {

        final int state = isInAmbientMode ? View.INVISIBLE : View.VISIBLE;

        buttonReset.setVisibility(state);
        buttonDecrement.setVisibility(state);
        buttonIncrement.setVisibility(state);
    }

    private void updateDisplay() {

        updateCountTextView();

        updateButtonVisibility(isAmbient());

        if (isAmbient()) {

            containerView.setBackgroundColor(backgroundColorAmbient);

        } else {

            containerView.setBackgroundColor(backgroundColorInteractive);

        }
    }
}
