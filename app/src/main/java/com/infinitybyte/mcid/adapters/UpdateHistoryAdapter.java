package com.infinitybyte.mcid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infinitybyte.mcid.R;
import com.infinitybyte.mcid.models.IDsModel;
import com.infinitybyte.mcid.models.UpdateHistoryModel;

import java.util.List;

public class UpdateHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<UpdateHistoryModel> listRecyclerItem;

    public UpdateHistoryAdapter(Context context, List<UpdateHistoryModel> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ChangelogViewHolder extends RecyclerView.ViewHolder {

        private TextView version, changelog;

        public ChangelogViewHolder(@NonNull View itemView) {
            super(itemView);
            version = itemView.findViewById(R.id.uh_version);
            changelog = itemView.findViewById(R.id.uh_changelog);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.changelog_layout, viewGroup, false);

        return new UpdateHistoryAdapter.ChangelogViewHolder((layoutView));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ChangelogViewHolder changelogViewHolder = (ChangelogViewHolder) viewHolder;
        UpdateHistoryModel changelogInfo = (UpdateHistoryModel) listRecyclerItem.get(i);

        changelogViewHolder.version.setText(changelogInfo.getVersion());
        changelogViewHolder.changelog.setText(changelogInfo.getChangelog());
    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
