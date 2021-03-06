package com.example.rabab.moviesfamily;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class SettActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

   // MainActivity.FetchMovies movies;
   private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);


        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_units_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), "")

        );

    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String stringValue = newValue.toString();
        if(preference instanceof ListPreference){ //only thing will effect
            ListPreference listPreference = (ListPreference) preference;
            int index= listPreference.findIndexOfValue(stringValue);
            if (index >= 0){
                preference.setSummary(listPreference.getEntries()[index]);


            }
        } else {
            preference.setSummary(stringValue);
        }
        return true;
    }
}
