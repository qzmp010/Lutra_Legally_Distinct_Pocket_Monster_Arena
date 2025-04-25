package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityAdminLobbyBinding;

public class AdminLobbyActivity extends AppCompatActivity {

  private int loggedInUserId;
  AppRepository repository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAdminLobbyBinding binding = ActivityAdminLobbyBinding.inflate(getLayoutInflater());

    loggedInUserId = getIntent().getIntExtra(LobbyActivity.LOBBY_USER_ID, -1);

    repository = AppRepository.getRepository(getApplication());

    repository.getUserByUserId(loggedInUserId).observe(this, user -> {
      if (user != null) {
        binding.WelcomeAdminTextView.setText(
            String.format(getString(R.string.welcome_s), user.getUsername()));
      }
    });

    setContentView(binding.getRoot());
    binding.BattleB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = SwitchMonsterActivity.intentFactory(getApplicationContext());
        intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserId);
        startActivity(intent);
      }
    });

    binding.createMonstersButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = AdminSelectMonsterTypeActivity.intentFactory(getApplicationContext(), loggedInUserId);
            startActivity(intent);
        }
    });

      binding.editMonstersButton.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = AdminSelectUserMonsterActivity.intentFactory(getApplicationContext(), loggedInUserId);
              startActivity(intent);
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
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
    sharedPrefEditor.putInt(getString(R.string.preference_userId_key), -1);
    sharedPrefEditor.apply();

    Intent intent = new Intent(AdminLobbyActivity.this, LoginActivity.class);
    startActivity(intent);
    finish();
  }

  public static Intent intentFactory(Context context, int loggedInUserId) {
    Intent intent = new Intent(context, AdminLobbyActivity.class);
    intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserId);
    return intent;
  }
}