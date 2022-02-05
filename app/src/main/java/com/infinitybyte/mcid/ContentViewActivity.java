package com.infinitybyte.mcid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.infinitybyte.mcid.adapters.JsonAdapter;
import com.infinitybyte.mcid.api.ErrorDialogAPI;
import com.infinitybyte.mcid.config.Settings;
import com.infinitybyte.mcid.config.SettingsAssist;
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

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;

public class ContentViewActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {

    private RecyclerView mRecyclerView;
    private List<IDsModel> viewItems = new ArrayList<>();

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MaterialToolbar toolbar;

    private static final int REQ_CODE_VERSION_UPDATE = 99;
    private InAppUpdateManager inAppUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File settingsFile = new File(getFilesDir(), "Settings.json");

        if(!settingsFile.exists()) {
            try {
                SettingsAssist.save(settingsFile, Settings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadSettings();
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_content_view);

        toolbar = findViewById(R.id.toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recview);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new JsonAdapter(this, viewItems);
        mRecyclerView.setAdapter(adapter);

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

        addItemsFromJSON();
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
                        Settings.idCategory = "items";
                        break;
                    case R.id.rd_view_id_effects:
                        Settings.idCategory = "effects";
                        break;
                    case R.id.rd_view_id_mobs:
                        Settings.idCategory = "mobs";
                        break;
                }
            }
        });

        tg_btn_sort_by.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.sort_by_ascending) {
                        Settings.sortingType = "ascending";
                        tg_btn_sort_by.uncheck(R.id.sort_by_descending);
                    } else if (checkedId == R.id.sort_by_descending) {
                        Settings.sortingType = "descending";
                        tg_btn_sort_by.uncheck(R.id.sort_by_ascending);
                    }
                }
            }
        });

        if (Settings.idCategory.equals("items")) {
            rg_view_ids.check(R.id.rd_view_id_items_and_blocks);
        } else if (Settings.idCategory.equals("effects")) {
            rg_view_ids.check(R.id.rd_view_id_effects);
        } else if (Settings.idCategory.equals("mobs")) {
            rg_view_ids.check(R.id.rd_view_id_mobs);
        }

        if (Settings.sortingType.equals("ascending")) {
            tg_btn_sort_by.check(R.id.sort_by_ascending);
        } else if (Settings.sortingType.equals("descending")){
            tg_btn_sort_by.check(R.id.sort_by_descending);
        }

        btn_enter_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                addItemsFromJSON();
                saveSettings();
                adapter.notifyDataSetChanged();
            }
        });

        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }

    private void addItemsFromJSON() {
        try {
            viewItems.clear();
            String bedrock_ids = readJSONDataFromFile();
            JSONObject jsonObject = new JSONObject(bedrock_ids);
            JSONArray jsonArray = jsonObject.getJSONArray(Settings.idCategory);

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String item_image = itemObj.getString("item_image");
                String item_name = itemObj.getJSONObject("item_name").getString(Settings.contentLanguage);
                String item_string_id = itemObj.getString("item_string_id");
                String item_number_id = itemObj.getString("item_number_id");

                IDsModel itemInfo = new IDsModel(item_image, item_name, item_string_id, item_number_id);
                viewItems.add(itemInfo);
            }

            if (Settings.sortingType.equals("descending")) {
                Collections.reverse(viewItems);
            }

        } catch (JSONException | IOException e) {
            //
        }
    }

    private String readJSONDataFromFile() throws IOException {

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

    public void saveSettings() {
        File settingsFile = new File(getFilesDir(), "Settings.json");

        try {
            SettingsAssist.save(settingsFile, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSettings() {
        File settingsFile = new File(getFilesDir(), "Settings.json");

        try {
            SettingsAssist.load(settingsFile, Settings.class);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}