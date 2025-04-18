package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CaptureActivity extends AppCompatActivity {

    public static final String TAG = "CaptureActivity.java";

    private int loggedInUser;
    private int enemyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        return intent;
    }
}