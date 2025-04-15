package com.example.streamingappapi;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.series.PopularSeriesItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CastRecItemAdapter<T> extends RecyclerView.Adapter<CastRecItemAdapter.ItemViewHolder> implements Filterable {

    private Context context;
    private List<CastItems> itemList;
    private List<CastItems> itemListFull;
    private List<T> mediaItemsList;

    public CastRecItemAdapter(Context context, List<CastItems> itemList, List<T> mediaItemsList) {
        this.context = context;
        this.itemList = itemList;
        this.itemListFull = new ArrayList<>(itemList); // Initialize the full list
        this.mediaItemsList = mediaItemsList != null ? mediaItemsList : new ArrayList<>();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CastItems item = itemList.get(position);

        Glide.with(context)
                .load(item.getImages().get(0)) // Assuming getImages() returns a List of URLs
                .into(holder.personImg);
        holder.itemTitleTv.setText(item.getName());
        holder.personDesignationTv.setText(item.getDesignation());


        holder.itemView.setOnClickListener(v -> {
            // Handle item click
            Intent intent = new Intent(context, ActorScreenActivity.class);
            intent.putStringArrayListExtra("imageResource", new ArrayList<>(item.getImages())); // Assuming this gets the image resource ID
            intent.putExtra("actorName", item.getName());
            intent.putExtra("actorDesc", item.getDesignation());
            intent.putExtra("actorAbout", item.getAbout());
            if (!mediaItemsList.isEmpty()) {
                if (mediaItemsList.get(0) instanceof MovieItem) {
                    MovieItem movieItem = (MovieItem) mediaItemsList.get(position);
                    if (mediaItemsList != null) {
                        intent.putParcelableArrayListExtra("popularMovieItemsList", (ArrayList<? extends Parcelable>) new ArrayList<>(mediaItemsList));
                    }
                } else if (mediaItemsList.get(0) instanceof PopularSeriesItems) {
                    PopularSeriesItems seriesItem = (PopularSeriesItems) mediaItemsList.get(position);
                    if (mediaItemsList != null) {
                        intent.putParcelableArrayListExtra("popularSeriesItemsList", (ArrayList<? extends Parcelable>) new ArrayList<>(mediaItemsList));
                    }
                }
            }

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {
        return castFilter;
    }

    private Filter castFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CastItems> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim();

                for (CastItems item : itemListFull) {
                    if (item.getName().toLowerCase(Locale.ROOT).contains(filterPattern)) {
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

    public boolean isDataEmpty() {
        return itemList.isEmpty();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView personImg;
        TextView itemTitleTv, personDesignationTv;
        LinearLayout itemll;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.personIv);
            itemTitleTv = itemView.findViewById(R.id.personName_tv);
            personDesignationTv = itemView.findViewById(R.id.personDesignation_tv);
            itemll=itemView.findViewById(R.id.itemll);
        }
    }

}
