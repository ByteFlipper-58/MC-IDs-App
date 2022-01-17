package com.infinitybyte.mcid.adapters;

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.infinitybyte.mcid.R;
import com.infinitybyte.mcid.models.IDsModel;

import java.util.List;

public class JsonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE = 1;
    private final Context context;
    private final List<IDsModel> listRecyclerItem;

    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;

    public JsonAdapter(Context context, List<IDsModel> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView item_name, item_string_id, item_number_id;
        private ImageView item_image;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_string_id = (TextView) itemView.findViewById(R.id.item_stroke_id);
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
                        R.layout.list_small_item, viewGroup, false);

                return new ItemViewHolder((layoutView));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
            default:

                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop());

                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                IDsModel itemInfo = (IDsModel) listRecyclerItem.get(i);

                itemViewHolder.item_name.setText(itemInfo.getItem_name());
                itemViewHolder.item_string_id.setText(itemInfo.getItem_string_id());
                itemViewHolder.item_number_id.setText(itemInfo.getItem_number_id());

                Glide.with(itemViewHolder.item_image)
                        .load(Uri.parse("file:///android_asset/images/" + itemInfo.getItem_image()))
                        .apply(requestOptions)
                        .into(itemViewHolder.item_image);
        }
    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }

    @Override
    public int getItemViewType (int position) {
        if (isSwitchView){
            return LIST_ITEM;
        }else{
            return GRID_ITEM;
        }
    }

    public boolean toggleItemViewType () {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }
}