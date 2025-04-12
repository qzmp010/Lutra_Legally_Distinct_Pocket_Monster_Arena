package com.lutra.legallydistinctpocketmonsterarea.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.lutra.legallydistinctpocketmonsterarea.database.AppDatabase;

@Entity(tableName = AppDatabase.USER_MONSTER_TABLE)
public class UserMonster {
  @PrimaryKey(autoGenerate = true)
  private int userMonsterId;
  private String nickname;
  private int attack;
  private int defense;
  private int health;
  private int userId;
  private int monsterTypeId;
}
