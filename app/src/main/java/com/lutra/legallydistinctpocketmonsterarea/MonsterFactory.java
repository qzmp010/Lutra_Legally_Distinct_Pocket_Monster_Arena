package com.lutra.legallydistinctpocketmonsterarea;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class MonsterFactory {

    private static final String TAG = "MonsterFactory.java";

    static UserMonster getRandomMonster(AppRepository repository) {

        Random rand = new Random();

        MonsterType template = null;

        //Get all possible monsters from DB
        List<MonsterType> allMonsters = new ArrayList<>();
        try {
            allMonsters = repository.getAllMonsterTypes();
        } catch (NullPointerException e) {
            Log.e(TAG, "Error: Unable to instantiate enemy monster.");
            return new UserMonster("MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                UserMonster.ElementalType.NORMAL,1,1,1,420,-1);
        }

        //Pick one to use as a template
        if(allMonsters.isEmpty()) {
            Log.e(TAG, "Error: Unable to instantiate enemy monster.");
            return new UserMonster("MISSINGNO.", "I shouldn't even exist.", R.drawable.missingno,
                    UserMonster.ElementalType.NORMAL,1,1,1,420,-1);
        } else {
            template = allMonsters.get(Math.abs(rand.nextInt() % allMonsters.size()));
        }

        //Instantiate new monster based on template
        int health = template.getHealthMax() -
                Math.abs(rand.nextInt() % (template.getHealthMax() - template.getHealthMin()));
        int attack = template.getAttackMax() -
                Math.abs(rand.nextInt() % (template.getAttackMax() - template.getAttackMin()));
        int defense = template.getDefenseMax() -
                Math.abs(rand.nextInt() % (template.getDefenseMax() - template.getDefenseMin()));

        //TODO: Remove userID from UserMonster constructor
        return new UserMonster(
                template.getMonsterTypeName(),
                template.getPhrase(),
                template.getImageID(),
                template.getElementalType(),
                attack,
                defense,
                health,
                420,
                template.getMonsterTypeId()
        );
    }

}
