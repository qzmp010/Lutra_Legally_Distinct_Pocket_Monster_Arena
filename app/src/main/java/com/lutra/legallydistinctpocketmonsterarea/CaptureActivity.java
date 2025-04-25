package com.lutra.legallydistinctpocketmonsterarea;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityCaptureBinding;

import java.util.Random;

/**
 * CaptureActivity - entered from BattleActivity on enemy monster defeat. All exit paths lead to
 * SwitchMonsterActivity. User is asked if they want to capture the enemy monster. If no, return to battle.
 * If yes, user is 75% successful. On fail, return to battle. On success, user is prompted to choose a nickname.
 * If not, return to battle. If yes, rename monster and return. On exit, show an alert dialog so user understands
 * result before moving on.
 * @author David Rosenfeld
 */
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
        loggedInUser = getIntent().getIntExtra(BattleActivity.USER_ID, -1);

        //No reason to continue if we'll never see it again.
        if(loggedInUser == -1) {
            startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
        }

        //Try to call captured monster from DB, instantiate MissingNo. on failure.
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

        //Set up initial display conditions
        initializeDisplay();

        //Capture monster
        showCaptureDialog();

    }

    /**
     * Sets up display with enemy monster stats and image.
     */
    private void initializeDisplay() {
        binding.enemyMonsterImage.setImageResource(enemyMonster.getImageID());
        binding.enemyMonsterName.setText(enemyMonster.getNickname());
        binding.enemyMonsterHP.setText(String.format("MAXHP: %d",enemyMonster.getMaxHealth()));
        binding.enemyMonsterType.setText(String.format("%s",enemyMonster.getType().name()));
        binding.enemyMonsterAttack.setText(String.format("ATT: %d", enemyMonster.getAttack()));
        binding.enemyMonsterDefense.setText(String.format("DEF: %d", enemyMonster.getDefense()));

        binding.captureDialog.setText(String.format("%s is weakened!%n", enemyMonster.getNickname().toUpperCase()));
        binding.captureDialog.append(String.format("Would you like to capture %s?%n%n", enemyMonster.getNickname().toUpperCase()));
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        return intent;
    }

    /**
     * Gives user a choice to capture monster. Quits to exit dialog on no or if monster couldn't be retrieved.
     * Attempt to capture monster on yes. I could really just kick them out in the error condition, but I like the GameBoy callback.
     */

    private void showCaptureDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        AlertDialog captureDialog = alertBuilder.create();
        alertBuilder.setTitle("Capture this monster?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Error check to make sure we don't have MissingNO
                if(enemyMonster.getUserMonsterId() == -1) {
                    showExitDialog();
                }
                captureMonster();
            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                repository.deleteMonsterByMonsterId(enemyMonster.getUserMonsterId());
                showExitDialog();
            }
        });

        captureDialog = alertBuilder.create();
        captureDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        captureDialog.show();
    }

    /**
     * 67% chance to capture (that has to be at least equal or greater than in the source material!).
     * On capture, prompt to rename. On fail, exit.
     */
    private void captureMonster() {
        Random rand = new Random();
        binding.captureDialog.append("You throw a...monster...orb?\n");
        if(rand.nextInt() % 3 == 0) {
            binding.captureDialog.append(String.format("%s got away.%n", enemyMonster.getNickname().toUpperCase()));

            repository.deleteMonsterByMonsterId(enemyMonster.getUserMonsterId());
            showExitDialog();

        } else {
            binding.captureDialog.append(String.format("You captured %s!!!%n", enemyMonster.getNickname().toUpperCase()));
            showRenameDialog();
        }
    }

    /**
     * On yes, pop a dialog to rename the monster. On no, exit.
     */
    private void showRenameDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Rename this monster?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                renameMonster();
            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enemyMonster.setUserId(loggedInUser);
                repository.insertUserMonster(enemyMonster);

                showExitDialog();
            }
        });

        AlertDialog renameDialog = alertBuilder.create();
        renameDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        renameDialog.show();
    }

    /**
     * Pops an edit text to rename the monster. Max length 12 chars. On bad choice, pops dialog again.
     * On successful rename, exit. On cancel, pop the previous dialog again.
     */
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
                    showExitDialog();
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

    /**
     * Shows before exit with a confirm button. Needed to be able to let the user know what the result was.
     */
    private void showExitDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        AlertDialog renameDialog = alertBuilder.create();

        alertBuilder.setNeutralButton("Return to Battle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                renameDialog.dismiss();
                Intent intent = SwitchMonsterActivity.intentFactory(getApplicationContext());
                intent.putExtra(CaptureActivity.USER_ID, loggedInUser);
                startActivity(intent);
            }
        });

        alertBuilder.create().show();
    }
}