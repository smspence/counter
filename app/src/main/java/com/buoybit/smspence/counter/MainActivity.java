package com.buoybit.smspence.counter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout containerView;
    private TextView currentCountTextView;

    private Button buttonReset;
    private Button buttonDecrement;
    private Button buttonIncrement;

    private static final int backgroundColorInteractive = Color.DKGRAY;
    private static final int backgroundColorAmbient = Color.BLACK;

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

        updateCountTextView();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentCount = 0;

                updateCountTextView();
            }
        });

        buttonDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentCount > 0) {
                    currentCount--;
                }

                updateCountTextView();
            }
        });

        buttonIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentCount++;

                updateCountTextView();
            }
        });
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
