package com.infinitybyte.mcid;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class JsonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE = 1;
    private final Context context;
    private final List<IDsModel> listRecyclerItem;

    public JsonAdapter(Context context, List<IDsModel> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView item_name, item_stroke_id, item_number_id;
        private ImageView item_image;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_stroke_id = (TextView) itemView.findViewById(R.id.item_stroke_id);
            item_number_id = (TextView) itemView.findViewById(R.id.item_number_id);
            item_image = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case TYPE:
            default:
                View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.small_item, viewGroup, false);

                return new ItemViewHolder((layoutView));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
            default:

                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                IDsModel itemInfo = (IDsModel) listRecyclerItem.get(i);

                itemViewHolder.item_name.setText(itemInfo.getItem_name());
                itemViewHolder.item_stroke_id.setText(itemInfo.getItem_stroke_id());
                itemViewHolder.item_number_id.setText(itemInfo.getItem_number_id());

                Glide.with(itemViewHolder.item_image)
                        .load(Uri.parse("file:///android_asset/images/" + itemInfo.getItem_image() + ".png"))
                        .into(itemViewHolder.item_image);
        }

    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
