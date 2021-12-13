package com.example.life_partner;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import java.util.Set;


public class settingFragment  extends PreferenceFragmentCompat {

    SharedPreferences prefs;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.setting_list);
        //
        prefs =PreferenceManager.getDefaultSharedPreferences(getActivity());
        //설정 상태에서 대한 제어 가능
        prefs.registerOnSharedPreferenceChangeListener(prefListener);


    }
    //설정값 변경 리스너
    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            PreferenceScreen screen = getPreferenceScreen();
            Intent svcIntent =  new Intent(getActivity().getApplicationContext(),ScreenLockService.class);

            Log.d("onSharedPreferenceChanged 호출", "호출");
            if (key.equals("nickName")) { //이름 변경
                EditTextPreference ep = findPreference(key);
                Toast.makeText(getActivity(),"이름변경",Toast.LENGTH_SHORT).show();
                ep.setSummary(prefs.getString(key,""));
            }

            if(prefs.getBoolean("locker",true)){
                svcIntent.putExtra("command", "start");
                getActivity().getApplicationContext().startForegroundService(svcIntent);///잠금화면 설정
                Log.d("잠금화면 on", "startForeground");
            }
            else{
                svcIntent.putExtra("command", "stop");
                getActivity().getApplicationContext().startForegroundService(svcIntent);///잠금화면 설정 정지
                Log.d("잠금화면 off", "deleteForeground");
            }
        }
    };
}