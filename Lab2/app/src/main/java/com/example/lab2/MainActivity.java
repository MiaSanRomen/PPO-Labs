package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab2.Database.DataBaseProvider;
import com.example.lab2.Database.TimerModel;

public class MainActivity extends AppCompatActivity {

    DataBaseProvider db;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = App.getInstance().getDatabase();



        listView = findViewById(R.id.ListTimer);
        TimerAdapter adapter = new TimerAdapter(this, R.layout.timers
                , db.timerDao().getAll(), db);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                TimerModel training = (TimerModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), TimerPage.class);
                intent.putExtra("timerId", training.Id);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonAddTimer).setOnClickListener(i -> {
            Intent intent = new Intent(getApplicationContext(), TimerCreate.class);
            intent.putExtra("timerId", new int[]{0,0});
            startActivity(intent);
        });

        findViewById(R.id.btnSettings).setOnClickListener(i -> {
            Intent Settings = new Intent(this, com.example.lab2.Settings.class);
            startActivityForResult(Settings, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
    }
}