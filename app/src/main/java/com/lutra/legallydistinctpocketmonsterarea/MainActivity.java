package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityMainBinding;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding binding;
  private AppRepository repository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    
    binding.lobbyActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(LobbyActivity.intentFactory(getApplicationContext()));
      }
    });

    binding.battleActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(BattleActivity.intentFactory(getApplicationContext()));
      }
    });

    binding.adminLobbyActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(AdminLobbyActivity.intentFactory(getApplicationContext()));
      }
    });
  }
}