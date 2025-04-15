package com.example.streamingappapi.series;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.streamingappapi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PopularSeriesRecItemAdapter extends RecyclerView.Adapter<PopularSeriesRecItemAdapter.ItemViewHolder> implements Filterable {

    private Context context;
    private List<PopularSeriesItems> itemList;
    private List<PopularSeriesItems> itemListFull;

    public PopularSeriesRecItemAdapter(Context context, List<PopularSeriesItems> itemList) {
        this.context = context;
        this.itemList = itemList != null ? itemList : new ArrayList<>();
        this.itemListFull = new ArrayList<>(itemList); // Initialize the full list
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_series_list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PopularSeriesItems item = itemList.get(position);

        // Set data to views
        Glide.with(context).load(item).into(holder.itemImg);
//        holder.itemImg.setImageResource(item.getPopularSeriesImg());
        holder.itemTitleTv.setText(item.getPopularSeriesTitle());;
        holder.ratingTv.setText(item.getPopularSeriesRating());

        int data=item.getPopularSeriesImg();
        String rating=item.getPopularSeriesRating();


        holder.itemView.setOnClickListener(v -> {
            for (PopularSeriesItems items : itemList) {
                Log.d("PopularSeriesRecItemAdapter", "Item: " + items.toString());
            }

            Intent intent = new Intent(context, SeriesScreenActivity.class);
            intent.putExtra("imageResource", item.getPopularSeriesImg());
            intent.putExtra("title", item.getPopularSeriesTitle());
            intent.putExtra("rating", item.getPopularSeriesRating());
            intent.putParcelableArrayListExtra("popularSeriesItemsList", new ArrayList<>(itemList));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {
        return popularSeriesFilter;
    }

    private Filter popularSeriesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PopularSeriesItems> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim();

                for (PopularSeriesItems item : itemListFull) {
                    if (item.getPopularSeriesTitle().toLowerCase(Locale.ROOT).contains(filterPattern)) {
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
