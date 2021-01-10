package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab2.Database.ActionsDB;
import com.example.lab2.Database.DataBaseProvider;
import com.example.lab2.Database.TimerModel;

public class TimerRemove extends AppCompatActivity {
    private DataBaseProvider db;
    TimerModel timerModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_remove);
        db = App.getInstance().getDatabase();

        Button btn = findViewById(R.id.btnRemove);
        btn.setOnClickListener(i -> {
            remove();
        });
    }

    private void remove(){

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int[] id = (int[])bundle.get("timerId");

        if(id[1] == 1){
            timerModel = db.timerDao().getById(id[0]);
            ActionsDB.actionDelete(timerModel);
        }

        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backIntent);
        finish();
    }
}
