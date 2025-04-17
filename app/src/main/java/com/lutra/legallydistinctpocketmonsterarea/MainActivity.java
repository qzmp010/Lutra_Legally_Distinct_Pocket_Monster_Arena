package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityMainBinding;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
  private static final String MAIN_ACTIVITY_USER_ID = "com.lutra.legallydistinctpocketmonsterarea.MAIN_ACTIVITY_USER_ID";
  private ActivityMainBinding binding;
  private AppRepository repository;

  private int loggedInUserId = -1;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    loginUser();

    invalidateOptionsMenu();

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
        loggout();
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
  private void loggout() {
    startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
  }


  static Intent mainActivityIntentFactory(Context context, int userId){
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
    return intent;
  }
}