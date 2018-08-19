package com.imagine.bd.hayvenapp.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.Timer;
import java.util.TimerTask;

public abstract class CustomTextWatcherimplements implements TextWatcher { //Notice abstract class so we leave abstract method textWasChanged() for implementing class to define it

    private final EditText myTextView; //Remember EditText is a TextView so this works for EditText also


    public CustomTextWatcherimplements(EditText tView) { //Notice I'm passing a view at the constructor, but you can pass other variables or whatever you need
        myTextView= tView;

    }

    private Timer timer = new Timer();
    private final int DELAY = 800; //milliseconds of delay for timer

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(final Editable s) {
        timer.cancel();
        timer = new Timer();

        timer.schedule(

                new TimerTask() {
                    @Override
                    public void run() {
                        textWasChanged();
                    }
                },
                DELAY

        );
    }

    public abstract void textWasChanged();
}; //Notice abstract method to leave implementation to implementing class