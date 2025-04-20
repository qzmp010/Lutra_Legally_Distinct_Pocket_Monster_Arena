package com.lutra.legallydistinctpocketmonsterarea.database;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;

import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppRepository {
  public static final String LOG_TAG = "com.lutra.ldpm.logs";
  private final MonsterTypeDAO monsterTypeDAO;
  private final UserMonsterDAO userMonsterDAO;
  private final UserDAO userDao;
  private static AppRepository repository;

  private AppRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    this.monsterTypeDAO = db.monsterTypeDAO();
    this.userMonsterDAO = db.userMonsterDAO();
    this.userDao = db.userDao();
  }

    /**
     * Parameterless version of getRepository() for use with MonsterFactory class.
     * Needed because we can't pass in an application context outside of an activity, and we need
     * to be able to call it from a static method. Should function exactly the same if repository
     * has already been initialized.
     * @return The current repository.
     */
  public static AppRepository getRepository() {
      try {
          return repository;
      } catch (NullPointerException e) {
          Log.e(LOG_TAG, "Error: repository could not be retrieved.");
          return null;
      }
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
    } catch (InterruptedException |ExecutionException e) {
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
    } catch (InterruptedException |ExecutionException e) {
      Log.i(LOG_TAG, "Problem inserting UserMonster into repository");
    }
    return 0L;
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



    public LiveData<User> getUserByUserName(String username) {
        return userDao.getUserByUserName(username);
    }
    public LiveData<User> getUserByUserId(int userId) {
        return userDao.getUserByUserId(userId);
    }

  public LiveData<List<UserMonster>> getByUserIdLiveData(int userId) {
    return userMonsterDAO.getByUserIdLiveData(userId);
  }

  public void insertUser(User user) {
      AppDatabase.databaseWriteExecutor.execute(() -> {
          userDao.insert(user);
      });
  }

}
