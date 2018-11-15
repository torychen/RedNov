package com.tory.rednov.view;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tory.rednov.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppSettingsFragment extends PreferenceFragment {


    public AppSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_appsettings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final Preference debugFlag = findPreference("KeyDebugFlag");

        
        return super.onCreateView(inflater, container,savedInstanceState);
    }

}
