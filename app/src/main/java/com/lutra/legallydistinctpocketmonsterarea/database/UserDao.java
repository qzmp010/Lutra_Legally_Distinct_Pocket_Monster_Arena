package com.lutra.legallydistinctpocketmonsterarea.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);
    @Query("SELECT * FROM users")
    List<User>getAllUsers();

}
