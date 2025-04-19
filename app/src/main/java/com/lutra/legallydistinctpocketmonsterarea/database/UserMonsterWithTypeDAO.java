package com.lutra.legallydistinctpocketmonsterarea.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.Map;

@Dao
public interface UserMonsterWithTypeDAO {
  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE
      + " INNER JOIN " + AppDatabase.MONSTER_TYPE_TABLE
      + " ON " + AppDatabase.USER_MONSTER_TABLE + ".monsterTypeId = "
      + AppDatabase.MONSTER_TYPE_TABLE + ".monsterTypeId"
      + " WHERE userId = :userId")
  Map<UserMonster, MonsterType> getUserMonsterMapByUserId(int userId);

  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE
      + " INNER JOIN " + AppDatabase.MONSTER_TYPE_TABLE
      + " ON " + AppDatabase.USER_MONSTER_TABLE + ".monsterTypeId = "
      + AppDatabase.MONSTER_TYPE_TABLE + ".monsterTypeId"
      + " WHERE userId = :userId")
  LiveData<Map<UserMonster, MonsterType>> getUserMonsterMapByUserIdLiveData(int userId);
}
