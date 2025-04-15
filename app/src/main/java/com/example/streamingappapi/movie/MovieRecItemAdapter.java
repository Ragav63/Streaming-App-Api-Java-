package com.example.streamingappapi.movie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.streamingappapi.CastItems;
import com.example.streamingappapi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieRecItemAdapter extends RecyclerView.Adapter<MovieRecItemAdapter.ItemViewHolder> implements Filterable {

    private Context context;
    private List<MovieItem> itemList;
    private List<MovieItem> itemListFull;

    public MovieRecItemAdapter(Context context, List<MovieItem> itemList) {
        this.context = context;
        this.itemList = itemList != null ? itemList : new ArrayList<>();
        this.itemListFull = new ArrayList<>(itemList); // Initialize the full list
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        MovieItem item = itemList.get(position);

        // Set data to views using MovieItem fields
        holder.itemTitleTv.setText(item.getTitle());
        holder.ratingTv.setText(String.valueOf(item.getImdb_rating()));

        // Load the movie poster using Glide or any other image loading library
        Glide.with(context).load(item.getPoster()).into(holder.itemImg);

        String imageResource = item.getPoster();

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieScreenActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("rating", item.getImdb_rating());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("imageResource", imageResource);
            intent.putExtra("year", item.getYear());
            intent.putExtra("country", item.getCountry());
            intent.putStringArrayListExtra("genres", new ArrayList<>(item.getGenres()));
            intent.putExtra("runtime", item.getRuntime());
            intent.putExtra("plot", item.getPlot());
            intent.putExtra("url", item.getUrl());
            intent.putStringArrayListExtra("images", new ArrayList<>(item.getImages()));

            intent.putStringArrayListExtra("trailers", new ArrayList<>(item.getTrailers()));

            intent.putExtra("languages", item.getLanguage());
            intent.putParcelableArrayListExtra("cast", new ArrayList<>(item.getCrew()));

            intent.putParcelableArrayListExtra("popularMovieItemsList", new ArrayList<>(itemList));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {
        return movieFilter;
    }

    private Filter movieFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MovieItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim();

                for (MovieItem item : itemListFull) {
                    if (item.getTitle().toLowerCase(Locale.ROOT).contains(filterPattern) ||
                            String.valueOf(item.getYear()).toLowerCase(Locale.ROOT).contains(filterPattern) ||
                            item.getCountry().toLowerCase(Locale.ROOT).contains(filterPattern) ||
                            item.getGenres().toString().toLowerCase(Locale.ROOT).contains(filterPattern) ||
                            item.getPlot().toLowerCase(Locale.ROOT).contains(filterPattern) ||
                            containsCrewMember(item.getCrew(), filterPattern)) {

                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private boolean containsCrewMember(List<CastItems> crewList, String filterPattern) {
        for (CastItems crew : crewList) {
            if (crew.getName().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDataEmpty() {
        return itemList.isEmpty();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView ratingTv, itemTitleTv;
        CardView itemCv;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_iv);
            ratingTv = itemView.findViewById(R.id.item_rating);
            itemTitleTv = itemView.findViewById(R.id.item_title);

            itemCv=itemView.findViewById(R.id.itemCv);
        }
    }

}
