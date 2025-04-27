package com.lutra.legallydistinctpocketmonsterarea.database;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lutra.legallydistinctpocketmonsterarea.MonsterFactory;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster.ElementalType;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AppRepositoryTest extends TestCase {
  private AppRepository repository;
  private final int MONSTER_ID = 22;
  private final int NEW_HEALTH = 99;
  private MonsterType monsterType;
  private UserMonster userMonster;

  @Before
  public void setUp() throws Exception {
    Application application = ApplicationProvider.getApplicationContext();
    repository = AppRepository.getRepository(application);
    monsterType = new MonsterType(
        "the monster",
        "phrase",
        1,
        2,
        0,
        4,
        3,
        20,
        10,
        ElementalType.ELECTRIC);
    userMonster = new UserMonster(
        MONSTER_ID,
        "my nick",
        "my phrase",
        1234,
        ElementalType.ELECTRIC,
        1,
        2,
        3,
        1,
        1);
  }

  @After
  public void tearDown() throws Exception {
    monsterType = null;
    userMonster = null;
    repository = null;
  }

  @Test
  public void testGetUserMonsterWithTypeById() {
    monsterType.setMonsterTypeId((int)repository.insertMonsterType(monsterType));
    userMonster.setUserMonsterId((int)repository.insertUserMonster(userMonster));
    var monsterEntry = repository.getUserMonsterWithTypeById(MONSTER_ID);
    Log.d("getUserMonsterWithTypeById_Key", monsterEntry.getKey().toString());
    Log.d("getUserMonsterWithTypeById_Value", monsterEntry.getValue().toString());
    assertEquals(monsterEntry.getKey(), userMonster);
    assertEquals(monsterEntry.getValue(), monsterType);
  }
}
