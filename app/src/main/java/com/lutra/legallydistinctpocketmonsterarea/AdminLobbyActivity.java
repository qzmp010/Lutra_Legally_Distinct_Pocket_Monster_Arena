package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityAdminLobbyBinding;

public class AdminLobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAdminLobbyBinding binding  = ActivityAdminLobbyBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.BattleB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminLobbyActivity.this, "Battle button is clicked", Toast.LENGTH_SHORT).show();
            }


        });
        binding.EditMonstersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminLobbyActivity.this, "Edit Monster button is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        binding.CreateMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminLobbyActivity.this, "Create Monster button is clicked", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AdminLobbyActivity.this, "Loggout  is clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, AdminLobbyActivity.class);
        return intent;
    }
}