package com.reddevil.loadshedding.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.reddevil.loadshedding.R;

import java.net.ContentHandler;

/**
 * Created by Sherlock on 3/19/2016.
 *
 * Class for storing application configurations.
 */
public class AppConfig {

    private Context mContext;

    public AppConfig(Context context)
    {
        this.mContext = context;
    }

    public void setUserGroup(int groupId)
    {
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.shared_preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("GROUP",groupId);
        editor.apply();
    }

    public int getUserGroup()
    {
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.shared_preference_file_key),Context.MODE_PRIVATE);
        return preferences.getInt("GROUP",0);
    }

    public void setRoutineId(int id)
    {
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.shared_preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ROUTINE_ID",id);
        editor.apply();
    }

    public int getRoutineId()
    {
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.shared_preference_file_key),Context.MODE_PRIVATE);
        return preferences.getInt("ROUTINE_ID",-1);
    }

    public void setAppInstallStatus(Boolean status)
    {
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.shared_preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("APP_INSTALLED",true);
        editor.apply();
    }

    public Boolean getAppInstalledStatus()
    {
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.shared_preference_file_key),Context.MODE_PRIVATE);
        return preferences.getBoolean("APP_INSTALLED",false);
    }





}
