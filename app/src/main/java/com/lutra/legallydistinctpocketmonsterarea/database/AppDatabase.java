package com.lutra.legallydistinctpocketmonsterarea.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lutra.legallydistinctpocketmonsterarea.R;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType.ElementalType;
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
        //todo: update initial monsters
        //insert initial test monsters
        MonsterTypeDAO monsterTypeDAO = INSTANCE.monsterTypeDAO();
        long monsterTypeId = monsterTypeDAO.insert(new MonsterType("Lightning Mousey",
            "Aachoo!",R.drawable.ld_pikachu, 10, 5, 10,5,
            25,20, UserMonster.ElementalType.ELECTRIC));

        UserMonsterDAO userMonsterDAO = INSTANCE.userMonsterDAO();
        userMonsterDAO.insert(new UserMonster("Zappy", "ZZZZZZZT", R.drawable.ld_pikachu, UserMonster.ElementalType.ELECTRIC, 10, 4,
            25, 0, (int)monsterTypeId));

        userMonsterDAO.insert(new UserMonster("Bigger Zappy", "BUZZZZZZZT",R.drawable.ld_pikachu, UserMonster.ElementalType.ELECTRIC, 15, 7,
            35, 0, (int)monsterTypeId));

        long monsterTypeId2 = monsterTypeDAO.insert(new MonsterType("Singing Balloon",
            "Poooof!",R.drawable.ld_jiggly, 7,4, 8,6,
            30,19, UserMonster.ElementalType.NORMAL));

        userMonsterDAO.insert(new UserMonster("Jiggly", "Poooof!",R.drawable.ld_jiggly, UserMonster.ElementalType.NORMAL, 7, 4,
            22, 0, (int)monsterTypeId2));

        userMonsterDAO.insert(new UserMonster("Wiggly", "Pop!",R.drawable.ld_jiggly, UserMonster.ElementalType.NORMAL, 17, 14,
            42, 0, (int)monsterTypeId2));

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

  public abstract UserDAO userDao();
}
