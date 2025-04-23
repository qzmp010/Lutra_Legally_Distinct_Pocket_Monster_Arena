package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityAdminLobbyBinding;

public class AdminLobbyActivity extends AppCompatActivity {
    private  int loggedInUserID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAdminLobbyBinding binding  = ActivityAdminLobbyBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        binding.BattleB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = BattleActivity.intentFactory(getApplicationContext());
                intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserID);
                startActivity(intent);
            }


        });
        binding.EditMonstersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminLobbyActivity.this, "Edit Monster button is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        binding.EditUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminLobbyActivity.this, "Edit user button is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        binding.CreateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminLobbyActivity.this, "Create user button is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        binding.LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

    }
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminLobbyActivity.this);
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

        Intent intent = new Intent(AdminLobbyActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, AdminLobbyActivity.class);
        return intent;
    }
}