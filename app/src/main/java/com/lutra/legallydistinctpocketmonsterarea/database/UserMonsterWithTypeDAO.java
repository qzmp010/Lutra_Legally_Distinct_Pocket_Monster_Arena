package com.lutra.legallydistinctpocketmonsterarea.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterTypeWithUserMonsters;
import java.util.List;
import java.util.Map;

@Dao
public interface UserMonsterWithTypeDAO {

  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE
      + " INNER JOIN " + AppDatabase.MONSTER_TYPE_TABLE
      + " ON " + AppDatabase.USER_MONSTER_TABLE + ".monsterTypeId = "
      + AppDatabase.MONSTER_TYPE_TABLE + ".monsterTypeId")
  Map<UserMonster, MonsterType> getUserMonstersWithTypeMap();

  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE
      + " INNER JOIN " + AppDatabase.MONSTER_TYPE_TABLE
      + " ON " + AppDatabase.USER_MONSTER_TABLE + ".monsterTypeId = "
      + AppDatabase.MONSTER_TYPE_TABLE + ".monsterTypeId")
  LiveData<Map<UserMonster, MonsterType>> getUserMonstersWithTypeMapLiveData();

  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE
      + " INNER JOIN " + AppDatabase.MONSTER_TYPE_TABLE
      + " ON " + AppDatabase.USER_MONSTER_TABLE + ".monsterTypeId = "
      + AppDatabase.MONSTER_TYPE_TABLE + ".monsterTypeId"
      + " WHERE userMonsterId = :userMonsterId"
      + " LIMIT 1")
  Map<UserMonster, MonsterType> getUserMonsterWithTypeMapByUserMonsterId(int userMonsterId);

  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE
      + " INNER JOIN " + AppDatabase.MONSTER_TYPE_TABLE
      + " ON " + AppDatabase.USER_MONSTER_TABLE + ".monsterTypeId = "
      + AppDatabase.MONSTER_TYPE_TABLE + ".monsterTypeId"
      + " WHERE userId = :userId")
  Map<UserMonster, MonsterType> getUserMonstersWithTypeMapByUserId(int userId);

  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE
      + " INNER JOIN " + AppDatabase.MONSTER_TYPE_TABLE
      + " ON " + AppDatabase.USER_MONSTER_TABLE + ".monsterTypeId = "
      + AppDatabase.MONSTER_TYPE_TABLE + ".monsterTypeId"
      + " WHERE userId = :userId")
  LiveData<Map<UserMonster, MonsterType>> getUserMonstersWithTypeMapByUserIdLiveData(int userId);

  @Transaction
  @Query("SELECT * FROM " + AppDatabase.MONSTER_TYPE_TABLE)
  List<MonsterTypeWithUserMonsters> getMonsterTypesWithUserMonsters();

  @Transaction
  @Query("SELECT * FROM " + AppDatabase.MONSTER_TYPE_TABLE)
  LiveData<List<MonsterTypeWithUserMonsters>> getMonsterTypesWithUserMonstersLiveData();
}
