package com.lutra.legallydistinctpocketmonsterarea;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;

import androidx.appcompat.app.AppCompatActivity;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityBattleBinding;

import java.util.Random;

public class BattleActivity extends AppCompatActivity {

    private ActivityBattleBinding binding;
    private AppRepository repository;

    private StringBuilder dialogBuilder = new StringBuilder();

    UserMonster userMonster;
    UserMonster enemyMonster;

    UserMonster activeMonster;

    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Inserted for testing, but I kinda like it anyway!
        binding.battleDialog.setMovementMethod(new ScrollingMovementMethod());

        repository = AppRepository.getRepository(getApplication());

        initializeBattle();
        takeCombatTurn();

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

        userMonster = new UserMonster("NotBulbasaur", "Yoooo, got any grass?",
                UserMonster.ElementalType.GRASS,8,5,30,420,420);
        enemyMonster = new UserMonster("NotSquirtle", "I didn't know you liked to get wet...",
                UserMonster.ElementalType.WATER,8,4,20,421,421);

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

        dialogBuilder.append(String.format("You have encountered a wild %s!%n", enemyMonsterName.toUpperCase()));
        dialogBuilder.append(String.format("\"%s\"%n%n", enemyMonster.getPhrase()));

        dialogBuilder.append(String.format("Go-go-gadget %s!%n", userMonsterName.toUpperCase()));
        dialogBuilder.append(String.format("\"%s\"%n%n", userMonster.getPhrase()));

        if(activeMonster == enemyMonster){
            dialogBuilder.append(String.format("%s gets the drop on %s.", enemyMonsterName, userMonsterName));
        }

        binding.battleDialog.setText(dialogBuilder.toString());
    }

    private void takeCombatTurn() {
        int attackValue = 0;
        int damage = 0;
        String enemyMonsterName = enemyMonster.getNickname().toUpperCase();
        String userMonsterName = userMonster.getNickname().toUpperCase();

        if(activeMonster == enemyMonster) {
            //Enemy Monster has a 50% chance to use a special vs. normal attack.
            if(rand.nextInt() % 2 == 0) {
                dialogBuilder.append(String.format("%s uses a special move...%n", enemyMonsterName));
                attackValue = enemyMonster.specialAttack(userMonster.getType());
                if(attackValue <= 0) {
                    dialogBuilder.append(String.format("%s avoided the attack!%n%n", userMonsterName));
                } else {
                    double modifier = enemyMonster.attackModifier(userMonster.getType());
                    if(modifier > 1) {
                        dialogBuilder.append("It's su-cough...it worked really well!\n");
                    } else if(modifier < 1) {
                        dialogBuilder.append("Dude, what were you thinking?\n");
                    } else {
                        dialogBuilder.append("It was fine, I guess.\n");
                    }
                    damage = userMonster.takeDamage(attackValue);
                    dialogBuilder.append(String.format("%s is hit for %d damage.%n%n", userMonsterName, damage));
                }
            } else {
                dialogBuilder.append(String.format("%s is attacking.%n", enemyMonsterName));
                attackValue = enemyMonster.normalAttack();
                damage = userMonster.takeDamage(attackValue);
                dialogBuilder.append(String.format("%s is hit for %d damage.%n%n", userMonsterName, damage));
            }
            binding.battleDialog.setText(dialogBuilder.toString());
            if(userMonster.getCurrentHealth() > 0) {
                String userHP = userMonster.getCurrentHealth() + "/" + userMonster.getMaxHealth();
                binding.userMonsterHP.setText(userHP);
                activeMonster = userMonster;
                takeCombatTurn();
            } else {
                String userHP = "0/" + userMonster.getMaxHealth();
                binding.userMonsterHP.setText(userHP);
                dialogBuilder.append(String.format("%s%n%s fainted! Battle demo ends for now.",
                        userMonster.getPhrase(),userMonster.getNickname()));
                binding.battleDialog.setText(dialogBuilder.toString());
            }
        }
    }
}