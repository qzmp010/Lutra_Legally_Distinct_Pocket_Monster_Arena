package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;

import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
  public static final String MAIN_ACTIVITY_USER_ID = "com.lutra.legallydistinctpocketmonsterarea.MAIN_ACTIVITY_USER_ID";
  static final String SHARED_PREFERENCE_USERID_KEY = "com.lutra.legallydistinctpocketmonsterarea.SHARED_PREFERENCE_USERID_KEY";
  static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.lutra.legallydistinctpocketmonsterarea.SAVED_INSTANCE_STATE_USERID_KEY";
  private static final int LOGGED_OUT = -1;
  private ActivityMainBinding binding;
  private AppRepository repository;

  private int loggedInUserId = -1;
  private User user;

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

    SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
    loggedInUserId = sharedPrefs.getInt(getString(R.string.preference_userId_key), -1);

    repository = AppRepository.getRepository(getApplication());
    loginUser(savedInstanceState);

    if (loggedInUserId == -1) {
      startActivity(LoginActivity.loginIntentFactory(this));
      finish();
      return;
    }

    User user = repository.getUserByUserIdBlocking(loggedInUserId);
    if (user != null && user.isAdmin()) {
      startActivity(AdminLobbyActivity.intentFactory(this, loggedInUserId));
      finish();
      return;
    } else if (user != null) {
      Intent intent = LobbyActivity.intentFactory(this);
      intent.putExtra("USER_ID", loggedInUserId);
      intent.putExtra("username", user.getUsername());
      startActivity(intent);
      finish();
      return;
    }
    startActivity(LoginActivity.loginIntentFactory(this));




    binding.battleActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent  = BattleActivity.intentFactory(getApplicationContext());
        intent.putExtra(MainActivity.MAIN_ACTIVITY_USER_ID, 49);
        startActivity(intent);
      }
    });


    binding.adminLobbyActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(AdminLobbyActivity.intentFactory(getApplicationContext(), loggedInUserId));
      }
    });

    binding.viewMonstersActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(ViewMonstersActivity.intentFactory(getApplicationContext(), loggedInUserId));
      }
    });
    binding.chooseMonsterActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(ChooseMonsterActivity.intentFactory(getApplicationContext()));
      }
    });
    binding.editMonstersActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(EditMonstersActivity.intentFactory(getApplicationContext(), loggedInUserId));
      }
    });

    binding.switchMonsterActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(SwitchMonsterActivity.intentFactory(getApplicationContext()));
      }
    });

    binding.adminCreateMonsterActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(AdminSelectMonsterTypeActivity.intentFactory(getApplicationContext(), loggedInUserId));
      }
    });

    binding.adminEditMonsterActivityButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(AdminSelectUserMonsterActivity.intentFactory(getApplicationContext(), loggedInUserId));
      }
    });
  }


  private void loginUser(Bundle savedInstanceState) {

    SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getString(R.string.preference_file_key),
            Context.MODE_PRIVATE);


    loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

    if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
      loggedInUserId = sharedPreferences.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
    }
    if(loggedInUserId == LOGGED_OUT){
      loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
    }

    if(loggedInUserId == LOGGED_OUT){
      return;
    }
    LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
    userObserver.observe(this,user -> {
      this.user = user;
      if(user != null){
        invalidateOptionsMenu();
      }
    });

  }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
    super.onSaveInstanceState(outState);
    outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
    updateSharedPreference();
    }


  private void updateSharedPreference(){
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
    sharedPrefEditor.putInt(getString(R.string.preference_userId_key),loggedInUserId);
    sharedPrefEditor.apply();
  }

  static Intent mainActivityIntentFactory(Context context, int userId){
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
    return intent;
  }
}