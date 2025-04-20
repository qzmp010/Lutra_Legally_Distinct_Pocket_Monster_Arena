package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityBattleBinding;

import java.util.ArrayList;
import java.util.Random;

public class BattleActivity extends AppCompatActivity {

    public static final String ENEMY_ID = "BattleActivity.ENEMY_ID";
    public static final String USER_ID = "BattleActivity.USER_ID";
    public static final String TAG = "BattleActivity.Java";


    private ActivityBattleBinding binding;
    private AppRepository repository;
    private int loggedInUserID = -1;

    UserMonster userMonster;
    UserMonster enemyMonster;

    UserMonster activeMonster;

    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginUser();

        //Inserted for testing, but I kinda like it anyway!
        binding.battleDialog.setMovementMethod(new ScrollingMovementMethod());

        repository = AppRepository.getRepository(getApplication());

        initializeBattle();
        enemyTurn();

        binding.normalAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeMonster == userMonster) {
                    userAttack();
                }
            }
        });

        binding.specialAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeMonster == userMonster) {
                    userSpecial();
                }
            }
        });

        binding.switchMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeMonster == userMonster) {
                    userSwitch();
                }
            }
        });

        binding.runMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeMonster == userMonster) {
                    userRun();
                }
            }
        });

    }

    /**
     * Initializes initial battle conditions:
     * Selects desired user's monster from database as userMonster.
     * Rolls random monster from type list for enemyMonster.
     * Renders display for both monsters
     * Determines which monster takes first turn (userMonster 75% chance)
     * Displays battle beginning dialog
     */
    private void initializeBattle() {
        //TODO: Change default conditions to pull monsters from database for combat.
        //Below are default monsters for testing.

        binding.battleDialog.setText("");
        userMonster = MonsterFactory.getUserMonster(repository);
        enemyMonster = MonsterFactory.getRandomMonster(repository);

        //Rolls to see which monster goes first
        if(Math.abs(rand.nextInt() % 4) == 0) {
            activeMonster = enemyMonster;
        } else {
            activeMonster = userMonster;
        }

        String enemyMonsterName = enemyMonster.getNickname();
        String userMonsterName = userMonster.getNickname();

        binding.enemyMonsterName.setText(enemyMonsterName);
        binding.enemyMonsterImage.setImageResource(enemyMonster.getImageID());
        //TODO: Consider replacing in layout with two separate fields so concatenation isn't needed.
        String enemyHP = enemyMonster.getCurrentHealth() + "/" + enemyMonster.getMaxHealth();
        binding.enemyMonsterHP.setText(enemyHP);

        binding.userMonsterName.setText(userMonster.getNickname());
        binding.userMonsterImage.setImageResource(userMonster.getImageID());
        String userHP = userMonster.getCurrentHealth() + "/" + userMonster.getMaxHealth();
        binding.userMonsterHP.setText(userHP);

        binding.battleDialog.append(String.format("You have encountered a wild %s!%n", enemyMonsterName.toUpperCase()));
        binding.battleDialog.append(String.format("\"%s\"%n%n", enemyMonster.getPhrase()));

        binding.battleDialog.append(String.format("Go-go-gadget %s!%n", userMonsterName.toUpperCase()));
        binding.battleDialog.append(String.format("\"%s\"%n%n", userMonster.getPhrase()));

        if(activeMonster == enemyMonster){
            binding.battleDialog.append(String.format("%s gets the drop on %s.%n%n", enemyMonsterName.toUpperCase(), userMonsterName.toUpperCase()));
        }

    }

    private void enemyTurn() {
        int attackValue = 0;
        int damage = 0;
        String enemyMonsterName = enemyMonster.getNickname().toUpperCase();
        String userMonsterName = userMonster.getNickname().toUpperCase();

        if(activeMonster == userMonster) {
            return;
        }

        //Enemy Monster has a 50% chance to use a special vs. normal attack.
        if(rand.nextInt() % 2 == 0) {
            binding.battleDialog.append(String.format("%n%s uses a special move...%n", enemyMonsterName));
            attackValue = enemyMonster.specialAttack(userMonster.getType());
            if(attackValue <= 0) {
                binding.battleDialog.append(String.format("%s avoided the attack!%n", userMonsterName));
            } else {
                double modifier = enemyMonster.attackModifier(userMonster.getType());
                if(modifier > 1) {
                    binding.battleDialog.append("It's su-cough...it worked really well!\n");
                } else if(modifier < 1) {
                    binding.battleDialog.append("Dude, what were you thinking?\n");
                } else {
                    binding.battleDialog.append("It was fine, I guess.\n");
                }
                damage = userMonster.takeDamage(attackValue);
                binding.battleDialog.append(String.format("%s is hit for %d damage.%n", userMonsterName, damage));
            }
        } else {
            binding.battleDialog.append(String.format("%n%s is attacking.%n", enemyMonsterName));
            attackValue = enemyMonster.normalAttack();
            damage = userMonster.takeDamage(attackValue);
            binding.battleDialog.append(String.format("%s is hit for %d damage.%n", userMonsterName, damage));
        }
        if(userMonster.getCurrentHealth() > 0) {
            String userHP = userMonster.getCurrentHealth() + "/" + userMonster.getMaxHealth();
            binding.userMonsterHP.setText(userHP);
            activeMonster = userMonster;
            enemyTurn();
        } else {
            String userHP = "0/" + userMonster.getMaxHealth();
            binding.userMonsterHP.setText(userHP);
            binding.battleDialog.append(String.format("%n%s%n%s fainted! Battle demo ends for now.",
                    userMonster.getPhrase(),userMonster.getNickname()));
        }

    }

    public void userAttack() {
        int attackValue = 0;
        int damage = 0;

        binding.battleDialog.append(String.format("%n%s is attacking.%n", userMonster.getNickname().toUpperCase()));
        attackValue = userMonster.normalAttack();
        damage = enemyMonster.takeDamage(attackValue);
        binding.battleDialog.append(String.format("%s is hit for %d damage.%n", enemyMonster.getNickname().toUpperCase(), damage));
        faintCheck();
    }

    public void userSpecial() {
        int attackValue = 0;
        int damage = 0;

        binding.battleDialog.append(String.format("%n%s uses a special move...%n", userMonster.getNickname().toUpperCase()));
        attackValue = userMonster.specialAttack(enemyMonster.getType());
        if(attackValue <= 0) {
            binding.battleDialog.append(String.format("%s avoided the attack!%n", enemyMonster.getNickname().toUpperCase()));
        } else {
            double modifier = userMonster.attackModifier(enemyMonster.getType());
            if(modifier > 1) {
                binding.battleDialog.append("It's su-cough...it worked really well!\n");
            } else if(modifier < 1) {
                binding.battleDialog.append("Dude, what were you thinking?\n");
            } else {
                binding.battleDialog.append("It was fine, I guess.\n");
            }
            damage = enemyMonster.takeDamage(attackValue);
            binding.battleDialog.append(String.format("%s is hit for %d damage.%n", enemyMonster.getNickname().toUpperCase(), damage));
        }
        faintCheck();
    }

    public void faintCheck() {
        if (enemyMonster.getCurrentHealth() > 0) {
            String enemyHP = enemyMonster.getCurrentHealth() + "/" + enemyMonster.getMaxHealth();
            binding.enemyMonsterHP.setText(enemyHP);

            activeMonster = enemyMonster;
            enemyTurn();
        } else {
            String userHP = "0/" + enemyMonster.getMaxHealth();
            enemyMonster.setCurrentHealth(0);
            binding.enemyMonsterHP.setText(userHP);
            binding.battleDialog.append(String.format("%n%s%n%s fainted!",
                    enemyMonster.getPhrase(), enemyMonster.getNickname()));

            repository.insertUserMonster(enemyMonster);

            Intent intent = CaptureActivity.intentFactory(getApplicationContext());
            intent.putExtra(BattleActivity.ENEMY_ID, enemyMonster.getUserMonsterId());
            intent.putExtra(BattleActivity.USER_ID, loggedInUserID);
            startActivity(intent);
        }
    }

    public void userSwitch() {
        binding.battleDialog.append("UserSwitched clicked.\n\n");
        activeMonster = enemyMonster;
        enemyTurn();
    }

    public void userRun() {
        binding.battleDialog.append("\nYou try to run away...\n");

        Random rand = new Random();
        if(rand.nextInt() % 3 == 0) {
            binding.battleDialog.append("...but cant escape!\n\n");
            activeMonster = enemyMonster;
            enemyTurn();
        } else {
            binding.battleDialog.append("...and did!");

            ArrayList<UserMonster> userMonsters = new ArrayList<>();
            while(userMonsters.isEmpty()) {
                try {
                    userMonsters = repository.getAllUserMonsters();
                    for(UserMonster monster : userMonsters) {
                        monster.setCurrentHealth(monster.getMaxHealth());
                        repository.insertUserMonster(monster);
                    }
                } catch (RuntimeException e) {
                    Log.e(TAG, "Couldn't restore monster health.");
                }
            }

            Intent intent = LobbyActivity.intentFactory(getApplicationContext());
            intent.putExtra(BattleActivity.USER_ID, loggedInUserID);
            startActivity(intent);
        }
    }

    private void loginUser() {
        if(loggedInUserID == -1) {
            loggedInUserID =  getIntent().getIntExtra(MainActivity.MAIN_ACTIVITY_USER_ID, -1);
        }

        if(loggedInUserID == -1) {
            loggedInUserID = getIntent().getIntExtra(LobbyActivity.LOBBY_USER_ID, -1);
        }

        if(loggedInUserID == -1) {
            loggedInUserID = getIntent().getIntExtra(CaptureActivity.USER_ID, -1);
        }
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, BattleActivity.class);
        return intent;
    }
}