package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityMainBinding;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
  private static final String MAIN_ACTIVITY_USER_ID = "com.lutra.legallydistinctpocketmonsterarea.MAIN_ACTIVITY_USER_ID";
  private ActivityMainBinding binding;
  private AppRepository repository;

  int loggedInUserId = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    loginUser();

    if(loggedInUserId == -1 ){
      Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
      startActivity(intent);
    }

    repository = AppRepository.getRepository(getApplication());

    //todo: remove -- test read from database
    var userMonsters = repository.getAllUserMonsters();
    var monsterTypes = repository.getAllMonsterTypes();

    String text = monsterTypes == null ? "null" : monsterTypes.stream().map(MonsterType::toString).collect(Collectors.joining("\n\n"));
    String text2 = userMonsters == null ? "null" : userMonsters.stream().map(UserMonster::toString).collect(Collectors.joining("\n\n"));
    binding.myTextView.setText(text + "\n\n\n\n" + text2);
  }

  private void loginUser() {
    loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID,-1);
  }

  static Intent mainActivityIntentFactory(Context context, int userId){
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
    return intent;
  }
}