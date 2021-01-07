package com.example.lab2;

import android.app.Application;
import androidx.room.Room;

import com.example.lab2.Database.DataBaseProvider;

public class App extends Application {
    public static App instance;

    private DataBaseProvider database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, DataBaseProvider.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public DataBaseProvider getDatabase() {
        return database;
    }
}
