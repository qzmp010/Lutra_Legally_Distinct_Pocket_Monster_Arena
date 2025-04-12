package com.lutra.legallydistinctpocketmonsterarea.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.List;

@Dao
public interface UserMonsterDAO {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(UserMonster userMonster);

  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE)
  List<UserMonster> getAll();

  @Query("SELECT * FROM " + AppDatabase.USER_MONSTER_TABLE + " WHERE userId = :userId")
  List<UserMonster> getByUserId(int userId);
}
