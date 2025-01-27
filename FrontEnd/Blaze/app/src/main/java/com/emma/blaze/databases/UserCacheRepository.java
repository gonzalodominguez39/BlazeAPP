package com.emma.blaze.databases;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.databases.db.AppDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserCacheRepository {
    private UserCacheDao userDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserCacheRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
    }

    public LiveData<UserCache> getLoggedInUser() {
        return userDao.getLoggedInUser();
    }

    public void createUserCache(UserResponse user) {
        UserCache userCache = new UserCache();
        userCache.setEmail(user.getEmail());
        userCache.setName(user.getName());
        userCache.setLoggedIn(true);
        insert(userCache);
    }

    public LiveData<UserCache> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public void insert(UserCache user) {
        executorService.execute(() -> userDao.insert(user));
    }

    public void update(UserCache user) {
        executorService.execute(() -> userDao.update(user));
    }

    public void deleteAll() {
        executorService.execute(userDao::deleteAll);
    }

    public void removeUser(long id) {
        executorService.execute(() -> userDao.removeUserById(id));
    }
}