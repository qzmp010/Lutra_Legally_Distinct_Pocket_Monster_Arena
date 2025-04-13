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
  private String phrase;
  private ElementalType type;
  private int attack;
  private int defense;
  private int currentHealth;
  private int maxHealth;
  private int userId;
  private int monsterTypeId;


  public enum ElementalType {
    NORMAL,
    ELECTRIC,
    FIRE,
    GRASS,
    WATER,
  }

  public UserMonster(String nickname, String phrase, ElementalType type, int attack, int defense, int maxHealth, int userId,
      int monsterTypeId) {
    this.nickname = nickname;
    this.phrase = phrase;
    this.type = type;
    this.attack = attack;
    this.defense = defense;
    this.maxHealth = maxHealth;
    this.userId = userId;
    this.monsterTypeId = monsterTypeId;

    currentHealth = maxHealth;
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

  public int getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(int health) {
    this.maxHealth = health;
  }

  public int getCurrentHealth() {
    return currentHealth;
  }

  public void setCurrentHealth(int currentHealth) {
    this.currentHealth = currentHealth;
  }

  public String getPhrase() {
    return phrase;
  }

  public void setPhrase(String phrase) {
    this.phrase = phrase;
  }

  public ElementalType getType() {
    return type;
  }

  public void setType(ElementalType type) {
    this.type = type;
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
        ", maxHealth=" + maxHealth +
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
        && maxHealth == that.maxHealth && userId == that.userId && monsterTypeId == that.monsterTypeId
        && Objects.equals(nickname, that.nickname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userMonsterId, nickname, attack, defense, maxHealth, userId, monsterTypeId);
  }
}
