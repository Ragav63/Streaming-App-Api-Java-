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

import com.example.streamingappapi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PopularMovieRecItemAdapter extends RecyclerView.Adapter<PopularMovieRecItemAdapter.ItemViewHolder> implements Filterable {

    private Context context;
    private List<PopularMovieItems> itemList;
    private List<PopularMovieItems> itemListFull;

    public PopularMovieRecItemAdapter(Context context, List<PopularMovieItems> itemList) {
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
        PopularMovieItems item = itemList.get(position);

        // Set data to views
        holder.itemImg.setImageResource(item.getPopularMovieImg());
        holder.ratingTv.setText(item.getPopularMovieRating());
        holder.itemTitleTv.setText(item.getPopularMovieTitle());

        int data=item.getPopularMovieImg();
        String rating=item.getPopularMovieRating();


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieScreenActivity.class);
            intent.putExtra("imageResource", item.getPopularMovieImg());
            intent.putExtra("rating", item.getPopularMovieRating());
            intent.putExtra("title", item.getPopularMovieTitle());
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
        return popularMovieFilter;
    }

    private Filter popularMovieFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PopularMovieItems> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim();

                for (PopularMovieItems item : itemListFull) {
                    if (item.getPopularMovieTitle().toLowerCase(Locale.ROOT).contains(filterPattern)) {
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
