package com.lutra.legallydistinctpocketmonsterarea.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.lutra.legallydistinctpocketmonsterarea.MonsterFactory;
import com.lutra.legallydistinctpocketmonsterarea.database.AppDatabase;
import java.util.Objects;
import java.util.Random;

@Entity(tableName = AppDatabase.USER_MONSTER_TABLE)
public class UserMonster {
  @PrimaryKey(autoGenerate = true)
  private int userMonsterId;
  private String nickname;
  private String phrase;
  private int imageID;
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

  public UserMonster(int userMonsterId, String nickname, String phrase, int imageID, ElementalType type, int attack, int defense, int maxHealth, int userId,
      int monsterTypeId) {

    //Make sure each monster has a unique ID set during runtime every time it is instantiated, whether it is inserted into the DB or not
    this.userMonsterId = userMonsterId;

    this.nickname = nickname;
    this.phrase = phrase;
    this.imageID = imageID;
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

  public int getImageID() {
    return imageID;
  }

  public void setImageID(int imageID) {
    this.imageID = imageID;
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

  /**
   * Normal attack against monster.
   * @return Random number within 2 of attack value
   */
  public int normalAttack() {
    Random rand = new Random();
    return attack + (rand.nextInt() % 3);
  }

  /**
   * Special attack against monster.
   * Extra damage modifier, but has 25% chance to miss.
   * @return attackValue
   */
  public int specialAttack(ElementalType type) {
    int attackValue = 0;
    Random rand = new Random();

    //If attack misses, stop further processing
    if(rand.nextInt() % 4 == 0) {
      return attackValue;
    }

    //We aren't dealing with fractional values in this man's program.
    attackValue = (int) (attack + (rand.nextInt() % 3) * 1.5);
    attackValue = (int) (attackValue * attackModifier(type));

    return attackValue;
  }

  /**
   * Calculates modifier for special attack based on traditional weaknesses.
   * Takes in other monster's type as parameter
   * @return attack modifier
   */
  public double attackModifier(ElementalType defending) {
    double modifier = 1.0; //Default modifier

    //No need to continue if we don't have an element
    if(this.type.equals(ElementalType.NORMAL)) {
      return modifier;
    }

    switch(defending) {
      case WATER:
        if(this.type.equals(ElementalType.ELECTRIC) ||
                this.type.equals(ElementalType.GRASS)) {
          modifier = 2.0;
          break;
        }
        if(this.type.equals(ElementalType.FIRE) ||
                this.type.equals(ElementalType.WATER)) {
          modifier = 0.5;
          break;
        }
        break;
      case FIRE:
        if(this.type.equals(ElementalType.WATER)) {
          modifier = 2.0;
          break;
        }
        if(this.type.equals(ElementalType.FIRE) ||
                this.type.equals(ElementalType.GRASS)) {
          modifier = 0.5;
          break;
        }
        break;
      case ELECTRIC:
        if(this.type.equals(ElementalType.ELECTRIC)) {
          modifier = 0.5;
          break;
        }
        break;
      case GRASS:
        if(this.type.equals(ElementalType.FIRE)) {
          modifier = 2.0;
          break;
        }
        if(this.type.equals(ElementalType.GRASS) ||
                this.type.equals(ElementalType.WATER) ||
                this.type.equals(ElementalType.ELECTRIC)) {
          modifier = 0.5;
          break;
        }
        break;
      default:

    }

    return modifier;
  }

  /**
   * Calculates damage based on passed in attack value minus defense (+/- 2)
   * Reduces health by damage.
   * We want the monster to take some damage, so it takes 1 damage by default.
   * @return Damage amount taken for flavortext.
   */
  public int takeDamage(int damage) {
    Random rand = new Random();
    damage = damage - (defense + (rand.nextInt() % 3));
    if(damage <= 0) {
      damage = 1;
    }

    currentHealth -= damage;
    return damage;
  }

  @NonNull
  @Override
  public String toString() {
    return "UserMonster{" +
        "userMonsterId=" + userMonsterId +
        ", nickname='" + nickname + '\'' +
        ", phrase='" + phrase + '\'' +
        ", imageID=" + imageID +
        ", type=" + type +
        ", attack=" + attack +
        ", defense=" + defense +
        ", currentHealth=" + currentHealth +
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
