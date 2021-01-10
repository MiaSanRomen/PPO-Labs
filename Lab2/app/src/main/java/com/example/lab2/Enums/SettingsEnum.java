package com.example.lab2.Enums;

public enum SettingsEnum {
    THEME("theme"),
    FONT("font"),
    LANGUAGE("language"),
    DELETE_ALL("DeleteAll");

    private final String setting;

    SettingsEnum(String settings)
    {
        this.setting = settings;
    }

    public String getSetting()
    {
        return setting;
    }
}
