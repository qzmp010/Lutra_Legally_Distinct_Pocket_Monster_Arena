package com.lutra.legallydistinctpocketmonsterarea;

import android.util.Log;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class MonsterFactory {

    private static final String TAG = "MonsterFactory.java";

    static UserMonster getRandomMonster() {

        Random rand = new Random();
        AppRepository repository = AppRepository.getRepository();

        MonsterType template = null;

        //Get all possible monsters from DB
        List<MonsterType> allMonsters = new ArrayList<>();
        try {
            allMonsters = repository.getAllMonsterTypes();
        } catch (NullPointerException e) {
            Log.e(TAG, "Error: Unable to instantiate enemy monster.");
            return null;
        }

        //Pick one to use as a template
        if(allMonsters.isEmpty()) {
            Log.e(TAG, "Error: Unable to instantiate enemy monster.");
            return null;
        } else {
            template = allMonsters.get(Math.abs(rand.nextInt() % allMonsters.size()));
        }

        //Instantiate new monster based on template
        return new UserMonster("NotBulbasaur", "Yoooo, got any grass?",
                UserMonster.ElementalType.GRASS,8,5,30,420,420);
    }
}
