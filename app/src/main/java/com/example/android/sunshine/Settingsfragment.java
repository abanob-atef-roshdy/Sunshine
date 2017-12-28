package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import java.util.InputMismatchException;



public class Settingsfragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener , Preference.OnPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
       addPreferencesFromResource(R.xml.pref_sunshine);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for(int i = 0;i<count;i++){
            Preference p = getPreferenceScreen().getPreference(i);
            String value = sharedPreferences.getString(p.getKey(),"");
            setpreferencesummary(p,value);

        }
        Preference preference = findPreference(getString(R.string.location_key));
        preference.setOnPreferenceChangeListener(this);
    }
    public void setpreferencesummary(Preference preference,String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefindex = listPreference.findIndexOfValue(value);
            if(prefindex>=0){
                listPreference.setSummary(listPreference.getEntries()[prefindex]);
            }
        }
        else if(preference instanceof EditTextPreference){
            preference.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        String value = sharedPreferences.getString(preference.getKey(),"");
        setpreferencesummary(preference,value);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals(getString(R.string.location_key))){
          try {
             String edvalue = newValue.toString();
                //float f = Float.parseFloat(edvalue);
                String a[] = {"0","1","2","3","4","5","6","7","8","9",";",",","!",")","(","@","%","$","*","+","-","_","#","."};
                for(int i = 0;i<a.length;i++){
                if(edvalue.contains(a[i]) || edvalue.isEmpty()) {

                    String message = "please enter a valid city name";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    return false;
                }

                }

        }catch (InputMismatchException i){
                String message = "please enter a valid city name";
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
