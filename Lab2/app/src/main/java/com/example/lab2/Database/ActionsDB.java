package com.example.lab2.Database;

public class ActionsDB {
    private static DataBaseProvider db;

    public static void setDb(DataBaseProvider db) {
        ActionsDB.db = db;
    }
    public static void actionDelete(TimerModel timerModel){
        db.timerDao().delete(timerModel);
    }
}
