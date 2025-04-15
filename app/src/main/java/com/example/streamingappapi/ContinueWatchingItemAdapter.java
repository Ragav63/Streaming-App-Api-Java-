package com.example.streamingappapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ContinueWatchingItemAdapter extends RecyclerView.Adapter<ContinueWatchingItemAdapter.ItemViewHolder> {
    private static Context context;
    private List<ContinueWatchingItems> continueWatchingItemsList;


    public ContinueWatchingItemAdapter(Context context, List<ContinueWatchingItems> continueWatchingItemsList) {
        this.context = context;
        this.continueWatchingItemsList = continueWatchingItemsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.continue_watching_home_list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ContinueWatchingItems currentItem = continueWatchingItemsList.get(position);
        holder.conWatchTitleTv.setText(currentItem.getConWatchTitle());
        holder.conWatchDescTv.setText(currentItem.getConWatchDesc());
        Glide.with(context).load(currentItem).into(holder.conWatchIv);
//        holder.conWatchIv.setImageResource(currentItem.getConWatchImg());

        // Set progress to 25% for demonstration
        setProgressBar(holder.conWatchPbar, 25);
    }

    @Override
    public int getItemCount() {
        return continueWatchingItemsList.size();
    }

    private void setProgressBar(ProgressBar progressBar, int progress) {
        // Set progress value
        progressBar.setProgress(progress);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView conWatchTitleTv, conWatchDescTv;
        ImageView conWatchIv;
        ProgressBar conWatchPbar;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            conWatchTitleTv = itemView.findViewById(R.id.conWatchTitle_tv);
            conWatchDescTv = itemView.findViewById(R.id.conWatchDesc_tv);
            conWatchIv = itemView.findViewById(R.id.conWatch_iv);
            conWatchPbar = itemView.findViewById(R.id.conWatchPbar);
        }
    }
}
