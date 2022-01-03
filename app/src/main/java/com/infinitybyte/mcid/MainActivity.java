package com.infinitybyte.mcid;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.recview);

        //Query
        Query query = firebaseFirestore.collection("bedrock_ids");
                //.orderBy("item_number_id", Query.Direction.ASCENDING);

        //RecyclerOptions
        FirestoreRecyclerOptions<IDsModel> options = new FirestoreRecyclerOptions.Builder<IDsModel>()
                .setQuery(query, IDsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<IDsModel, BedrockHolder>(options) {
            @NonNull
            @Override
            public BedrockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_item, parent, false);
                return new BedrockHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BedrockHolder holder, int position, @NonNull IDsModel model) {
                holder.item_name.setText(model.getItem_name());
                holder.item_stroke_id.setText(model.getItem_stroke_id());
                holder.item_number_id.setText(model.getItem_number_id());
            }
        };

        //mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }

    private class BedrockHolder extends RecyclerView.ViewHolder {
        //ImageView item_image;
        TextView item_name, item_stroke_id, item_number_id;

        public BedrockHolder(@NonNull View itemView) {
            super(itemView);
            //item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_stroke_id = itemView.findViewById(R.id.item_stroke_id);
            item_number_id = itemView.findViewById(R.id.item_number_id_text);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}