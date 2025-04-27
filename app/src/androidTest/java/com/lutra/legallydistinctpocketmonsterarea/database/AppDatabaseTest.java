package com.lutra.legallydistinctpocketmonsterarea.database;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.lutra.legallydistinctpocketmonsterarea.R;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest extends TestCase {

  private AppDatabase db;
  private UserDAO userDAO;
  private UserMonsterDAO userMonsterDAO;
  private MonsterTypeDAO monsterTypeDAO;
  private UserMonsterWithTypeDAO userMonsterWithTypeDAO;

  private static final int NEW_HEALTH = 99;
  private UserMonster userMonster;

  int userId;
  private User user;
  private MonsterType mouseyType;
  private MonsterType dinoType;
  private UserMonster zappy;
  private UserMonster plantisaurus;

  @Before
  public void setUp() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    userDAO = db.userDao();
    userMonsterDAO = db.userMonsterDAO();
    monsterTypeDAO = db.monsterTypeDAO();
    userMonsterWithTypeDAO = db.userMonsterWithTypeDAO();
    userId = 42;

    user = new User(
        "MonsterTrainer",
        "secret",
        false);

    userMonster = new UserMonster(
            420,
            "my nick",
            "my phrase",
            1234,
            UserMonster.ElementalType.ELECTRIC,
            1,
            2,
            3,
            1,
            -99);

    mouseyType = new MonsterType(
        "Lightning Mouse",
        "Aa-chooooooo!",
        R.drawable.ld_pikachu,
        10, 7,
        7, 5,
        25, 20,
        UserMonster.ElementalType.ELECTRIC);

    dinoType = new MonsterType(
        "Flower Dinoo",
        "Flower power, ya dig?",
        R.drawable.ld_bulbasaur_png,
        8, 5,
        8, 5,
        27, 22,
        UserMonster.ElementalType.GRASS);
  }

  @After
  public void tearDown() throws Exception {
    userId = 0;
    userMonster = null;
    mouseyType = null;
    dinoType = null;
    zappy = null;
    plantisaurus = null;

    db.close();
  }

  @Test
  public void testDeleteMonsterByMonsterId() {
    long userMonsterId = userMonsterDAO.insert(new UserMonster(1,"Zappyer", "BUZZZZZZZT", R.drawable.ld_pikachu,
        UserMonster.ElementalType.ELECTRIC, 11, 6,
        35, 0, 0));
    assertNotNull(userMonsterDAO.getMonsterByMonsterId((int)userMonsterId));
    userMonsterDAO.deleteMonsterByMonsterId((int)userMonsterId);
    assertNull(userMonsterDAO.getMonsterByMonsterId((int)userMonsterId));
  }

  @Test
  public void testInsertUserMonster() {
    //Insert monster into DB, record monster ID
    int userMonsterId = (int)userMonsterDAO.insert(userMonster);

    //Recall monster from DB to prove that it was inserted
    UserMonster retrievedMonster = userMonsterDAO.getMonsterByMonsterId(userMonsterId);
    assertNotNull(retrievedMonster);

    //Prove that it is identical to the monster we inserted
    assertEquals(userMonster, retrievedMonster);

    //Prove it is identical to a new monster we recall by the same ID
    UserMonster retrievedMonster2 = userMonsterDAO.getMonsterByMonsterId(userMonsterId);
    assertEquals(retrievedMonster, retrievedMonster2);
  }

  @Test
  public void testUpdateUserMonster() {
    //Insert monster into DB, record ID
    int userMonsterId = (int)userMonsterDAO.insert(userMonster);

    //Recall an instance of the monster by that ID, prove we were successful.
    UserMonster beforeUpdate = userMonsterDAO.getMonsterByMonsterId(userMonsterId);
    assertNotNull(beforeUpdate);

    //Prove the recalled monster does not have a given max health.
    assertFalse(beforeUpdate.getMaxHealth() == NEW_HEALTH);

    //Set the recalled monster's max health to a given value, and reinsert.
    beforeUpdate.setMaxHealth(NEW_HEALTH);
    userMonsterDAO.insert(beforeUpdate);

    //Use our original monster ID to recall the monster and prove that its max health has changed.
    UserMonster afterUpdate = userMonsterDAO.getMonsterByMonsterId(userMonsterId);
    assertTrue(afterUpdate.getMaxHealth() == NEW_HEALTH);
  }

  @Test
  public void testDeleteUserMonster() {
    //Insert monster into DB, record ID
    int userMonsterId = (int)userMonsterDAO.insert(userMonster);

    //Recall monster by ID and prove that it exists
    UserMonster goodMonster = userMonsterDAO.getMonsterByMonsterId(userMonsterId);
    assertNotNull(goodMonster);

    //Delete monster by ID from the database
    userMonsterDAO.deleteMonsterByMonsterId(userMonsterId);

    //Prove that we can't recall monster using that ID anymore
    UserMonster badMonster = userMonsterDAO.getMonsterByMonsterId(userMonsterId);
    assertNull(badMonster);
  }

  @Test
  public void testGetUserMonstersWithTypeMap() {
    int mouseyId = (int) monsterTypeDAO.insert(mouseyType);
    int dinoId = (int) monsterTypeDAO.insert(dinoType);

    mouseyType.setMonsterTypeId(mouseyId);
    dinoType.setMonsterTypeId(dinoId);

    zappy = new UserMonster(0,"Zappyer", "BUZZZZZZZT", R.drawable.ld_pikachu,
        UserMonster.ElementalType.ELECTRIC, 11, 6,
        35, userId, mouseyId);

    plantisaurus = new UserMonster(0,"Plantisaurusy", "Yo, got any grass?",
        R.drawable.ld_bulbasaur_png, UserMonster.ElementalType.GRASS, 10, 7,
        40, userId, dinoId);

    zappy.setUserMonsterId((int) userMonsterDAO.insert(zappy));
    plantisaurus.setUserMonsterId((int) userMonsterDAO.insert(plantisaurus));

    Map<UserMonster, MonsterType> userMonsterTypeMap = userMonsterWithTypeDAO.getUserMonstersWithTypeMap();

    assertEquals(userMonsterTypeMap.get(zappy), mouseyType);
    assertEquals(userMonsterTypeMap.get(plantisaurus), dinoType);
  }

  @Test
  public void testUserInsert() {
    List<User> noUser = userDAO.getAllUsersSync();
    assertTrue(noUser.isEmpty());

    long myUserId = userDAO.insert(user);
    user.setId((int) myUserId);

    List<User> myUsers = userDAO.getAllUsersSync();
    assertEquals(user, myUsers.getLast());
  }

  @Test
  public void testGetAllUsers() {
    List<User> myUsers = userDAO.getAllUsersSync();
    assertTrue(myUsers.isEmpty());

    long myUserId = userDAO.insert(user);
    user.setId((int) myUserId);

    User user2 = new User(
        "user2",
        "secret2",
          true);

    user2.setId((int) userDAO.insert(user2));

    myUsers = userDAO.getAllUsersSync();
    assertTrue(myUsers.containsAll(List.of(user, user2)));
  }

  @Test
  public void testUserDelete() {
    List<User> myUsers = userDAO.getAllUsersSync();
    assertTrue(myUsers.isEmpty());

    long myUserId = userDAO.insert(user);
    user.setId((int) myUserId);

    myUsers = userDAO.getAllUsersSync();
    assertEquals(user, myUsers.getLast());

    userDAO.delete(user);
    myUsers = userDAO.getAllUsersSync();
    assertTrue(myUsers.isEmpty());
  }
}