package com.infinitybyte.mcid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.infinitybyte.mcid.adapters.JsonAdapter;
import com.infinitybyte.mcid.api.SettingsMain;
import com.infinitybyte.mcid.models.IDsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;

public class ContentViewActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {

    private SettingsMain settings;

    private String locale = Locale.getDefault().getLanguage();
    private String item_locale_name = "null";

    private RecyclerView mRecyclerView;
    private List<IDsModel> viewItems = new ArrayList<>();

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MaterialToolbar toolbar;

    private static final String TAG = "ContentView";

    private ProgressBar progressBar;

    private static final int REQ_CODE_VERSION_UPDATE = 99;
    private InAppUpdateManager inAppUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_content_view);

        settings = new SettingsMain(this);

        toolbar = findViewById(R.id.toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recview);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new JsonAdapter(this, viewItems);
        mRecyclerView.setAdapter(adapter);

        if (locale.equals("ru") || locale.equals("uk") || locale.equals("be")) {
            item_locale_name = "ru";
        } else {
            item_locale_name = "en";
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.filter_and_sort:
                        filterAndSortBottomSheetDialog();
                        break;
                    case R.id.about_app:
                        Intent intent = new Intent(ContentViewActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                .mode(Constants.UpdateMode.FLEXIBLE)
                // default is false. If is set to true you,
                // have to manage the user confirmation when
                // you detect the InstallStatus.DOWNLOADED status,
                .useCustomNotification(true)
                .handler(this);

        inAppUpdateManager.checkForAppUpdate();

        Settings settings = new Settings();

        try {
            settings.loadSettings();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            settings.saveSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getDataFromURL();
    }

    private void filterAndSortBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.filter_and_sort_settings_layout);

        RadioGroup rg_view_ids = bottomSheetDialog.findViewById(R.id.rg_view_ids);
        MaterialButtonToggleGroup tg_btn_sort_by = bottomSheetDialog.findViewById(R.id.tg_btn_sort_by);
        MaterialButton btn_enter_filters = bottomSheetDialog.findViewById(R.id.btn_enter_filters);

        rg_view_ids.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rd_view_id_items_and_blocks:
                        settings.setShowIdType("items");
                        break;
                    case R.id.rd_view_id_effects:
                        settings.setShowIdType("effects");
                        break;
                    case R.id.rd_view_id_mobs:
                        settings.setShowIdType("mobs");
                        break;
                }
            }
        });

        tg_btn_sort_by.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.sort_by_ascending) {
                        settings.setSortBy("ascending");
                        tg_btn_sort_by.uncheck(R.id.sort_by_descending);
                    } else if (checkedId == R.id.sort_by_descending) {
                        settings.setSortBy("descending");
                        tg_btn_sort_by.uncheck(R.id.sort_by_ascending);
                    }
                }
            }
        });

        if (settings.getShowIdType() == "items") {
            rg_view_ids.check(R.id.rd_view_id_items_and_blocks);
        } else if (settings.getShowIdType() == "effects") {
            rg_view_ids.check(R.id.rd_view_id_effects);
        } else if (settings.getShowIdType() == "mobs") {
            rg_view_ids.check(R.id.rd_view_id_mobs);
        }

        if (settings.getSortBy() == "ascending") {
            tg_btn_sort_by.check(R.id.sort_by_ascending);
        } else if (settings.getSortBy() == "descending"){
            tg_btn_sort_by.check(R.id.sort_by_descending);
        }

        btn_enter_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                getDataFromURL();
                adapter.notifyDataSetChanged();
            }
        });

        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }

    /*private void addItemsFromJSON() {
        try {
            viewItems.clear();
            String bedrock_ids = readJSONDataFromFile();
            JSONObject jsonObject = new JSONObject(bedrock_ids);
            JSONArray jsonArray = jsonObject.getJSONArray(settings.getShowIdType());

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String item_image = itemObj.getString("item_image");
                String item_name = itemObj.getJSONObject("item_name").getString(item_locale_name);
                String item_string_id = itemObj.getString("item_string_id");
                String item_number_id = itemObj.getString("item_number_id");

                IDsModel itemInfo = new IDsModel(item_image, item_name, item_string_id, item_number_id);
                viewItems.add(itemInfo);
            }

            if (settings.getSortBy() == "descending") {
                Collections.reverse(viewItems);
            }

        } catch (JSONException | IOException e) {
            Log.d(TAG, "addItemsFromJSON: ", e);
        }
    }

    private String readJSONDataFromFile() throws IOException{

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String jsonString = null;
            inputStream = getAssets().open("bedrock_ids.json");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }*/

    private void getDataFromURL() {
        String url = "https://raw.githubusercontent.com/IbremMiner837/MC-IDs-App/master/app/src/main/assets/bedrock_ids.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressBar.setVisibility(View.GONE);
                if (response != null) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        //JSONArray jsonArray = new JSONArray(response);
                        viewItems.clear();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray(settings.getShowIdType());
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject itemObj = jsonArray.getJSONObject(i);

                            String item_image = itemObj.getString("item_image");
                            String item_name = itemObj.getJSONObject("item_name").getString(item_locale_name);
                            String item_string_id = itemObj.getString("item_string_id");
                            String item_number_id = itemObj.getString("item_number_id");

                            IDsModel itemInfo = new IDsModel(item_image, item_name, item_string_id, item_number_id);
                            viewItems.add(itemInfo);
                        }

                        if (settings.getSortBy() == "descending") {
                            Collections.reverse(viewItems);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: " + error);
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onInAppUpdateError(int code, Throwable error) {
        //
    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        if (status.isDownloaded()) {

            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

            Snackbar snackbar = Snackbar.make(rootView,
                    R.string.update_downloaded,
                    Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction(R.string.restart, view -> {

                // Triggers the completion of the update of the app for the flexible flow.
                inAppUpdateManager.completeUpdate();

            });

            snackbar.show();

        }
    }
}