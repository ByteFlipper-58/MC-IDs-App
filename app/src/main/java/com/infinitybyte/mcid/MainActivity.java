package com.infinitybyte.mcid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.infinitybyte.mcid.adapter.BedrockIDsAdapter;
import com.infinitybyte.mcid.fragment.fragment_bedrock_ids;
import com.infinitybyte.mcid.model.IDsModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new fragment_bedrock_ids()).commit();
    }
}