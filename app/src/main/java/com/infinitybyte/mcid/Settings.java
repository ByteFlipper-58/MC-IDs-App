package com.infinitybyte.mcid;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;

public class Settings {

    @Parameter(jsonKey = "sorting_type")
    public static String sortingType = "as";

    private static JSONObject settings;
    private Context ctx;

    public void saveSettings() throws IOException {
        for (Field field : Settings.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Parameter.class)) {
                try {
                    settings.put(field.getAnnotation(Parameter.class).jsonKey(),
                            field.get(null));
                } catch (IllegalAccessException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //File settingsFile = new File(ctx.getFilesDir(), "settings.json");
        File settingsFile = ctx.getExternalFilesDir("settings.json");
        FileOutputStream fileOutputStream = new FileOutputStream(settingsFile);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(settings.toString());

        //FileHandle settingsFile = Gdx.files.external("S.A.U.W./User/settings.json");
        //settingsFile.writeString(settings.toString(), false);
    }

    public void loadSettings() throws JSONException, IOException {

        FileInputStream fileInputStream = ctx.openFileInput("settings.json");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader settingsFile = new BufferedReader(inputStreamReader);
        String result = settingsFile.readLine();

        //FileHandle settingsFile = Gdx.files.external("S.A.U.W./User/settings.json");
        //String result = settingsFile.readString();

        settings = new JSONObject(result);
        for (Field field : Settings.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Parameter.class)) {
                try {
                    field.set(null, settings.get(field.getAnnotation(Parameter.class).jsonKey()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
