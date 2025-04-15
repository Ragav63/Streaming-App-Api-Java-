package com.example.streamingappapi;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.movie.PopularMoviesFragment;
import com.example.streamingappapi.series.PopularSeriesFragment;
import com.example.streamingappapi.series.PopularSeriesItems;
import com.example.streamingappapi.tv.TvFragment;

import java.util.ArrayList;
import java.util.List;

//interface OnCategoryClickListener {
//    void onCategoryClick(String categoryTitle);
//}

public class CategoryHomeRecItemAdapter extends RecyclerView.Adapter<CategoryHomeRecItemAdapter.ItemViewHolder>{

    private static Context context;
    private List<CategoryHomeItems> itemList;
    private List<MovieItem> movieItemList; // Add this instance variable
    private List<PopularSeriesItems> popularSeriesItemsList; // Add this instance variable
    private FragmentManager fragmentManager;

    public CategoryHomeRecItemAdapter(Context context, List<CategoryHomeItems> itemList, List<MovieItem> movieItemList, List<PopularSeriesItems> popularSeriesItemsList, FragmentManager fragmentManager) {
        this.context = context;
        this.itemList = itemList;
        this.movieItemList = movieItemList;
        this.popularSeriesItemsList = popularSeriesItemsList;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_home_list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CategoryHomeItems item = itemList.get(position);

        // Set data to views
        Glide.with(context).load(item).into(holder.itemImg);
//        holder.itemImg.setImageResource(item.getCategoryHomeImg());
        holder.categoryTitle.setText(item.getCategoryHomeTitle());


        holder.itemCv.setOnClickListener(v -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();

                switch (holder.categoryTitle.getText().toString()) {
                    case "TV CHANNELS":
                        TvFragment tvFragment = new TvFragment();
                        transaction.replace(R.id.container, tvFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;

                    case "MOVIES":
                    case "CARTOONS":
                    case "SCI-FI":
                    case "SPORT":
                        PopularMoviesFragment popularMoviesFragment = new PopularMoviesFragment();
                        bundle.putParcelableArrayList("popularMovieItems", (ArrayList<? extends Parcelable>) movieItemList);
                        popularMoviesFragment.setArguments(bundle);
                        transaction.replace(R.id.container, popularMoviesFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;

                    case "SERIES":
                        PopularSeriesFragment popularSeriesFragment = new PopularSeriesFragment();
                        bundle.putParcelableArrayList("popularSeriesItems", (ArrayList<? extends Parcelable>) popularSeriesItemsList);
                        popularSeriesFragment.setArguments(bundle);
                        transaction.replace(R.id.container, popularSeriesFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;

                    default:
                        TvFragment defaultTvFragment = new TvFragment();
                        transaction.replace(R.id.container, defaultTvFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                }

        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView categoryTitle;
        CardView itemCv;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_category_iv);
            categoryTitle = itemView.findViewById(R.id.item_category_title);

            itemCv=itemView.findViewById(R.id.itemCv);
        }
    }

}
