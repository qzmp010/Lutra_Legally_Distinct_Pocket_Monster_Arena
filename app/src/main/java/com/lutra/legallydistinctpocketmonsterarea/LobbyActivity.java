package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import androidx.core.view.WindowInsetsCompat;

import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityLobbyBinding;

public class LobbyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLobbyBinding binding = ActivityLobbyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String user_name = getIntent().getStringExtra("username");

        if(!(user_name == null) && !user_name.isEmpty()){
            binding.welcomeTextView.setText("Welcome "+ user_name + "!");
        }else{
            System.out.println("Username cannot be blank");
        }

        binding.battleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //toast for testing purposes, need to implement Battle Activity
                Toast.makeText(LobbyActivity.this, "Battle button is clicked", Toast.LENGTH_SHORT).show();

            }
        });
        binding.ViewMonster.setOnClickListener(new View.OnClickListener() { //need to implement View Monster Activity
            @Override
            public void onClick(View view) {
                Toast.makeText(LobbyActivity.this, "View monster is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        binding.Logout.setOnClickListener(new View.OnClickListener() { //handle logout part
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });


    }
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LobbyActivity.this);
        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), -1);
        sharedPrefEditor.apply();

        Intent intent = new Intent(LobbyActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, LobbyActivity.class);
        return intent;
    }
}