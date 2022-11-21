package com.example.smartpurifier;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class SettingFragment extends PreferenceFragmentCompat {

    private static final String SETTING_1 = "setting1";
    private static final String SETTING_2 = "setting2";
    private static final String SETTING_3 = "setting3";
    private static final String SETTING_4 = "setting4";
    private static final String SETTING_LANGUAGE = "key_language";
    private static final String SETTING_DEPTEST = "key_dependent";
    private static final String SETTING_DEPCHILD = "key_dependent_child";
    SharedPreferences prefs;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    // key값에 해당하는 명령 넣기

                    if (key.equals(SETTING_DEPTEST)) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals(SETTING_DEPCHILD)) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals(SETTING_LANGUAGE)) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals(SETTING_1)) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals(SETTING_2)) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals(SETTING_3)) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals(SETTING_4)) {
                        Log.d("TAG", key + "SELECTED");
                    }
                }
            };
}