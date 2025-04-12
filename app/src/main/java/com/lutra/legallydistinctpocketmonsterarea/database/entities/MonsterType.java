package com.lutra.legallydistinctpocketmonsterarea.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.lutra.legallydistinctpocketmonsterarea.database.AppDatabase;
import java.util.Objects;

@Entity(tableName = AppDatabase.MONSTER_TYPE_TABLE)
public class MonsterType {
  @PrimaryKey(autoGenerate = true)
  private int monsterTypeId;
  @NonNull
  private String monsterTypeName;
  private String phrase;
  private int attackMax;
  private int attackMin;
  private int defenseMax;
  private int defenseMin;
  private int healthMax;
  private int healthMin;
  private ElementalType elementalType;
  private String imageFilePath;

  public MonsterType(@NonNull String monsterTypeName, String phrase, int attackMax, int attackMin,
      int defenseMax, int defenseMin, int healthMax, int healthMin, ElementalType elementalType) {
    this.monsterTypeName = monsterTypeName;
    this.phrase = phrase;
    this.attackMax = attackMax;
    this.attackMin = attackMin;
    this.defenseMax = defenseMax;
    this.defenseMin = defenseMin;
    this.healthMax = healthMax;
    this.healthMin = healthMin;
    this.elementalType = elementalType;
  }

  public int getMonsterTypeId() {
    return monsterTypeId;
  }

  public void setMonsterTypeId(int monsterTypeId) {
    this.monsterTypeId = monsterTypeId;
  }

  @NonNull
  public String getMonsterTypeName() {
    return monsterTypeName;
  }

  public void setMonsterTypeName(@NonNull String monsterTypeName) {
    this.monsterTypeName = monsterTypeName;
  }

  public String getPhrase() {
    return phrase;
  }

  public void setPhrase(String phrase) {
    this.phrase = phrase;
  }

  public int getAttackMax() {
    return attackMax;
  }

  public void setAttackMax(int attackMax) {
    this.attackMax = attackMax;
  }

  public int getAttackMin() {
    return attackMin;
  }

  public void setAttackMin(int attackMin) {
    this.attackMin = attackMin;
  }

  public int getDefenseMax() {
    return defenseMax;
  }

  public void setDefenseMax(int defenseMax) {
    this.defenseMax = defenseMax;
  }

  public int getDefenseMin() {
    return defenseMin;
  }

  public void setDefenseMin(int defenseMin) {
    this.defenseMin = defenseMin;
  }

  public int getHealthMax() {
    return healthMax;
  }

  public void setHealthMax(int healthMax) {
    this.healthMax = healthMax;
  }

  public int getHealthMin() {
    return healthMin;
  }

  public void setHealthMin(int healthMin) {
    this.healthMin = healthMin;
  }

  public ElementalType getElementalType() {
    return elementalType;
  }

  public void setElementalType(
      ElementalType elementalType) {
    this.elementalType = elementalType;
  }

  public String getImageFilePath() {
    return imageFilePath;
  }

  public void setImageFilePath(String imageFilePath) {
    this.imageFilePath = imageFilePath;
  }

  @NonNull
  @Override
  public String toString() {
    return "MonsterType{" +
        "monsterTypeName='" + monsterTypeName + '\'' +
        ", phrase='" + phrase + '\'' +
        ", attackMax=" + attackMax +
        ", attackMin=" + attackMin +
        ", defenseMax=" + defenseMax +
        ", defenseMin=" + defenseMin +
        ", healthMax=" + healthMax +
        ", healthMin=" + healthMin +
        ", elementalType=" + elementalType +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonsterType that = (MonsterType) o;
    return monsterTypeId == that.monsterTypeId && attackMax == that.attackMax
        && attackMin == that.attackMin && defenseMax == that.defenseMax
        && defenseMin == that.defenseMin && healthMax == that.healthMax
        && healthMin == that.healthMin
        && Objects.equals(monsterTypeName, that.monsterTypeName)
        && Objects.equals(phrase, that.phrase) && elementalType == that.elementalType
        && Objects.equals(imageFilePath, that.imageFilePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(monsterTypeId, monsterTypeName, phrase, attackMax, attackMin, defenseMax,
        defenseMin, healthMax, healthMin, elementalType, imageFilePath);
  }

  public enum ElementalType {
    NORMAL,
    ELECTRIC,
    FIRE,
    GRASS,
    WATER,
  }
}
