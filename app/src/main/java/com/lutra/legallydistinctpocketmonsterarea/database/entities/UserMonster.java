package com.lutra.legallydistinctpocketmonsterarea.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.lutra.legallydistinctpocketmonsterarea.database.AppDatabase;
import java.util.Objects;

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

  public UserMonster(String nickname, int attack, int defense, int health, int userId,
      int monsterTypeId) {
    this.nickname = nickname;
    this.attack = attack;
    this.defense = defense;
    this.health = health;
    this.userId = userId;
    this.monsterTypeId = monsterTypeId;
  }

  public int getUserMonsterId() {
    return userMonsterId;
  }

  public void setUserMonsterId(int userMonsterId) {
    this.userMonsterId = userMonsterId;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public int getAttack() {
    return attack;
  }

  public void setAttack(int attack) {
    this.attack = attack;
  }

  public int getDefense() {
    return defense;
  }

  public void setDefense(int defense) {
    this.defense = defense;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getMonsterTypeId() {
    return monsterTypeId;
  }

  public void setMonsterTypeId(int monsterTypeId) {
    this.monsterTypeId = monsterTypeId;
  }

  @NonNull
  @Override
  public String toString() {
    return "UserMonster{" +
        "nickname='" + nickname + '\'' +
        ", attack=" + attack +
        ", defense=" + defense +
        ", health=" + health +
        ", userId=" + userId +
        ", monsterTypeId=" + monsterTypeId +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserMonster that = (UserMonster) o;
    return userMonsterId == that.userMonsterId && attack == that.attack && defense == that.defense
        && health == that.health && userId == that.userId && monsterTypeId == that.monsterTypeId
        && Objects.equals(nickname, that.nickname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userMonsterId, nickname, attack, defense, health, userId, monsterTypeId);
  }
}
