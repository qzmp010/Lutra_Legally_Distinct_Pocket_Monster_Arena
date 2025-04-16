package com.lutra.legallydistinctpocketmonsterarea;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


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
                Toast.makeText(LobbyActivity.this, "Logout is clicked" ,Toast.LENGTH_SHORT).show();
            }
        });

    }
}