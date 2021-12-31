package com.infinitybyte.mcid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BedrockIDsAdapter extends RecyclerView.Adapter<BedrockIDsAdapter.BedrockIDsHolder> {

    Context context;
    ArrayList<IDsModel> IDsArrayList;

    public BedrockIDsAdapter(Context context, ArrayList<IDsModel> IDsArrayList) {
        this.context = context;
        this.IDsArrayList = IDsArrayList;
    }

    @NonNull
    @Override
    public BedrockIDsAdapter.BedrockIDsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_item, parent, false);
        return new BedrockIDsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BedrockIDsAdapter.BedrockIDsHolder holder, int position) {
        IDsModel IDsModel = IDsArrayList.get(position);

        holder.item_name.setText(IDsModel.item_name);
        holder.item_stroke_id.setText(IDsModel.item_stroke_id);
        holder.item_number_id.setText(String.valueOf(IDsModel.item_number_id));
    }

    @Override
    public int getItemCount() {
        return IDsArrayList.size();
    }

    public static class BedrockIDsHolder extends RecyclerView.ViewHolder {
        ImageView item_image;
        TextView item_name, item_stroke_id, item_number_id;

        public BedrockIDsHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_stroke_id = itemView.findViewById(R.id.item_stroke_id);
            item_number_id = itemView.findViewById(R.id.item_number_id_text);
        }
    }
}
