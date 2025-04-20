package com.lutra.legallydistinctpocketmonsterarea;

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

    static UserMonster getRandomMonster(AppRepository repository) {

        Random rand = new Random();

        MonsterType template = null;

        //Get all possible monsters from DB
        List<MonsterType> allMonsters = new ArrayList<>();
        while(allMonsters.isEmpty()) {
            try {
                allMonsters = repository.getAllMonsterTypes();
            } catch (RuntimeException e) {
                Log.e(TAG, "Error: Unable to instantiate enemy monster.");
                return new UserMonster("MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
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

    public static UserMonster getUserMonster(AppRepository repository) {
        Random rand = new Random();
        ArrayList<UserMonster> userMonsters = new ArrayList<>();
        while(userMonsters.isEmpty()) {
            try {
                userMonsters = repository.getAllUserMonsters();
            } catch (RuntimeException e) {
                Log.e(TAG, "Error: Unable to instantiate enemy monster.");
                return new UserMonster("MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                        UserMonster.ElementalType.NORMAL,1,1,1,420,-1);
            }
        }
        return userMonsters.get(Math.abs(rand.nextInt() % userMonsters.size()));
    }
}
