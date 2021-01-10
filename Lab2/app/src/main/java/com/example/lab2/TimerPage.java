package com.example.lab2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab2.Database.DataBaseProvider;
import com.example.lab2.Database.TimerModel;

import java.util.ArrayList;
import java.util.Objects;

public class TimerPage extends AppCompatActivity {

    private DataBaseProvider db;
    private TextView Step;
    private TextView Time;
    private TimerModel Model;
    BroadcastReceiver receiver;
    ListView listTraining;
    Button startStop;
    ArrayList<String> TrainingSteps = new ArrayList();
    ArrayAdapter<String> adapter;

    public final static String PARAM_START_TIME = "start_time";
    public final static String NAME_ACTION = "name";
    public final static String TIME_ACTION = "time";
    public final static String CURRENT_ACTION = "pause";
    public final static String BROADCAST_ACTION = "com.example.lab2";
    int element = 0;
    boolean check_last_sec = false;
    String value_status_pause = "";
    String value_time_pause = "";
    int value_element_pause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_page);

        db = App.getInstance().getDatabase();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        int idT = (int)bundle.get("timerId");
        Model = db.timerDao().getById(idT);

        startStop = findViewById(R.id.btnStart);
        listTraining = findViewById(R.id.allParts);
        Step = findViewById(R.id.partName);
        Time = findViewById(R.id.partTime);

        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (Objects.equals(intent.getStringExtra(CURRENT_ACTION), "work")) {
                    String task = intent.getStringExtra(NAME_ACTION);
                    String status = intent.getStringExtra(TIME_ACTION);
                    assert status != null;
                    if (status.equals("1")) {
                        workLastSec();
                    } else {
                        workInProgress(task);
                    }
                    Step.setText(task);
                    Time.setText(status);
                } else if (Objects.equals(intent.getStringExtra(CURRENT_ACTION), "clear")) {
                    clear();
                } else {
                    value_status_pause = intent.getStringExtra(NAME_ACTION);
                    value_time_pause = intent.getStringExtra(TIME_ACTION);
                    assert value_time_pause != null;
                    startPause(value_time_pause);
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(receiver, intentFilter);

        fillAdapter(Model);

        startStop.setOnClickListener(this::onStartClick);
        startStop.setBackground(getResources().getDrawable(R.drawable.flag));


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TrainingSteps);
        listTraining.setAdapter(adapter);
        listTraining.setOnItemClickListener((parent, view, position, id) -> ChangeFieldListView(view, position));
        listTraining.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                for (int i = 0; i < visibleItemCount; i++) {
                    listTraining.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
                }
                if (element >= firstVisibleItem && element < firstVisibleItem + visibleItemCount && !check_last_sec) {
                    listTraining.getChildAt(element - firstVisibleItem).setBackgroundColor(getResources().getColor(R.color.colorAccent,getTheme()));
                }
                if (element - 1 >= firstVisibleItem && element - 1 < firstVisibleItem + visibleItemCount && check_last_sec) {
                    listTraining.getChildAt(element - 1 - firstVisibleItem).setBackgroundColor(getResources().getColor(R.color.colorAccent,getTheme()));
                }
            }
        });
    }

    public void clear() {
        if (element != 0 && element - 1 - listTraining.getFirstVisiblePosition() < 14 && element - 1 - listTraining.getFirstVisiblePosition() >= 0 && element != 0)
            listTraining.getChildAt(element - listTraining.getFirstVisiblePosition() - 1).setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
        if (element - listTraining.getFirstVisiblePosition() < 14 && element - 1 - listTraining.getFirstVisiblePosition() >= 0)
            listTraining.getChildAt(element - listTraining.getFirstVisiblePosition()).setBackgroundColor(getResources().getColor(R.color.colorAccent,getTheme()));
    }

    public void workInProgress(String task) {
        if (check_last_sec) {
            check_last_sec = false;
        }
        value_status_pause = "";
        if (task.equals(getResources().getString(R.string.Finish))) {
            startStop.setOnClickListener(TimerPage.this::onStartClick);
            startStop.setBackground(getResources().getDrawable(R.drawable.flag));
        }
    }

    public void workLastSec() {
        element++;
        check_last_sec = true;
        if (element < adapter.getCount()) {
            String[] words = Objects.requireNonNull(adapter.getItem(element)).split(" : ");
            if (words.length == 2)
                newService(words[1], "0");
            else
                newService(words[1], words[2]);
        }
    }

    public void startPause(String status) {
        if (status.equals("1")) {
            if (!check_last_sec)
                element--;
            else
                check_last_sec = false;
            String[] words = Objects.requireNonNull(adapter.getItem(element)).split(" : ");
            value_status_pause = words[1];
        }
        value_element_pause = element;
    }

    public void fillAdapter(TimerModel workout) {
        int number = 1;
        if (workout.Preparation != 0)
            TrainingSteps.add(StringForTimer(number++, getResources().getString(R.string.Prepare), workout.Preparation));
        int cycle = workout.Cycles;while (cycle > 0) {
            TrainingSteps.add(StringForTimer(number++, getResources().getString(R.string.Work), workout.WorkTime));
            TrainingSteps.add(StringForTimer(number++, getResources().getString(R.string.Calm), workout.RestTime));
            cycle--;
        }
        TrainingSteps.add(number + " : " + getResources().getString(R.string.Finish));

    }

    public String StringForTimer(int number, String name, Integer time) {
        return number + " : " + name + " : " + time;
    }

    public void ChangeFieldListView(View view, int position) {
        for (int i = 0; i < listTraining.getLastVisiblePosition() - listTraining.getFirstVisiblePosition() + 1; i++) {
            listTraining.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
        }
        element = position;
        view.setBackgroundColor(getResources().getColor(R.color.colorAccent,getTheme()));
        stopService(new Intent(this, Timer.class));
        String[] words = Objects.requireNonNull(adapter.getItem(position)).split(" : ");
        if (words.length == 2) {
            startStop.setBackground(getResources().getDrawable(R.drawable.flag));
            startStop.setOnClickListener(this::onStartClick);
            newService(words[1], "0");
        } else {
            startStop.setOnClickListener(this::onResetClick);
            startStop.setBackground(getResources().getDrawable(R.drawable.stop));
            newService(words[1], words[2]);
        }
    }

    public void newService(String name, String time) {
        startService(new Intent(this, Timer.class).putExtra(PARAM_START_TIME, time)
                .putExtra(NAME_ACTION, name));
    }

    public void onStartClick(View view) {
        if (value_status_pause.isEmpty() || value_status_pause.equals(getResources().getString(R.string.Finish))) {
            element = 0;
            check_last_sec = false;
            stopService(new Intent(this, Timer.class));
            newService(getResources().getString(R.string.Prepare), String.valueOf(Model.Preparation));
            listTraining.getChildAt(listTraining.getLastVisiblePosition() - listTraining.getFirstVisiblePosition()).setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
            if (element >= listTraining.getFirstVisiblePosition() && element <= listTraining.getLastVisiblePosition())
                listTraining.getChildAt(element).setBackgroundColor(getResources().getColor(R.color.colorAccent,getTheme()));
        } else {
            element = value_element_pause;
            newService(value_status_pause, value_time_pause);
        }
        startStop.setOnClickListener(this::onResetClick);
        startStop.setBackground(getResources().getDrawable(R.drawable.stop));
    }

    public void onResetClick(View view) {
        startStop.setOnClickListener(this::onStartClick);
        startStop.setBackground(getResources().getDrawable(R.drawable.flag));
        stopService(new Intent(this, Timer.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, Timer.class));
    }


    @Override
    public void onBackPressed() {
        quitDialog();
    }

    private void quitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                TimerPage.this);
        quitDialog.setTitle(getResources().getString(R.string.Exit));
        quitDialog.setPositiveButton(getResources().getString(R.string.Yes), (dialog, which) -> {
            unregisterReceiver(receiver);
            stopService(new Intent(this, Timer.class));
            finish();
        });
        quitDialog.setNegativeButton(getResources().getString(R.string.No), (dialog, which) -> {
        });
        quitDialog.show();
    }
}
