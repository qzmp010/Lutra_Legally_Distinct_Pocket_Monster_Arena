package com.lutra.legallydistinctpocketmonsterarea.database;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.lutra.legallydistinctpocketmonsterarea.R;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterTypeWithUserMonsters;
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

  @Before
  public void setUp() throws Exception {
    //super.setUp();
    Context context = ApplicationProvider.getApplicationContext();
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    userDAO = db.userDao();
    userMonsterDAO = db.userMonsterDAO();
    monsterTypeDAO = db.monsterTypeDAO();
    userMonsterWithTypeDAO = db.userMonsterWithTypeDAO();
  }

  @After
  public void tearDown() throws Exception {
    //super.tearDown();
    db.close();
  }

  public void testGetDatabase() {
  }

  public void testMonsterTypeDAO() {
  }

  public void testUserMonsterDAO() {
  }

  @Test
  public void testUserMonsterWithTypeDAO() {
    MonsterType mouseyType = new MonsterType(
        "Lightning Mouse",
        "Aa-chooooooo!",
        R.drawable.ld_pikachu,
        10, 7,
        7, 5,
        25, 20,
        UserMonster.ElementalType.ELECTRIC);

    MonsterType dinoType = new MonsterType(
        "Flower Dinoo",
        "Flower power, ya dig?",
        R.drawable.ld_bulbasaur_png,
        8, 5,
        8, 5,
        27, 22,
        UserMonster.ElementalType.GRASS);

    MonsterType turtleType = new MonsterType(
        "Weird Turtley",
        "'Urtle! 'Urtle!'",
        R.drawable.ld_squirtle,
        9, 6,
        7, 4,
        25, 20,
        UserMonster.ElementalType.WATER);

    MonsterType lizardType = new MonsterType(
        "Fire Lizarder",
        "Deal with it.",
        R.drawable.ld_charizard,
        11, 8,
        6, 3,
        23, 16,
        UserMonster.ElementalType.FIRE);

    int userId = 0;
    int mouseyId = (int) monsterTypeDAO.insert(mouseyType);
    int dinoId = (int) monsterTypeDAO.insert(dinoType);
    int lizardId = (int) monsterTypeDAO.insert(lizardType);
    int turtleId = (int) monsterTypeDAO.insert(turtleType);

    mouseyType.setMonsterTypeId(mouseyId);
    dinoType.setMonsterTypeId(dinoId);
    lizardType.setMonsterTypeId(lizardId);
    turtleType.setMonsterTypeId(turtleId);

    UserMonster zappy = new UserMonster(1,"Zappyer", "BUZZZZZZZT", R.drawable.ld_pikachu,
        UserMonster.ElementalType.ELECTRIC, 11, 6,
        35, userId, mouseyId);

    UserMonster plantisaurus = new UserMonster(2,"Plantisaurusy", "Yo, got any grass?",
        R.drawable.ld_bulbasaur_png, UserMonster.ElementalType.GRASS, 10, 7,
        40, userId, dinoId);

    UserMonster flamizord = new UserMonster(3,"Flamizordor", "Burninating the countryside!",
        R.drawable.ld_charizard, UserMonster.ElementalType.FIRE, 13, 4,
        25, userId, lizardId);

    UserMonster warturt = new UserMonster(4,"Warturtle", "I didn't know you liked to get wet!",
        R.drawable.ld_squirtle, UserMonster.ElementalType.WATER, 12, 5,
        30, userId, turtleId);

    UserMonster splashturt = new UserMonster(5,"Splashturty", "I didn't know you liked to get wet!",
        R.drawable.ld_squirtle, UserMonster.ElementalType.WATER, 12, 5,
        30, userId, turtleId);

    zappy.setUserMonsterId((int) userMonsterDAO.insert(zappy));
    plantisaurus.setUserMonsterId((int) userMonsterDAO.insert(plantisaurus));
    flamizord.setUserMonsterId((int) userMonsterDAO.insert(flamizord));
    splashturt.setUserMonsterId((int) userMonsterDAO.insert(splashturt));
    warturt.setUserMonsterId((int) userMonsterDAO.insert(warturt));

    List<MonsterTypeWithUserMonsters> monsterTypesListWithUserMonsters = userMonsterWithTypeDAO.getMonsterTypesWithUserMonsters();

    monsterTypesListWithUserMonsters.forEach(
        m -> Log.d("getMonsterTypesWithUserMonsters", m.toString()));

    Map<UserMonster, MonsterType> userMonsterTypeMap = userMonsterWithTypeDAO.getUserMonstersWithTypeMap();

    userMonsterTypeMap.forEach((k, v) ->
        Log.d("getUserMonstersWithTypeMap", k.toString() + " ---- " + v.toString()));

// TODO: create assert statements
// TODO: make separate test for each DAO method
//
//    assertEquals(userMonsterTypeMap.get(zappy), mouseyType);
//    assertEquals(userMonsterTypeMap.get(plantisaurus), dinoType);
//    assertEquals(userMonsterTypeMap.get(flamizord), lizardType);
//    assertEquals(userMonsterTypeMap.get(splashturt), turtleType);
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

  public void testUserDao() {
  }
}