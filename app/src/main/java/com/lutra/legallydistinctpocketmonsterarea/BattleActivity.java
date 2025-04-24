package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityBattleBinding;

import java.util.ArrayList;
import java.util.Random;

/**
 * BattleActivity - the main battle engine of LDPM Arena. Initially lands from SwitchMonster activity
 * with a monster chosen by the user. Opponent monster is chosen in static MonsterFactory method.
 * User has four options - normal attack (uses attack stat), special attack (x1.5 dmg, includes element,
 * 75% chance to hit) switch monster, or run (67% chance of success). Upon running, user returns to lobby with
 * all monsters in team healed - this is currently the only way to heal. Enemy has a 25% chance to attack first.
 * If user monster is defeated, they are forced to switch monsters. If enemy monster is defeated, enemy monster
 * is written to DB with userID -1 and brought to CaptureActivity.
 * @author David Rosenfeld
 */

public class BattleActivity extends AppCompatActivity {

    public static final String ENEMY_ID = "BattleActivity.ENEMY_ID";
    public static final String USER_ID = "BattleActivity.USER_ID";
    public static final String TAG = "BattleActivity.Java";

    private ActivityBattleBinding binding;
    private AppRepository repository;
    private int loggedInUserID = -1;
    private int userMonsterID = -1;

    UserMonster userMonster;            //User's monster
    UserMonster enemyMonster;           //CPU
    UserMonster activeMonster;          //Monster whose turn it is

    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Set user ID to the current user
        loginUser();

        //Inserted for testing, but I kinda like it anyway!
        binding.battleDialog.setMovementMethod(new ScrollingMovementMethod());

        repository = AppRepository.getRepository(getApplication());

        getUserMonster();
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

    private void getUserMonster() {
        userMonsterID = getIntent().getIntExtra(SwitchMonsterActivity.MONSTER_ID, -1);
        if(userMonsterID == -1) {
            userMonster = new UserMonster(-1, "MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                    UserMonster.ElementalType.NORMAL,1,1,1,420,-1);
        }
        else {
            userMonster = repository.getUserMonsterById(userMonsterID);
        }
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

        binding.battleDialog.setText("");
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
        String enemyHP = enemyMonster.getCurrentHealth() + "/" + enemyMonster.getMaxHealth();
        binding.enemyMonsterHP.setText(enemyHP);

        binding.userMonsterName.setText(userMonster.getNickname());
        binding.userMonsterImage.setImageResource(userMonster.getImageID());
        String userHP = userMonster.getCurrentHealth() + "/" + userMonster.getMaxHealth();
        binding.userMonsterHP.setText(userHP);

        binding.battleDialog.append(String.format("You have encountered a wild %s!%n", enemyMonsterName.toUpperCase()));
        binding.battleDialog.append(String.format("\"%s\"%n%n", enemyMonster.getPhrase()));

        binding.battleDialog.append(String.format("%s%s!%n", getEntrancePhrase(), userMonsterName.toUpperCase()));
        binding.battleDialog.append(String.format("\"%s\"%n%n", userMonster.getPhrase()));

        if(activeMonster == enemyMonster){
            binding.battleDialog.append(String.format("%s gets the drop on %s.%n%n", enemyMonsterName.toUpperCase(), userMonsterName.toUpperCase()));
        }

    }

    /**
     * Enemy battle script. Enemy has 50/50 chance to use normal or special attack. Special attacks
     * get flavortext depending on elemental affinity. If user monster has HP remaining, userMonster
     * becomes active and player can click a button. If user monster is defeated, a confirmation button
     * appears so the user can see what happened, and continues to SwitchMonsterActivity.
     */
    private void enemyTurn() {
        int attackValue = 0;
        int damage = 0;
        String enemyMonsterName = enemyMonster.getNickname().toUpperCase();
        String userMonsterName = userMonster.getNickname().toUpperCase();

        //Script can't run on user turn.
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
        } else {
            //TODO: consider splitting this off into another method.
            String userHP = "0/" + userMonster.getMaxHealth();
            binding.userMonsterHP.setText(userHP);
            binding.battleDialog.append(String.format("%n\"%s\"%n%s fainted!",
                    userMonster.getPhrase(),userMonster.getNickname()));
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            AlertDialog loseDialog;
            alertBuilder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userSwitch();
                }
            });
            loseDialog = alertBuilder.create();
            loseDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            loseDialog.show();
        }

    }

    /**
     * User makes regular attack against enemyMonster. Calls faintCheck() to see if enemy is defeated.
     */
    private void userAttack() {
        int attackValue = 0;
        int damage = 0;

        binding.battleDialog.append(String.format("%n%s is attacking.%n", userMonster.getNickname().toUpperCase()));
        attackValue = userMonster.normalAttack();
        damage = enemyMonster.takeDamage(attackValue);
        binding.battleDialog.append(String.format("%s is hit for %d damage.%n", enemyMonster.getNickname().toUpperCase(), damage));
        faintCheck();
    }

    /**
     * User makes special attack against enemyMonster. Calls faintCheck() to see if enemy is defeated.
     */
    private void userSpecial() {
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

    /**
     * Check to see if enemy monster has fainted. If yes, insert enemy monster into DB, record its ID,
     * bring it and the user to CaptureActivity, and pop up confirm dialog so user can tell what happened.
     * If not, update display and start enemy turn.
     */
    private void faintCheck() {
        if (enemyMonster.getCurrentHealth() > 0) {
            String enemyHP = enemyMonster.getCurrentHealth() + "/" + enemyMonster.getMaxHealth();
            binding.enemyMonsterHP.setText(enemyHP);

            activeMonster = enemyMonster;
            enemyTurn();
        } else {
            String userHP = "0/" + enemyMonster.getMaxHealth();
            enemyMonster.setCurrentHealth(0);
            binding.enemyMonsterHP.setText(userHP);
            binding.battleDialog.append(String.format("%n\"%s\"%n%s fainted!",
                    enemyMonster.getPhrase(), enemyMonster.getNickname().toUpperCase()));

            long enemyID = repository.insertUserMonster(enemyMonster);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            AlertDialog winDialog;
            alertBuilder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    repository.insertUserMonster(userMonster);
                    Intent intent = CaptureActivity.intentFactory(getApplicationContext());
                    intent.putExtra(BattleActivity.ENEMY_ID, (int) enemyID);
                    intent.putExtra(BattleActivity.USER_ID, loggedInUserID);
                    startActivity(intent);
                }
            });
            winDialog = alertBuilder.create();
            winDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            winDialog.show();
        }
    }

    /**
     * Inserts current user monster into DB (to track health) and starts SwitchMonsterActivity.
     */
    private void userSwitch() {
        repository.insertUserMonster(userMonster);
        Intent intent = SwitchMonsterActivity.intentFactory(getApplicationContext());
        intent.putExtra(USER_ID, loggedInUserID);
        startActivity(intent);
    }

    /**
     * User has 67% chance to run away. If successful, return to lobby and restore all monster health.
     * Confirm dialogs to pause progress so user can understand result. If unsuccessful, enemy takes turn.
     */
    private void userRun() {
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
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            AlertDialog runDialog;
            alertBuilder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = LobbyActivity.intentFactory(getApplicationContext());
                    intent.putExtra(BattleActivity.USER_ID, loggedInUserID);
                    startActivity(intent);
                }
            });
            runDialog = alertBuilder.create();
            runDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            runDialog.show();
        }
    }

    /**
     * Checks intent for tags from other activities that may interact with BattleActivity. If none are found,
     * user will start battle with MISSINGNO.
     */
    private void loginUser() {
        if(loggedInUserID == -1) {
            loggedInUserID =  getIntent().getIntExtra(MainActivity.MAIN_ACTIVITY_USER_ID, -1);
        }

        if(loggedInUserID == -1) {
            loggedInUserID = getIntent().getIntExtra(LobbyActivity.LOBBY_USER_ID, -1);
        }

        if(loggedInUserID == -1) {
            loggedInUserID = getIntent().getIntExtra(SwitchMonsterActivity.USER_ID, -1);
        }

        if(loggedInUserID == -1) {
            loggedInUserID = getIntent().getIntExtra(CaptureActivity.USER_ID, -1);
        }
    }

    /**
     * @return One of four phrases to introduce the monster with.
     */
    private String getEntrancePhrase() {
        Random rand = new Random();
        int phrase = Math.abs(rand.nextInt() % 4);

        switch(phrase) {
            case 0:
                return "Go-go-gadget ";
            case 1:
                return "I..uh..pick youuuu ";
            case 2:
                return "Your attention, please. Now batting, ";
            case 3:
                return "Drop a gem on 'em ";
            default:
        }
        return "I don't have a clever intro for ";
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, BattleActivity.class);
        return intent;
    }
}