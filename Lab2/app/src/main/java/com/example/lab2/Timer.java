package com.example.lab2;

import android.app.Service;
import android.content.Intent;
import android.media.SoundPool;
import android.os.IBinder;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Timer extends Service {

    ScheduledExecutorService service;
    SoundPool soundPool;
    int soundReady;
    int soundFinish;
    int current_time;

    String name;
    ScheduledFuture<?> scheduledFuture;

    public void onCreate() {
        super.onCreate();
        soundPool = new SoundPool.Builder()
                            .setMaxStreams(5)
                            .build();
        soundReady = soundPool.load(this, R.raw.censore_preview, 1);
        soundFinish = soundPool.load(this, R.raw.final_sound, 1);
        service = Executors.newScheduledThreadPool(1);
    }

    public void onDestroy() {
        service.shutdownNow();
        scheduledFuture.cancel(true);
        Intent intent = new Intent(TimerPage.BROADCAST_ACTION);
        intent.putExtra(TimerPage.CURRENT_ACTION, "pause");
        intent.putExtra(TimerPage.NAME_ACTION, name);
        intent.putExtra(TimerPage.TIME_ACTION, Integer.toString(current_time));
        sendBroadcast(intent);
        super.onDestroy();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        int time = Integer.parseInt(intent.getStringExtra(TimerPage.PARAM_START_TIME));
        name = intent.getStringExtra(TimerPage.NAME_ACTION);
        MyTimerTask mr = new MyTimerTask(startId, time, name);
        if (scheduledFuture != null) {
            service.schedule(() -> {
                scheduledFuture.cancel(true);
                scheduledFuture = service.scheduleAtFixedRate(mr, 0, time + 1, TimeUnit.SECONDS);
            }, 1000, TimeUnit.MILLISECONDS);
        } else {
            scheduledFuture = service.scheduleAtFixedRate(mr, 0, time + 1, TimeUnit.SECONDS);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    class MyTimerTask extends TimerTask {

        int time;
        int startId;
        String name;

        public MyTimerTask(int startId, int time, String name) {
            this.time = time;
            this.startId = startId;
            this.name = name;
        }


        public void run() {
            Intent intent = new Intent(TimerPage.BROADCAST_ACTION);
            if (name.equals(getResources().getString(R.string.Finish))) {
                intent.putExtra(TimerPage.CURRENT_ACTION, "work");
                intent.putExtra(TimerPage.NAME_ACTION, name);
                intent.putExtra(TimerPage.TIME_ACTION, "");
                sendBroadcast(intent);
            }
            try {
                for (current_time = time; current_time > 0; current_time--) {
                    intent.putExtra(TimerPage.CURRENT_ACTION, "work");
                    intent.putExtra(TimerPage.NAME_ACTION, name);
                    intent.putExtra(TimerPage.TIME_ACTION, Integer.toString(current_time));
                    sendBroadcast(intent);
                    TimeUnit.SECONDS.sleep(1);
                    signal(current_time);
                }
                intent = new Intent(TimerPage.BROADCAST_ACTION);
                intent.putExtra(TimerPage.CURRENT_ACTION, "clear");
                sendBroadcast(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void signal(int time) {
            if (time <= 5) {
                if (time == 1)
                    soundPool.play(soundFinish, 1, 1, 0, 0, 1);
                else
                    soundPool.play(soundReady, 1, 1, 0, 0, 1);
            }
        }

    }
}