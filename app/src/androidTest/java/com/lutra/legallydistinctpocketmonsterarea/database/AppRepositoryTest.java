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
    repository = null;
  }

  @Test
  public void testGetUserMonsterWithTypeById() {
    int monsterTypeId = (int)repository.insertMonsterType(monsterType);
    int userMonsterId = (int)repository.insertUserMonster(userMonster);
    var monsterEntry = repository.getUserMonsterWithTypeById(MONSTER_ID);
    Log.d("getUserMonsterWithTypeById_Key", monsterEntry.getKey().toString());
    Log.d("getUserMonsterWithTypeById_Value", monsterEntry.getValue().toString());
  }

  @Test
  public void testInsertUserMonster() {
    //Insert monster into DB, record monster ID
    int userMonsterId = (int)repository.insertUserMonster(userMonster);

    //Recall monster from DB to prove that it was inserted
    UserMonster retrievedMonster = repository.getUserMonsterById(userMonsterId);
    assertNotNull(retrievedMonster);

    //Prove that it is identical to the monster we inserted
    assertEquals(userMonster, retrievedMonster);

    //Prove it is identical to a new monster we recall by the same ID
    UserMonster retrievedMonster2 = repository.getUserMonsterById(userMonsterId);
    assertEquals(retrievedMonster, retrievedMonster2);
  }

  @Test
  public void testUpdateUserMonster() {
    //Insert monster into DB, record ID
    int userMonsterId = (int)repository.insertUserMonster(userMonster);

    //Recall an instance of the monster by that ID, prove we were successful.
    UserMonster beforeUpdate = repository.getUserMonsterById(userMonsterId);
    assertNotNull(beforeUpdate);

    //Prove the recalled monster does not have a given max health.
    assertFalse(beforeUpdate.getMaxHealth() == NEW_HEALTH);

    //Set the recalled monster's max health to a given value, and reinsert.
    beforeUpdate.setMaxHealth(NEW_HEALTH);
    repository.insertUserMonster(beforeUpdate);

    //Use our original monster ID to recall the monster and prove that its max health has changed.
    UserMonster afterUpdate = repository.getUserMonsterById(userMonsterId);
    assertTrue(afterUpdate.getMaxHealth() == NEW_HEALTH);
  }

  @Test
  public void testDeleteUserMonster() {
    //Insert monster into DB, record ID
    int userMonsterId = (int)repository.insertUserMonster(userMonster);

    //Recall monster by ID and prove that it exists
    UserMonster goodMonster = repository.getUserMonsterById(userMonsterId);
    assertNotNull(goodMonster);

    //Delete monster by ID from the database
    repository.deleteMonsterByMonsterId(userMonsterId);

    //Prove that we can't recall monster using that ID anymore
    UserMonster badMonster = repository.getUserMonsterById(userMonsterId);
    assertNull(badMonster);
  }
}
