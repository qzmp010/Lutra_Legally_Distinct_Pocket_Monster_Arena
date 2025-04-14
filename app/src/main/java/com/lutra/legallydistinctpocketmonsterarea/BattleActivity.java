package com.lutra.legallydistinctpocketmonsterarea;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityBattleBinding;

import java.util.Random;

public class BattleActivity extends AppCompatActivity {

    private ActivityBattleBinding binding;
    private AppRepository repository;

    UserMonster userMonster;
    UserMonster enemyMonster;

    UserMonster activeMonster;

    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AppRepository.getRepository(getApplication());

        initializeBattle();

    }

    /**
     * Initializes initial battle conditions:
     * Restore's all users monsters' HP.
     * Selects desired user's monster from database as userMonster.
     * Rolls random monster from type list for enemyMonster.
     * Determines which monster takes first turn (userMonster 75% chance)
     * Displays battle beginning dialog
     */
    private void initializeBattle() {
        //TODO: Change default conditions to pull monsters from database for combat.
        //Below are default monsters for testing.

        userMonster = new UserMonster("NotBulbasaur", "Yoooo, got any grass?", UserMonster.ElementalType.GRASS,8,5,30,420,420);
        enemyMonster = new UserMonster("NotSquirtle", "I didn't know you liked to get wet...", UserMonster.ElementalType.WATER,8,4,20,421,421);

        //Rolls to see which monster goes first
        if(Math.abs(rand.nextInt() % 4) == 0) {
            activeMonster = enemyMonster;
        } else {
            activeMonster = userMonster;
        }

        String enemyMonsterName = enemyMonster.getNickname();
        String userMonsterName = userMonster.getNickname();

        binding.enemyMonsterName.setText(enemyMonsterName);
        binding.enemyMonsterImage.setImageResource(R.drawable.ld_squirtle);
        //TODO: Consider replacing in layout with two separate fields so concatenation isn't needed.
        String enemyHP = enemyMonster.getCurrentHealth() + "/" + enemyMonster.getMaxHealth();
        binding.enemyMonsterHP.setText(enemyHP);

        binding.userMonsterName.setText(userMonster.getNickname());
        binding.userMonsterImage.setImageResource(R.drawable.ld_bulbasaur_png);
        String userHP = userMonster.getCurrentHealth() + "/" + userMonster.getMaxHealth();
        binding.userMonsterHP.setText(userHP);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("You have encountered a wild %s!%n", enemyMonsterName.toUpperCase()));
        sb.append(String.format("\"%s\"%n%n", enemyMonster.getPhrase()));

        sb.append(String.format("Go-go-gadget %s!%n", userMonsterName.toUpperCase()));
        sb.append(String.format("\"%s\"%n%n", userMonster.getPhrase()));

        if(activeMonster == enemyMonster){
            sb.append(String.format("%s gets the drop on %s.", enemyMonsterName, userMonsterName));
        }

        binding.battleDialog.setText(sb.toString());
    }
}