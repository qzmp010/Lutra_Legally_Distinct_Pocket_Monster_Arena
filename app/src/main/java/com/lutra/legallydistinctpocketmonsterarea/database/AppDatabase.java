package com.lutra.legallydistinctpocketmonsterarea.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lutra.legallydistinctpocketmonsterarea.R;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MonsterType.class, UserMonster.class, User.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
  private static final String DATABASE_NAME = "LDPMDatabase";
  public static final String MONSTER_TYPE_TABLE = "monsterTypeTable";
  public static final String USER_MONSTER_TABLE = "userMonsterTable";

  public static final String USER_TABLE = "userTable";

  private static volatile AppDatabase INSTANCE;
  private static final int NUMBER_OF_THREADS = 4;

  static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(
      NUMBER_OF_THREADS);

  static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(
              context.getApplicationContext(),
              AppDatabase.class,
              DATABASE_NAME
          )
              .fallbackToDestructiveMigrationOnDowngrade(true)
              .addCallback(addDefaultValues)
              .build();
        }
      }
    }
    return INSTANCE;
  }

  private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      databaseWriteExecutor.execute(() -> {
        //Enemy Monsters:
        //Name
        //Phrase
        //Image
        //Max, Min Attack
        //Max, Min Defense
        //Max, Min Health
        //Type
        MonsterTypeDAO monsterTypeDAO = INSTANCE.monsterTypeDAO();
        monsterTypeDAO.insert(new MonsterType(
                "Lightning Mousey",
                "Aa-chooooooo!",
                R.drawable.ld_pikachu,
                10, 7,
                7, 5,
                25,20,
                UserMonster.ElementalType.ELECTRIC));

        monsterTypeDAO.insert(new MonsterType(
                "Singing Balloon",
                "Poooooooof!",
                R.drawable.ld_jiggly,
                7,4,
                6,4,
                22,17,
                UserMonster.ElementalType.NORMAL));

        monsterTypeDAO.insert(new MonsterType(
                "Flower Dino",
                "Flower power, ya dig?",
                R.drawable.ld_bulbasaur_png,
                8, 5,
                8, 5,
                27,22,
                UserMonster.ElementalType.GRASS));

        monsterTypeDAO.insert(new MonsterType(
                "Weird Turtle",
                "'Urtle! 'Urtle!'",
                R.drawable.ld_squirtle,
                9, 6,
                7, 4,
                25,20,
                UserMonster.ElementalType.WATER));

        monsterTypeDAO.insert(new MonsterType(
                "Fire Lizard",
                "Deal with it.",
                R.drawable.ld_charizard,
                11, 8,
                6, 3,
                23,16,
                UserMonster.ElementalType.FIRE));

        //Starters
        UserMonsterDAO userMonsterDAO = INSTANCE.userMonsterDAO();
        userMonsterDAO.insert(new UserMonster(1, "Plantisaurus", "Yo, got any grass?", R.drawable.ld_bulbasaur_png, UserMonster.ElementalType.GRASS, 10, 7,
            40, 0, 1));

        userMonsterDAO.insert(new UserMonster(2, "Zappy", "BUZZZZZZZT",R.drawable.ld_pikachu, UserMonster.ElementalType.ELECTRIC, 11, 6,
            35, 0, 2));

        userMonsterDAO.insert(new UserMonster(3, "Splashturt", "I didn't know you liked to get wet!",R.drawable.ld_squirtle, UserMonster.ElementalType.WATER, 12, 5,
            30, 0, 3));

        userMonsterDAO.insert(new UserMonster(4, "Flamizord", "Burninating the countryside!",R.drawable.ld_charizard, UserMonster.ElementalType.FIRE, 13, 4,
            25, 0, 3));

        UserDAO userDao = INSTANCE.userDao();
        User admin = new User("admin", "admin123", true);
        userDao.insert(admin);

        User testUser = new User("trainer1", "pokedex123", false);
        userDao.insert(testUser);
      });
    }
  };

  public abstract MonsterTypeDAO monsterTypeDAO();

  public abstract UserMonsterDAO userMonsterDAO();

  public abstract UserMonsterWithTypeDAO userMonsterWithTypeDAO();

  public abstract UserDAO userDao();
}
