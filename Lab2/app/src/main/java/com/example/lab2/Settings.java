package com.example.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatDelegate;


import com.example.lab2.Database.DataBaseProvider;
import com.example.lab2.Enums.FontEnum;
import com.example.lab2.Enums.LanguageEnum;
import com.example.lab2.Enums.SettingsEnum;

import java.util.Arrays;
import java.util.Locale;

public class Settings extends PreferenceActivity {

    SharedPreferences sp;
    int language_def;
    int font_def;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(SettingsEnum.THEME.getSetting(), true)) {
            setTheme(R.style.Theme_AppCompat);
        }

        String font = sp.getString(SettingsEnum.FONT.getSetting(), FontEnum.NORM.getFont()[0]);
        String listValue = sp.getString(SettingsEnum.LANGUAGE.getSetting(), LanguageEnum.ENGLISH_ru.getLanguage());
        Configuration configuration = new Configuration();

        Locale locale;
        assert listValue != null;
        if (listValue.toLowerCase().equals(LanguageEnum.ENGLISH_en.getLanguage()) || listValue.toLowerCase().equals(LanguageEnum.ENGLISH_ru.getLanguage())) {
            font_def = 1;
            locale = new Locale(LanguageEnum.EN.getLanguage());
        } else {
            font_def = 0;
            locale = new Locale(LanguageEnum.RUS.getLanguage());
        }
        Locale.setDefault(locale);
        configuration.locale = locale;

        assert font != null;
        if (Arrays.asList(FontEnum.NORM.getFont()).contains(font.toLowerCase())) {
            language_def = 0;
            configuration.fontScale = (float) 0.85;
        } else if (Arrays.asList(FontEnum.GREAT.getFont()).contains(font.toLowerCase())) {
            language_def = 1;
            configuration.fontScale = (float) 1;
        }

        getBaseContext().getResources().updateConfiguration(configuration, null);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ChangeSettingsFragment()).commit();
        super.onCreate(savedInstanceState);
    }


    public static class ChangeSettingsFragment extends PreferenceFragment {

        private DataBaseProvider db;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            db = App.getInstance().getDatabase();
            addPreferencesFromResource(R.xml.settings);
            Preference button = findPreference(SettingsEnum.DELETE_ALL.getSetting());
            ListPreference language = (ListPreference) findPreference(SettingsEnum.LANGUAGE.getSetting());
            Preference theme = findPreference(SettingsEnum.THEME.getSetting());
            ListPreference font = (ListPreference) findPreference(SettingsEnum.FONT.getSetting());
            font.setValueIndex(((Settings) getActivity()).language_def);
            language.setValueIndex(((Settings) getActivity()).font_def);
            theme.setOnPreferenceChangeListener(this::themeChange);
            language.setOnPreferenceChangeListener(this::languageChange);
            button.setOnPreferenceClickListener(this::deleteClick);
            font.setOnPreferenceChangeListener(this::fontChange);
        }

        private boolean themeChange(Preference preference, Object newValue) {
            if ((boolean) newValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            getActivity().recreate();
            return true;
        }


        private boolean languageChange(Preference preference, Object newValue) {
            Locale locale;
            if (newValue.toString().toLowerCase().equals(LanguageEnum.ENGLISH_en.getLanguage()) || newValue.toString().toLowerCase().equals(LanguageEnum.ENGLISH_ru.getLanguage())) {
                locale = new Locale(LanguageEnum.EN.getLanguage());
            } else {
                locale = new Locale(LanguageEnum.RUS.getLanguage());
            }
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.locale = locale;
            getActivity().getResources().updateConfiguration(configuration, null);
            getActivity().recreate();
            return true;
        }


        private boolean fontChange(Preference preference, Object newValue) {
            Configuration configuration = getResources().getConfiguration();
            if (Arrays.asList(FontEnum.NORM.getFont()).contains(newValue.toString().toLowerCase())) {
                configuration.fontScale = (float) 0.85;
            } else if (Arrays.asList(FontEnum.GREAT.getFont()).contains(newValue.toString().toLowerCase())) {
                configuration.fontScale = (float) 1;
            }
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);
            getActivity().recreate();
            return true;
        }


        private boolean deleteClick(Preference preference) {
            db.timerDao().DeleteAll();
            Intent intent = new Intent();
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
            return true;
        }
    }
}