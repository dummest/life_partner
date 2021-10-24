package com.example.life_partner;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


public class settingFragment  extends PreferenceFragmentCompat {

    SharedPreferences prefs;

    ListPreference colorPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_list, rootKey);
        colorPreference = (ListPreference) findPreference("color");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!prefs.getString("color", "").equals("")) {
            colorPreference.setSummary(prefs.getString("color", "검정"));
        }
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener(){

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("color")){
                colorPreference.setSummary(prefs.getString("color", "검정"));

            }

        }
    };
}
