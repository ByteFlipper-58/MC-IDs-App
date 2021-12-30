package com.infinitybyte.mcid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.infinitybyte.mcid.adapter.BedrockIDsAdapter;
import com.infinitybyte.mcid.model.IDsModel;

public class MainActivity extends AppCompatActivity {

    private BedrockIDsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Query query = FirebaseFirestore.getInstance()
                .collection("bedrock_ids");

        FirestoreRecyclerOptions<IDsModel> options = new FirestoreRecyclerOptions.Builder<IDsModel>()
                .setQuery(query, IDsModel.class)
                .build();

        adapter = new BedrockIDsAdapter(options);
        RecyclerView recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}