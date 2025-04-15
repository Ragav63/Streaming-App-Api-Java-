package com.example.streamingappapi;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.movie.MovieScreenActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeStartPagerAdapter extends RecyclerView.Adapter<HomeStartPagerAdapter.ItemViewHolder> {

    private List<MovieItem> homeStartItemsList;
    private List<MovieItem> movieItemList;
    private List<Integer> favoritePositions; // List to track favorite positions
    private boolean isDownloaded = false;

    public HomeStartPagerAdapter(List<MovieItem> homeStartItemsList, List<MovieItem> movieItemList) {
        this.homeStartItemsList = homeStartItemsList;
        this.movieItemList = movieItemList;
        this.favoritePositions = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_start_item_page, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        MovieItem currentItem = homeStartItemsList.get(position);
        holder.homeStartRating.setText(String.valueOf(currentItem.getImdb_rating()));
        holder.homeStartTitle.setText(currentItem.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(currentItem.getPoster())
                .into(holder.homeStartImage);
//        holder.homeStartImage.setImageResource(currentItem.getHomeStartImg());
        String imageResource = currentItem.getPoster();

        holder.homeStartWatchNow.setOnClickListener(v -> {
            if (isDownloaded) {
                Toast.makeText(v.getContext(), "Already added to Download", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Added to Download", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), MovieScreenActivity.class);
                intent.putExtra("id", currentItem.getId());
                intent.putExtra("rating", currentItem.getImdb_rating());
                intent.putExtra("title", currentItem.getTitle());
                intent.putExtra("imageResource", imageResource);
                intent.putExtra("year", currentItem.getYear());
                intent.putExtra("country", currentItem.getCountry());
                intent.putStringArrayListExtra("genres", new ArrayList<>(currentItem.getGenres()));
                intent.putExtra("runtime", currentItem.getRuntime());
                intent.putExtra("plot", currentItem.getPlot());
                intent.putExtra("url", currentItem.getUrl());
                intent.putStringArrayListExtra("images", new ArrayList<>(currentItem.getImages()));

                intent.putStringArrayListExtra("trailers", new ArrayList<>(currentItem.getTrailers()));

                intent.putExtra("languages", currentItem.getLanguage());
                intent.putParcelableArrayListExtra("cast", new ArrayList<>(currentItem.getCrew()));

                intent.putParcelableArrayListExtra("popularMovieItemsList", new ArrayList<>(movieItemList));
                holder.itemView.getContext().startActivity(intent);
                isDownloaded = true;
            }
        });

        // Check if this position is in the list of favorite positions
        if (favoritePositions.contains(position)) {
            holder.favIv.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.bluemain));
        } else {
            holder.favIv.clearColorFilter(); // Remove the tint
        }

        holder.favIv.setOnClickListener(v -> {
            // Toggle the favorite state
            if (favoritePositions.contains(position)) {
                favoritePositions.remove(Integer.valueOf(position)); // Remove from favorites
            } else {
                favoritePositions.add(position); // Add to favorites
                Toast.makeText(v.getContext(), holder.homeStartTitle.getText().toString()+" Added to Favourite", Toast.LENGTH_SHORT).show();
            }
            notifyItemChanged(position); // Notify the adapter to refresh the item
        });
    }

    @Override
    public int getItemCount() {
        return homeStartItemsList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView homeStartRating, homeStartTitle, homeStartWatchNow;
        ImageView homeStartImage, favIv;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            homeStartRating = itemView.findViewById(R.id.homeRatingTv);
            homeStartTitle = itemView.findViewById(R.id.hometitleTv);
            homeStartImage = itemView.findViewById(R.id.homeIV);
            homeStartWatchNow = itemView.findViewById(R.id.watchNowTv);
            favIv = itemView.findViewById(R.id.favIv);
        }
    }
}
