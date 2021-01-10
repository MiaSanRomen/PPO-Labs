package com.example.lab2.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TimerModel {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    public String Name;
    public int Color;
    public int Preparation;
    public int Work;
    public int Rest;
    public int Cycles;
}
