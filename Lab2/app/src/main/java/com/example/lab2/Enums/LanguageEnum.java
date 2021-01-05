package com.example.lab2.Enums;

public enum LanguageEnum {
    ENGLISH_ru("английский"),
    ENGLISH_en("english"),
    EN("en"),
    RUS("ru");

    private final String setting;

    LanguageEnum(String settings)
    {
        this.setting = settings;
    }

    public String getLanguage()
    {
        return setting;
    }
}
