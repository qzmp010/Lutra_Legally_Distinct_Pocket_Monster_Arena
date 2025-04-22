package com.lutra.legallydistinctpocketmonsterarea;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityCaptureBinding;

import java.util.Random;


public class CaptureActivity extends AppCompatActivity {

    public static final String TAG = "CaptureActivity.java";
    public static final String USER_ID = "CaptureActivity_USER_ID";

    private int loggedInUser;
    private int enemyID;
    private UserMonster enemyMonster;
    private AppRepository repository;

    private ActivityCaptureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCaptureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AppRepository.getRepository(getApplication());
        enemyID = getIntent().getIntExtra(BattleActivity.ENEMY_ID, -1);
        //TODO: Change default value below - currently using for testing
        loggedInUser = getIntent().getIntExtra(BattleActivity.USER_ID, 99);

        if(enemyID != -1) {
            while(enemyMonster == null) {
                try {
                    enemyMonster = repository.getUserMonsterById(enemyID);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Could not retrieve enemy monster.");
                    enemyMonster = new UserMonster(-1, "MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                            UserMonster.ElementalType.NORMAL, 1, 1, 1, 420, -1);
                }
            }
        } else {
            Log.e(TAG, "Could not retrieve enemy monster.");
            enemyMonster = new UserMonster(-1, "MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                    UserMonster.ElementalType.NORMAL, 1, 1, 1, 420, -1);
        }

        initializeDisplay();
        showCaptureDialog();

    }

    private void initializeDisplay() {
        binding.enemyMonsterImage.setImageResource(enemyMonster.getImageID());
        binding.enemyMonsterName.setText(enemyMonster.getNickname());
        binding.enemyMonsterHP.setText(String.format("MAXHP: %d",enemyMonster.getMaxHealth()));
        binding.enemyMonsterType.setText(String.format("TYPE: %s",enemyMonster.getType().name()));
        binding.enemyMonsterAttack.setText(String.format("ATT: %d", enemyMonster.getAttack()));
        binding.enemyMonsterDefense.setText(String.format("DEF: %d", enemyMonster.getDefense()));

        binding.captureDialog.setText(String.format("%s is weakened!%n", enemyMonster.getNickname().toUpperCase()));
        binding.captureDialog.append(String.format("Would you like to capture %s?%n%n", enemyMonster.getNickname().toUpperCase()));
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        return intent;
    }

    private void showCaptureDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        AlertDialog captureDialog = alertBuilder.create();
        alertBuilder.setTitle("Capture this monster?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Error check to make sure we don't have MissingNO
                if(enemyMonster.getUserMonsterId() == -1) {
                    Intent intent = BattleActivity.intentFactory(getApplicationContext());
                    intent.putExtra(BattleActivity.USER_ID, loggedInUser);
                    startActivity(intent);
                }
                captureDialog.dismiss();
                captureMonster();
            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean deleted = false;
                while(!deleted) {
                    deleted = repository.deleteMonsterByMonsterId(enemyMonster.getUserMonsterId());
                }

                Intent intent = BattleActivity.intentFactory(getApplicationContext());
                intent.putExtra(BattleActivity.USER_ID, loggedInUser);
                startActivity(intent);
            }
        });

        alertBuilder.create().show();
    }

    private void captureMonster() {
        Random rand = new Random();
        binding.captureDialog.append("You throw a...monster...orb?\n");
        if(rand.nextInt() % 3 == 0) {
            binding.captureDialog.append(String.format("%s got away.%n", enemyMonster.getNickname().toUpperCase()));

            boolean deleted = false;
            while(!deleted) {
                deleted = repository.deleteMonsterByMonsterId(enemyMonster.getUserMonsterId());
            }

            Intent intent = BattleActivity.intentFactory(getApplicationContext());
            intent.putExtra(BattleActivity.USER_ID, loggedInUser);
            startActivity(intent);
        } else {
            binding.captureDialog.append(String.format("You captured %s!!!%n", enemyMonster.getNickname().toUpperCase()));
            showRenameDialog();
        }
    }

    private void showRenameDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        AlertDialog renameDialog = alertBuilder.create();
        alertBuilder.setTitle("Rename this monster?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                renameDialog.dismiss();
                renameMonster();
            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enemyMonster.setUserId(loggedInUser);
                repository.insertUserMonster(enemyMonster);

                Intent intent = BattleActivity.intentFactory(getApplicationContext());
                intent.putExtra(CaptureActivity.USER_ID, loggedInUser);
                startActivity(intent);
            }
        });

        alertBuilder.create().show();
    }

    private void renameMonster() {
        EditText editText = new EditText(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        AlertDialog renameDialog = alertBuilder.create();
        alertBuilder.setMessage("Choose a new nickname (max. 12 chars):");
        alertBuilder.setView(editText);

        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNickname = editText.getText().toString();
                if(!newNickname.isEmpty() && newNickname.length() <= 12) {
                    enemyMonster.setNickname(newNickname);
                    enemyMonster.setUserId(loggedInUser);

                    repository.insertUserMonster(enemyMonster);

                    renameDialog.dismiss();

                    Intent intent = BattleActivity.intentFactory(getApplicationContext());
                    intent.putExtra(CaptureActivity.USER_ID, loggedInUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(CaptureActivity.this, "Invalid nickname.", LENGTH_SHORT).show();
                    renameDialog.dismiss();
                    renameMonster();
                }
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                renameDialog.dismiss();
                showRenameDialog();
            }
        });

        alertBuilder.create().show();
    }
}