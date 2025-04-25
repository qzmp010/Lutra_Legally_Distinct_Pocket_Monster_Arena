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
        UserDAO userDao = INSTANCE.userDao();
        User admin = new User("admin", "admin123", true);
        int userAdminId = (int) userDao.insert(admin);

        User testUser = new User("trainer1", "pokedex123", false);
        int userTestUserId = (int) userDao.insert(testUser);

        User davidTest = new User("david", "12345", false);
        davidTest.setId(49);
        int userDavidId = (int) userDao.insert(davidTest);

        //Enemy Monsters:
        //Name
        //Phrase
        //Image
        //Max, Min Attack
        //Max, Min Defense
        //Max, Min Health
        //Type
        MonsterTypeDAO monsterTypeDAO = INSTANCE.monsterTypeDAO();

        MonsterType typeDino = new MonsterType(
            "Flower Dino",
            "Flower power, ya dig?",
            R.drawable.ld_bulbasaur_png,
            8, 5,
            8, 5,
            27, 22,
            UserMonster.ElementalType.GRASS);

        MonsterType typeTurtle = new MonsterType(
            "Weird Turtle",
            "'Urtle! 'Urtle!'",
            R.drawable.ld_squirtle,
            9, 6,
            7, 4,
            25, 20,
            UserMonster.ElementalType.WATER);

        MonsterType typeLizard = new MonsterType(
            "Fire Lizard",
            "Deal with it.",
            R.drawable.ld_charizard,
            11, 8,
            6, 3,
            23, 16,
            UserMonster.ElementalType.FIRE);

        MonsterType typeMousey = new MonsterType(
            "Lightning Mousey",
            "Aa-chooooooo!",
            R.drawable.ld_pikachu,
            10, 7,
            7, 5,
            25, 20,
            UserMonster.ElementalType.ELECTRIC);

        MonsterType typeBalloon = new MonsterType(
            "Singing Balloon",
            "Poooooooof!",
            R.drawable.ld_jiggly,
            7, 4,
            6, 4,
            22, 17,
            UserMonster.ElementalType.NORMAL);

        typeDino.setMonsterTypeId((int) monsterTypeDAO.insert(typeDino));
        typeTurtle.setMonsterTypeId((int) monsterTypeDAO.insert(typeTurtle));
        typeLizard.setMonsterTypeId((int) monsterTypeDAO.insert(typeLizard));
        typeMousey.setMonsterTypeId((int) monsterTypeDAO.insert(typeMousey));
        typeBalloon.setMonsterTypeId((int) monsterTypeDAO.insert(typeBalloon));

        //Test monsters for SwitchMonster
        //TODO: Remove test monsters
        UserMonsterDAO userMonsterDAO = INSTANCE.userMonsterDAO();

        //admin gets one monster
        userMonsterDAO.insert(new UserMonster(
            0,
            "Wiggly",
            typeBalloon.getPhrase(),
            typeBalloon.getImageID(),
            typeBalloon.getElementalType(),
            21,
            42,
            69,
            userAdminId,
            typeBalloon.getMonsterTypeId()
        ));

        userMonsterDAO.insert(new UserMonster(
            0,
            "Big Green",
            typeDino.getPhrase(),
            typeDino.getImageID(),
            typeDino.getElementalType(),
            10,
            6,
            40,
            userDavidId,
            typeDino.getMonsterTypeId()
        ));

        userMonsterDAO.insert(new UserMonster(
            0,
            "Texas Filburt",
            typeTurtle.getPhrase(),
            typeTurtle.getImageID(),
            typeTurtle.getElementalType(),
            11,
            5,
            38,
            userDavidId,
            typeTurtle.getMonsterTypeId()
        ));

        userMonsterDAO.insert(new UserMonster(
            0,
            "Trogdoorrr",
            typeLizard.getPhrase(),
            typeLizard.getImageID(),
            typeLizard.getElementalType(),
            12,
            4,
            34,
            userDavidId,
            typeLizard.getMonsterTypeId()
        ));

        userMonsterDAO.insert(new UserMonster(
            0,
            "Zappy",
            typeMousey.getPhrase(),
            typeMousey.getImageID(),
            typeMousey.getElementalType(),
            13,
            5,
            32,
            userDavidId,
            typeMousey.getMonsterTypeId()
        ));

        userMonsterDAO.insert(new UserMonster(
            0,
            "Chupon",
            typeBalloon.getPhrase(),
            typeBalloon.getImageID(),
            typeBalloon.getElementalType(),
            8,
            5,
            17,
            userDavidId,
            typeBalloon.getMonsterTypeId()
        ));
      });
    }
  };

  public abstract MonsterTypeDAO monsterTypeDAO();

  public abstract UserMonsterDAO userMonsterDAO();

  public abstract UserMonsterWithTypeDAO userMonsterWithTypeDAO();

  public abstract UserDAO userDao();
}
