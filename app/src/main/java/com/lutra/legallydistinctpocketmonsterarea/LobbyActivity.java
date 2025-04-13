package com.lutra.legallydistinctpocketmonsterarea;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import androidx.core.view.WindowInsetsCompat;

import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityLobbyBinding;

public class LobbyActivity extends AppCompatActivity {
   // ActivityLobbyBinding binding = ActivityLobbyBinding.inflate(getLayoutInflater());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityLobbyBinding binding = ActivityLobbyBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        setContentView(binding.getRoot());
        binding.battleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //toast for testing purposes
                Toast.makeText(LobbyActivity.this, "Battle button is clicked", Toast.LENGTH_SHORT).show();

            }
        });

    }
}