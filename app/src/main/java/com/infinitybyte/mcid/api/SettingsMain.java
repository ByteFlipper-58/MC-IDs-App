package com.infinitybyte.mcid.api;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsMain {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public SettingsMain(Context context) {
        this.context = context;
        setPreferences();
    }

    private void setPreferences(){
        preferences = context.getSharedPreferences("show_id_type", Context.MODE_PRIVATE);
        preferences = context.getSharedPreferences("sort_by", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void cleanPreferences(){
        editor.clear();
        editor.commit();
    }

    public void setShowIdType(String ShowIdType){
        editor.putString("show_id_type", ShowIdType);
        editor.commit();
    }

    public String getShowIdType(){
        return preferences.getString("show_id_type", "images/items");
    }

    public void setSortBy(String sort_by){
        editor.putString("sort_by", sort_by);
        editor.commit();
    }

    public String getSortBy(){
        return preferences.getString("sort_by", "ascending");
    }
}
