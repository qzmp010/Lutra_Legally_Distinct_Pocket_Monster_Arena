package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class MonsterFactory {

    private static final String TAG = "MonsterFactory.java";
    private static final int DEFAULT_USER = -1;
    private static final int DEFAULT_ID = 0;

    public static UserMonster getRandomMonster(AppRepository repository) {

        Random rand = new Random();

        MonsterType template = null;

        //Get all possible monsters from DB
        List<MonsterType> allMonsters = new ArrayList<>();
        while(allMonsters.isEmpty()) {
            try {
                allMonsters = repository.getAllMonsterTypes();
            } catch (RuntimeException e) {
                Log.e(TAG, "Error: Unable to instantiate enemy monster.");
                return new UserMonster(-1, "MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                        UserMonster.ElementalType.NORMAL, 1, 1, 1, 420, -1);
            }
        }

        //Pick one to use as a template
        template = allMonsters.get(Math.abs(rand.nextInt() % allMonsters.size()));


        //Instantiate new monster based on template
        int health = template.getHealthMax() -
                Math.abs(rand.nextInt() % (template.getHealthMax() - template.getHealthMin()));
        int attack = template.getAttackMax() -
                Math.abs(rand.nextInt() % (template.getAttackMax() - template.getAttackMin()));
        int defense = template.getDefenseMax() -
                Math.abs(rand.nextInt() % (template.getDefenseMax() - template.getDefenseMin()));

        return new UserMonster(
                DEFAULT_ID,
                template.getMonsterTypeName(),
                template.getPhrase(),
                template.getImageID(),
                template.getElementalType(),
                attack,
                defense,
                health,
                DEFAULT_USER,
                template.getMonsterTypeId()
        );
    }

    /**
     * Use this function to insert a monster with known characteristics into the database. Order of params listed below.
     * @param repository A good instance of the repository.
     * @param monsterTypeID Defaults: (1)FlowerDino (2)WeirdTurtle (3)FireLizard (4)LightningMousey (5)SingingBalloon
     * @param nickname Use "" as default.
     * @param phrase Use "" as default.
     * @param attack Suggested range between 5 (very weak) to 15 (very strong)
     * @param defense Suggested range between 3 (very weak) to 10 (very strong)
     * @param health Suggested range between 15 (very weak) to 50 (very strong)
     * @param userID User to assign monster to.
     * @return True if monster was able to be created.
     */
    public static boolean createNewMonster(AppRepository repository, int monsterTypeID, String nickname, String phrase, int attack, int defense, int health, int userID) {
        MonsterType template;
        do {
            try {
                template = repository.getMonsterTypeById(monsterTypeID);
            } catch (RuntimeException e) {
                Log.e(TAG, "Unable to retrieve template monster.");
                return false;
            }
        } while(template == null);

        if(nickname.isEmpty()) {
            nickname = template.getMonsterTypeName();
        }

        if(phrase.isEmpty()) {
            phrase = template.getPhrase();
        }

        //The following checks make sure it's not possible to create a monster that can't battle.
        if(attack < 0) {
            attack = 1;
        }

        if(defense < 0) {
            defense = 1;
        }

        if(health < 0) {
            health = 1;
        }

        repository.insertUserMonster(new UserMonster(
                DEFAULT_ID,
                nickname,
                phrase,
                template.getImageID(),
                template.getElementalType(),
                attack,
                defense,
                health,
                userID,
                template.getMonsterTypeId()
        ));
        return true;
    }

    public static UserMonster getUserMonster(AppRepository repository, int userID) {
        Random rand = new Random();
        ArrayList<UserMonster> userMonsters = new ArrayList<>();
        while(userMonsters.isEmpty()) {
            try {
                userMonsters = repository.getUserMonstersByUserId(userID);
            } catch (RuntimeException e) {
                Log.e(TAG, "Error: Unable to instantiate enemy monster.");
                return new UserMonster(-1, "MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                        UserMonster.ElementalType.NORMAL,1,1,1,420,-1);
            }
        }
        return userMonsters.get(Math.abs(rand.nextInt() % userMonsters.size()));
    }
}
