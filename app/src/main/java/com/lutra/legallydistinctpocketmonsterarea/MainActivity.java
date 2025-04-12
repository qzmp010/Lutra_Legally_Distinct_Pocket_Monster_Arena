package com.lutra.legallydistinctpocketmonsterarea;

import android.os.Bundle;

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

    repository = AppRepository.getRepository(getApplication());

    //todo: remove -- test read from database
    var userMonsters = repository.getAllUserMonsters();
    var monsterTypes = repository.getAllMonsterTypes();

    String text = monsterTypes == null ? "null" : monsterTypes.stream().map(MonsterType::toString).collect(Collectors.joining("\n\n"));
    String text2 = userMonsters == null ? "null" : userMonsters.stream().map(UserMonster::toString).collect(Collectors.joining("\n\n"));
    binding.myTextView.setText(text + "\n\n\n\n" + text2);
  }
}