package com.lutra.legallydistinctpocketmonsterarea.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonsterTypeWithUserMonsters that = (MonsterTypeWithUserMonsters) o;
    return Objects.equals(monsterType, that.monsterType) && Objects.equals(
        userMonsters, that.userMonsters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(monsterType, userMonsters);
  }
}