package com.example.lab2.Enums;

public enum FontEnum {
    NORM(new String[]{"нормальный", "normal"}),
    GREAT(new String[]{"большой", "great"});

    private final String [] setting;

    FontEnum(String[] setting)
    {
        this.setting = setting;
    }

    public String[] getFont(){
        return setting;
    }
}
