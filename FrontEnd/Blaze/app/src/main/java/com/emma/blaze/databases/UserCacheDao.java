package com.emma.blaze.databases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserCacheDao {
    @Insert
    void insert(UserCache user);

    @Update
    void update(UserCache user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<UserCache> getLoggedInUser();
}
