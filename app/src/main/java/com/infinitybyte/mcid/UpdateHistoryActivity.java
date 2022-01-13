package com.infinitybyte.mcid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.infinitybyte.mcid.adapters.JsonAdapter;
import com.infinitybyte.mcid.adapters.UpdateHistoryAdapter;
import com.infinitybyte.mcid.models.IDsModel;
import com.infinitybyte.mcid.models.UpdateHistoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UpdateHistoryActivity extends AppCompatActivity {

    private String locale = Locale.getDefault().getLanguage();
    private String item_locale_name = "null";

    private RecyclerView mRecyclerView;
    private List<UpdateHistoryModel> viewItems = new ArrayList<>();

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MaterialToolbar toolbar;

    private static final String TAG = "UpdateHistoryView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_history);

        toolbar = findViewById(R.id.toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recview);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new UpdateHistoryAdapter(this, viewItems);
        mRecyclerView.setAdapter(adapter);

        if (locale.equals("ru") || locale.equals("uk") || locale.equals("be")) {
            item_locale_name = "ru";
        } else {
            item_locale_name = "en";
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(R.string.about_app);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addItemsFromJSON();
    }

    private void addItemsFromJSON() {
        try {
            viewItems.clear();
            String bedrock_ids = readJSONDataFromFile();
            JSONObject jsonObject = new JSONObject(bedrock_ids);
            JSONArray jsonArray = jsonObject.getJSONArray("UpdateHistory");

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String version = itemObj.getString("version");
                String changelog = itemObj.getJSONObject("changelog").getString(item_locale_name);

                UpdateHistoryModel updateHistoryModel = new UpdateHistoryModel(version, changelog);
                viewItems.add(updateHistoryModel);
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
            inputStream = getAssets().open("UpdateHistory.json");
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}