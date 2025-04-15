package com.example.streamingappapi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AboutPhotosRecItemAdapter extends RecyclerView.Adapter<AboutPhotosRecItemAdapter.ItemViewHolder>{

    private Context context;
    private ArrayList<String> itemList;

    public AboutPhotosRecItemAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList ;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aboutphotos_list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//        AboutPhotosItems item = itemList.get(position);
        String imageUrl = itemList.get(position);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.aboutPhotosImg);
//        holder.aboutPhotosImg.setImageResource(item.getAboutImg());

        holder.itemView.setOnClickListener(v -> {
            // Handle item click
            Intent intent = new Intent(context, FullScreenImageActivity.class);
            intent.putExtra("imageResource", imageUrl); // Assuming this gets the image resource ID
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView aboutPhotosImg;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            aboutPhotosImg = itemView.findViewById(R.id.photosIv);
        }
    }

}
