package com.example.a3lab_holidays.Room;

import com.example.a3lab_holidays.User;

import java.util.UUID;

public class Game {
    public static User userFirst;
    public static User userSecond;
    public static String Id;
    public static boolean isReadyMe, isReadySecond;
    public static String userPath, myUserPath;
    public static boolean[][] fieldMy = new boolean[8][8];
    public static boolean[][] fieldEnemy = new boolean[8][8];
    public static UserEnum myUser;
    public static String myUsername;


    Game() {

    }

    public Game(User user) {
        userFirst = user;
        Id = UUID.randomUUID().toString().substring(0, 8);
    }

    public Game(String id, User userF, User userS) {
        Id = id;
        userFirst = userF;
        userSecond = userS;
    }

    public void Connect(User user) {
        userSecond = user;
    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public User getUserFirst() {
        return userFirst;
    }

    public User getUserSecond() {
        return userSecond;
    }

    public void setUserFirst(User userFirst) {
        Game.userFirst = userFirst;
    }

    public void setUserSecond(User userSecond) {
        Game.userSecond = userSecond;
    }

}
