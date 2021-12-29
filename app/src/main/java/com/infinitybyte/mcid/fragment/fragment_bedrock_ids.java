package com.infinitybyte.mcid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.infinitybyte.mcid.MainActivity;
import com.infinitybyte.mcid.R;
import com.infinitybyte.mcid.adapter.BedrockIDsAdapter;
import com.infinitybyte.mcid.model.IDsModel;

public class fragment_bedrock_ids extends Fragment {
    //ГЕНЕРАТОР КАРТОЧЕК

    private RecyclerView recview;
    private BedrockIDsAdapter adapter;
    private Toolbar toolbar;

    public fragment_bedrock_ids() {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bedrock_ids, container, false);

        recview = (RecyclerView) view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseFirestore.getInstance()
                .collection("bedrock_ids")
                .orderBy("item_number_id", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<IDsModel> options = new FirestoreRecyclerOptions.Builder<IDsModel>()
                .setQuery(query, IDsModel.class)
                .build();

        adapter = new BedrockIDsAdapter(options);
        recview.setAdapter(adapter);

        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }
}
