package com.infinitybyte.mcid;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BedrockIDsAdapter extends RecyclerView.Adapter<BedrockIDsAdapter.BedrockIDsHolder> {

    Context context;
    String data1[], data2[], images[];

    public BedrockIDsAdapter(Context ct, String item_name[], String item_stroke_id[], String item_image[]) {
        context = ct;
        data1 = item_name;
        data2 = item_stroke_id;
        images = item_image;
    }

    @NonNull
    @Override
    public BedrockIDsAdapter.BedrockIDsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.small_item, parent, false);
        return new BedrockIDsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BedrockIDsAdapter.BedrockIDsHolder holder, int position) {
        holder.item_name.setText(data1[position]);
        holder.item_stroke_id.setText(data2[position]);

        Glide.with(holder.item_image)
                .load(Uri.parse("file:///android_asset/" + images[position] + ".png"))
                .into(holder.item_image);
    }

    @Override
    public int getItemCount() {
        return images.length;
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
