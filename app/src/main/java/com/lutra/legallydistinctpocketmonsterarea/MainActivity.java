package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityMainBinding;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
  private static final String MAIN_ACTIVITY_USER_ID = "com.lutra.legallydistinctpocketmonsterarea.MAIN_ACTIVITY_USER_ID";
  static final String SHARED_PREFERENCE_USERID_KEY = "com.lutra.legallydistinctpocketmonsterarea.SHARED_PREFERENCE_USERID_KEY";
  private static final int LOGGED_OUT = -1;
  private static final String SHARED_PREFERENCE_USERID_VALUE = "com.lutra.legallydistinctpocketmonsterarea.SHARED_PREFERENCE_USERID_VALUE";
  private ActivityMainBinding binding;
  private AppRepository repository;

  private int loggedInUserId = -1;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    repository = AppRepository.getRepository(getApplication());
    loginUser(savedInstanceState);


    if(loggedInUserId == -1 ){
      Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
      startActivity(intent);
    }


    //todo: remove -- test read from database
    var userMonsters = repository.getAllUserMonsters();
    var monsterTypes = repository.getAllMonsterTypes();

    String text = monsterTypes == null ? "null" : monsterTypes.stream().map(MonsterType::toString).collect(Collectors.joining("\n\n"));
    String text2 = userMonsters == null ? "null" : userMonsters.stream().map(UserMonster::toString).collect(Collectors.joining("\n\n"));
    binding.myTextView.setText(text + "\n\n\n\n" + text2);
  }

  private void loginUser(Bundle savedInstanceState) {

    SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFERENCE_USERID_KEY,
            Context.MODE_PRIVATE);

    if(sharedPreferences.contains(SHARED_PREFERENCE_USERID_VALUE)){
      loggedInUserId = sharedPreferences.getInt(SHARED_PREFERENCE_USERID_VALUE, LOGGED_OUT);

    }
    if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SHARED_PREFERENCE_USERID_KEY)){
      loggedInUserId = sharedPreferences.getInt(SHARED_PREFERENCE_USERID_KEY, LOGGED_OUT);
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
      }else{
          logout();
        }
    });

  }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
    super.onSaveInstanceState(outState);
    outState.putInt(SHARED_PREFERENCE_USERID_KEY, loggedInUserId);
    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);
    SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
    sharedPrefEditor.putInt(MainActivity.SHARED_PREFERENCE_USERID_KEY, loggedInUserId);
    sharedPrefEditor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logged_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
      MenuItem item = menu.findItem(R.id.logoutMenuItem);
      item.setVisible(true);
      item.setTitle("Log out");
      item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
          showLogoutDialog();
          return false;
        }
      });
        return true;
    }

  private void showLogoutDialog(){
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
    final AlertDialog alertDialog = alertBuilder.create();

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
        alertDialog.dismiss();
      }
    });

    alertBuilder.create().show();

  }
  private void logout() {
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);
    SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
    sharedPrefEditor.putInt(SHARED_PREFERENCE_USERID_KEY,LOGGED_OUT);
    sharedPrefEditor.apply();

    getIntent().putExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
    startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
  }


  static Intent mainActivityIntentFactory(Context context, int userId){
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
    return intent;
  }
}