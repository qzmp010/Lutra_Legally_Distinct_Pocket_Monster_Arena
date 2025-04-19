package com.lutra.legallydistinctpocketmonsterarea.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class MonsterTypeWithUserMonsters {
  @Embedded public MonsterType monsterType;
  @Relation(
      parentColumn = "monsterTypeId",
      entityColumn = "monsterTypeId"
  )
  public List<UserMonster> userMonsters;

  @Override
  public String toString() {
    return "MonsterTypeWithUserMonsters{" +
        "monsterType=" + monsterType +
        ", userMonsters=" + userMonsters +
        '}';
  }
}