package com.infinitybyte.mcid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.infinitybyte.mcid.R;
import com.infinitybyte.mcid.model.IDsModel;

public class BedrockIDsAdapter extends FirestoreRecyclerAdapter<IDsModel, BedrockIDsAdapter.BedrockIDsHolder> {

    public BedrockIDsAdapter(FirestoreRecyclerOptions<IDsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BedrockIDsHolder holder, int position, @NonNull IDsModel model) {
        holder.item_name.setText(model.getItem_name());
        holder.item_stroke_id.setText(model.getItem_stroke_id());
        holder.item_number_id.setText(model.getItem_number_id());

        Glide.with(holder.item_image.getContext())
                .load(model.getItem_image())
                .into(holder.item_image);
    }

    @NonNull
    @Override
    public BedrockIDsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_item, parent, false);
        return new BedrockIDsAdapter.BedrockIDsHolder(v);
    }

    public class BedrockIDsHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name, item_stroke_id, item_number_id;

        public BedrockIDsHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_stroke_id = itemView.findViewById(R.id.item_stroke_id);
            item_number_id = itemView.findViewById(R.id.item_number_id);
        }
    }
}
