package com.infinitybyte.mcid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ContentViewActivity extends AppCompatActivity {

    private String locale = Locale.getDefault().getLanguage();
    private String item_locale_name = "null";

    private static final int ID_TYPE = 0;
    private static final int SORTING_TYPE = 1;

    private RecyclerView mRecyclerView;
    private List<IDsModel> viewItems = new ArrayList<>();

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MaterialToolbar toolbar;

    private static final String TAG = "ContentView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);

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
                }
                return true;
            }
        });

        addItemsFromJSON();
    }

    private void filterAndSortBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.filter_and_sort_settings_layout);

        MaterialRadioButton show_item_and_block_id = bottomSheetDialog.findViewById(R.id.rd_view_id_items_and_blocks);
        MaterialRadioButton show_effects_id = bottomSheetDialog.findViewById(R.id.rd_view_id_items_and_blocks);
        MaterialRadioButton show_mobs_id = bottomSheetDialog.findViewById(R.id.rd_view_id_mobs);
        MaterialButton sort_by_ascending = bottomSheetDialog.findViewById(R.id.sort_by_ascending);
        MaterialButton sort_by_descending = bottomSheetDialog.findViewById(R.id.sort_by_descending);

        show_item_and_block_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        show_effects_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        show_mobs_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        show_mobs_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }

    private void addItemsFromJSON() {
        try {

            String bedrock_ids = readJSONDataFromFile();
            JSONObject jsonObject = new JSONObject(bedrock_ids);
            JSONArray jsonArray;

            if (ID_TYPE == 0) {
                jsonArray = jsonObject.getJSONArray("items");
            } else if (ID_TYPE == 1) {
                jsonArray = jsonObject.getJSONArray("effects");
            } else if (ID_TYPE == 2) {
                jsonArray = jsonObject.getJSONArray("mobs");
            } else {
                jsonArray = jsonObject.getJSONArray("items");
            }

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String item_image = itemObj.getString("item_image");
                //String item_name = itemObj.getString("item_name");
                String item_name = itemObj.getJSONObject("item_name").getString(item_locale_name);
                String item_stroke_id = itemObj.getString("item_stroke_id");
                String item_number_id = itemObj.getString("item_number_id");

                IDsModel itemInfo = new IDsModel(item_image, item_name, item_stroke_id, item_number_id);
                viewItems.add(itemInfo);
            }

            Collections.sort(viewItems, new Comparator<IDsModel>(){
                public int compare(IDsModel obj1, IDsModel obj2) {
                    if (SORTING_TYPE == 0) {
                        //по возростанию (string)
                        return obj1.getItem_number_id().compareToIgnoreCase(obj2.getItem_number_id());
                    } else {
                        //по убыванию (string)
                        return obj2.getItem_number_id().compareToIgnoreCase(obj1.getItem_number_id());
                    }
                    // ## Ascending order
                    //return obj1.getItem_number_id().compareToIgnoreCase(obj2.getItem_number_id()); // To compare string values
                    // return Integer.valueOf(obj1.getItem_number_id()).compareTo(obj2.getItem_number_id()); // To compare integer values

                    // ## Descending order
                    // return obj2.getItem_number_id().compareToIgnoreCase(obj1.getItem_number_id()); // To compare string values
                    // return Integer.valueOf(obj2.getId()).compareTo(obj1.getId()); // To compare integer values
                }
            });


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
    }
}