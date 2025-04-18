package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityCaptureBinding;

public class CaptureActivity extends AppCompatActivity {

    public static final String TAG = "CaptureActivity.java";

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
        loggedInUser = getIntent().getIntExtra(BattleActivity.USER_ID, 0);

        if(enemyID != -1) {
            while(enemyMonster == null) {
                try {
                    repository.getUserMonsterById(enemyID);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Could not retrieve enemy monster.");
                    enemyMonster = new UserMonster("MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                            UserMonster.ElementalType.NORMAL, 1, 1, 1, 420, -1);
                }
            }
        } else {
            Log.e(TAG, "Could not retrieve enemy monster.");
            enemyMonster = new UserMonster("MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                    UserMonster.ElementalType.NORMAL, 1, 1, 1, 420, -1);
        }

        initializeDisplay();

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
}