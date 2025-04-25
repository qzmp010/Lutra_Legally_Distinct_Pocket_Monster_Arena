package com.lutra.legallydistinctpocketmonsterarea.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import java.util.List;

@Dao
public interface MonsterTypeDAO {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(MonsterType monsterType);

  @Query("SELECT * FROM " + AppDatabase.MONSTER_TYPE_TABLE)
  List<MonsterType> getAll();

  @Query("DELETE FROM " + AppDatabase.MONSTER_TYPE_TABLE + " WHERE monsterTypeId = :monsterTypeId")
  void deleteMonsterTypeById(int monsterTypeId);


  @Query("SELECT * FROM " + AppDatabase.MONSTER_TYPE_TABLE)
  LiveData<List<MonsterType>> getAllLiveData();

  @Query("SELECT * FROM "
      + AppDatabase.MONSTER_TYPE_TABLE
      + " WHERE monsterTypeId = :monsterTypeId LIMIT 1")
  MonsterType getByMonsterTypeId(int monsterTypeId);


}
