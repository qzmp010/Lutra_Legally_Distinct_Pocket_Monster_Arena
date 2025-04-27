package com.lutra.legallydistinctpocketmonsterarea.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " ORDER BY username")
    List<User> getAllUsersSync();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM " +AppDatabase.USER_TABLE )
    void deleteAll();

    @Query("SELECT * from " + AppDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * from " + AppDatabase.USER_TABLE + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);
    @Query("SELECT * from " + AppDatabase.USER_TABLE + " WHERE id == :userId")
    User getUserByUserIdBlocking(int userId);
}
