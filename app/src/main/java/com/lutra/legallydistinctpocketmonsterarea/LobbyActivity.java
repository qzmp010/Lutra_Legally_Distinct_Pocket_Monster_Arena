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

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityLobbyBinding;

public class LobbyActivity extends AppCompatActivity {

    public static final String LOBBY_USER_ID = "LobbyActivity.java_LOBBY_USER_ID";
    private int loggedInUserID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLobbyBinding binding = ActivityLobbyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        loginUser();


        String user_name = getIntent().getStringExtra("username");

        if(!(user_name == null) && !user_name.isEmpty()){
            binding.welcomeTextView.setText("Welcome "+ user_name + "!");
        }else{
            System.out.println("Username cannot be blank");
        }

        binding.battleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SwitchMonsterActivity.intentFactory(getApplicationContext());
                intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserID);
                startActivity(intent);
            }
        });
        binding.EditMonstersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditMonstersActivity.intentFactory(getApplicationContext());
                intent.putExtra(LobbyActivity.LOBBY_USER_ID,loggedInUserID);
                startActivity(intent);
            }
        });
        binding.ViewMonster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ViewMonstersActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        binding.Logout.setOnClickListener(new View.OnClickListener() {
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

    private void loginUser() {
        if(loggedInUserID == -1) {
            loggedInUserID =  getIntent().getIntExtra(MainActivity.MAIN_ACTIVITY_USER_ID, -1);
        }

        if(loggedInUserID == -1) {
            loggedInUserID = getIntent().getIntExtra(LoginActivity.USER_ID, -1);
        }

        if(loggedInUserID == -1) {
            loggedInUserID = getIntent().getIntExtra(BattleActivity.USER_ID, -1);
        }

        if(loggedInUserID == -1) {
            loggedInUserID = getIntent().getIntExtra(SwitchMonsterActivity.USER_ID,-1);
        }

        if(loggedInUserID == -1) {
            logout();
        }
    }


    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, LobbyActivity.class);
        return intent;
    }
}