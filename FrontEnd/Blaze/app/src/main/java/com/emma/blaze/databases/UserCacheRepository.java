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
    private MutableLiveData<UserCache> loggedInUser;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserCacheRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        loggedInUser= new MutableLiveData<>();
        userDao.getLoggedInUser().observeForever(userCache -> {
            if (userCache != null) {
                loggedInUser.setValue(userCache);
            }
        });
    }


    public LiveData<UserCache> getLoggedInUser() {
        return loggedInUser;
    }

    public void createUserCache(UserResponse user){
        if(loggedInUser.getValue()!=null){
            loggedInUser.getValue().setLoggedIn(false);
            update(loggedInUser.getValue());
        }
        UserCache userCache = new UserCache();
        userCache.setEmail(user.getEmail());
        userCache.setName(user.getName());
        userCache.setLoggedIn(true);
        insert(userCache);
    }

    public LiveData <UserCache> getUserByEmail(String email) {
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
}
