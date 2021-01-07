package com.example.lab2.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TimerModel.class}, version = 1)
public abstract class DataBaseProvider extends RoomDatabase {
    public abstract TimerDB timerDao();
}
