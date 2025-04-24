package com.lutra.legallydistinctpocketmonsterarea.database;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterTypeWithUserMonsters;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppRepository {

  public static final String LOG_TAG = "com.lutra.ldpm.logs";
  private final MonsterTypeDAO monsterTypeDAO;
  private final UserMonsterDAO userMonsterDAO;
  private final UserMonsterWithTypeDAO userMonsterWithTypeDAO;
  private final UserDAO userDao;
  private static AppRepository repository;

  private AppRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    this.monsterTypeDAO = db.monsterTypeDAO();
    this.userMonsterDAO = db.userMonsterDAO();
    this.userMonsterWithTypeDAO = db.userMonsterWithTypeDAO();
    this.userDao = db.userDao();
  }

  public static AppRepository getRepository(Application application) {
    if (repository != null) {
      return repository;
    }

    Future<AppRepository> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<AppRepository>() {
          @Override
          public AppRepository call() throws Exception {
            return new AppRepository(application);
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem getting AppRepository; thread error");
    }
    return null;
  }

  /**
   * Inserts or updates MonsterType
   * Inserts new entry if monsterTypeId is null or unique
   * Updates existing entry if monsterTypeId exists
   * @param monsterType to insert or update
   * @return monsterTypeId of inserted MonsterType
   */
  public long insertMonsterType(MonsterType monsterType) {
    Future<Long> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<Long>() {
          @Override
          public Long call() throws Exception {
            return monsterTypeDAO.insert(monsterType);
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem inserting MonsterType into repository");
    }
    return 0L;
  }

  public ArrayList<MonsterType> getAllMonsterTypes() {
    Future<ArrayList<MonsterType>> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<ArrayList<MonsterType>>() {
          @Override
          public ArrayList<MonsterType> call() throws Exception {
            return (ArrayList<MonsterType>) monsterTypeDAO.getAll();
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem getting all MonsterTypes from repository");
    }
    return null;
  }

  public LiveData<List<MonsterType>> getAllMonsterTypesLiveData() {
    return monsterTypeDAO.getAllLiveData();
  }

  public MonsterType getMonsterTypeById(int monsterTypeId) {
    Future<MonsterType> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<MonsterType>() {
          @Override
          public MonsterType call() throws Exception {
            return monsterTypeDAO.getByMonsterTypeId(monsterTypeId);
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem getting MonsterType by ID from repository");
    }
    return null;
  }

  /**
   * Inserts or updates UserMonster
   * Inserts new entry if userMonsterId is null or unique
   * Updates existing entry if userMonsterId exists
   * @param userMonster to insert or update
   * @return userMonsterId of inserted UserMonster
   */
  public long insertUserMonster(UserMonster userMonster) {
    Future<Long> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<Long>() {
          @Override
          public Long call() throws Exception {
            return userMonsterDAO.insert(userMonster);
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem inserting UserMonster into repository");
    }
    return 0L;
  }

    public UserMonster getUserMonsterById(int userMonsterId) {
        Future<UserMonster> future = AppDatabase.databaseWriteExecutor.submit(
                new Callable<UserMonster>() {
                    @Override
                    public UserMonster call() throws Exception {
                        return userMonsterDAO.getMonsterByMonsterId(userMonsterId);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(LOG_TAG, "Problem getting UserMonster by ID from repository");
        }
        return null;
    }

    public Entry<UserMonster, MonsterType> getUserMonsterWithTypeById(int userMonsterId) {
      Future<Entry<UserMonster, MonsterType>> future = AppDatabase.databaseWriteExecutor.submit(
          new Callable<Entry<UserMonster, MonsterType>>() {
            @Override
            public Entry<UserMonster, MonsterType> call() throws Exception {
              return userMonsterWithTypeDAO
                  .getUserMonsterWithTypeMapByUserMonsterId(userMonsterId)
                  .entrySet().stream().findFirst().orElse(new SimpleEntry<>(null, null));
            }
          }
      );
      try {
        return future.get();
      } catch (InterruptedException | ExecutionException e) {
        Log.i(LOG_TAG, "Problem getting UserMonster with MonsterType by userMonsterId: " + userMonsterId);
      }
      return null;
    }

  public ArrayList<UserMonster> getAllUserMonsters() {
    Future<ArrayList<UserMonster>> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<ArrayList<UserMonster>>() {
          @Override
          public ArrayList<UserMonster> call() throws Exception {
            return (ArrayList<UserMonster>) userMonsterDAO.getAll();
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem getting all UserMonsters from repository");
    }
    return null;
  }

  public ArrayList<UserMonster> getUserMonstersByUserId(int userId) {
    Future<ArrayList<UserMonster>> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<ArrayList<UserMonster>>() {
          @Override
          public ArrayList<UserMonster> call() throws Exception {
            return (ArrayList<UserMonster>) userMonsterDAO.getByUserId(userId);
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem getting UserMonsters by userId from repository");
    }
    return null;
  }

  public LiveData<List<UserMonster>> getUserMonstersByUserIdLiveData(int userId) {
    return userMonsterDAO.getByUserIdLiveData(userId);
  }

  public boolean deleteMonsterByMonsterId(int monsterID) {
    Future future = AppDatabase.databaseWriteExecutor.submit(
        new Callable() {
          @Override
          public Object call() throws Exception {
            userMonsterDAO.deleteMonsterByMonsterId(monsterID);
            return null;
          }
        }
    );
    try {
      future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem deleting UserMonster with userMonsterId " + monsterID);
    }
    return true;
  }

  public LiveData<User> getUserByUserName(String username) {
    return userDao.getUserByUserName(username);
  }

  public LiveData<User> getUserByUserId(int userId) {
    return userDao.getUserByUserId(userId);
  }

  /**
   * Gets all UserMonsters with MonsterType
   * @return ArrayList with Entry containing UserMonster as key and MonsterType as value
   */
  public List<Entry<UserMonster, MonsterType>> getUserMonstersWithTypeList() {
    Future<List<Entry<UserMonster, MonsterType>>> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<List<Entry<UserMonster, MonsterType>>>() {
          @Override
          public List<Entry<UserMonster, MonsterType>> call() throws Exception {
            return new ArrayList<>(userMonsterWithTypeDAO.getUserMonstersWithTypeMap().entrySet());
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem getting UserMonsters with MonsterType from repository");
    }
    return null;
  }

  public LiveData<List<Entry<UserMonster, MonsterType>>> getUserMonstersWithTypeListLiveData() {
    return Transformations.map(
        userMonsterWithTypeDAO.getUserMonstersWithTypeMapLiveData(),
        m -> new ArrayList<>(m.entrySet())
    );
  }

  /**
   * Gets UserMonsters with MonsterType for userId
   * @param userId of user in which to retrieve UserMonsters
   * @return ArrayList with Entry containing UserMonster as key and MonsterType as value
   */
  public List<Entry<UserMonster, MonsterType>> getUserMonsterWithTypeListByUserId(int userId) {
    Future<List<Entry<UserMonster, MonsterType>>> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<List<Entry<UserMonster, MonsterType>>>() {
          @Override
          public List<Entry<UserMonster, MonsterType>> call() throws Exception {
            return new ArrayList<>(
                userMonsterWithTypeDAO.getUserMonstersWithTypeMapByUserId(userId).entrySet()
            );
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem getting UserMonsters with MonsterType by userId from repository");
    }
    return null;
  }

  public LiveData<List<Entry<UserMonster, MonsterType>>> getUserMonsterWithTypeListByUserIdLiveData(int userId) {
    return Transformations.map(
        userMonsterWithTypeDAO.getUserMonstersWithTypeMapByUserIdLiveData(userId),
        m -> new ArrayList<>(m.entrySet())
    );
  }

  public List<MonsterTypeWithUserMonsters> getMonsterTypesWithUserMonsters() {
    Future<ArrayList<MonsterTypeWithUserMonsters>> future = AppDatabase.databaseWriteExecutor.submit(
        new Callable<ArrayList<MonsterTypeWithUserMonsters>>() {
          @Override
          public ArrayList<MonsterTypeWithUserMonsters> call() throws Exception {
            return (ArrayList<MonsterTypeWithUserMonsters>) userMonsterWithTypeDAO.getMonsterTypesWithUserMonsters();
          }
        }
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(LOG_TAG, "Problem getting MonsterTypes with UserMonsters from repository");
    }
    return null;
  }

  public LiveData<List<MonsterTypeWithUserMonsters>> getMonsterTypesWithUserMonstersLiveData() {
    return userMonsterWithTypeDAO.getMonsterTypesWithUserMonstersLiveData();
  }

  public void insertUser(User user) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      userDao.insert(user);
    });
  }
}
