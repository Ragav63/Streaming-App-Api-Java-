package com.example.streamingappapi;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.movie.MovieScreenActivity;
import com.example.streamingappapi.series.PopularSeriesItems;
import com.example.streamingappapi.series.SeriesScreenActivity;

import java.util.ArrayList;
import java.util.List;

public class FilmographyRecItemAdapter<T> extends RecyclerView.Adapter<FilmographyRecItemAdapter.ItemViewHolder>{

    private Context context;
    private List<T> itemList;

    public FilmographyRecItemAdapter(Context context, List<T> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filmography_list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        T item = itemList.get(position);

        if (item instanceof MovieItem) {
            MovieItem movieItem = (MovieItem) item;
            holder.filmRating.setText(String.valueOf(movieItem.getImdb_rating()));
            Glide.with(context)
                    .load(movieItem.getPoster())
                    .into(holder.filmImg);
            holder.itemTitleTv.setText(movieItem.getTitle());
        } else if (item instanceof PopularSeriesItems) {
            PopularSeriesItems seriesItem = (PopularSeriesItems) item;
            holder.filmRating.setText(seriesItem.getPopularSeriesRating());
            Glide.with(context).load(seriesItem).into(holder.filmImg);
//            holder.filmImg.setImageResource(seriesItem.getPopularSeriesImg());
            holder.itemTitleTv.setText(seriesItem.getPopularSeriesTitle());
        } else if (item instanceof FilmographyListItems) {
            FilmographyListItems filmographyItem = (FilmographyListItems) item;
            holder.filmRating.setText(filmographyItem.getFilmRating());
            Glide.with(context).load(filmographyItem).into(holder.filmImg);
//            holder.filmImg.setImageResource(filmographyItem.getFilmImg());
            holder.itemTitleTv.setText(filmographyItem.getFilmTitle());
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent;
            if (item instanceof MovieItem) {
                MovieItem movieItem = (MovieItem) item;
                intent = new Intent(context, MovieScreenActivity.class);
                intent.putExtra("id", movieItem.getId());
                intent.putExtra("imageResource", movieItem.getPoster());
                intent.putExtra("title", movieItem.getTitle());
                intent.putExtra("rating", movieItem.getImdb_rating());
                intent.putExtra("year", movieItem.getYear());
                intent.putExtra("country", movieItem.getCountry());
                intent.putStringArrayListExtra("genres", new ArrayList<>(movieItem.getGenres()));
                intent.putExtra("runtime", movieItem.getRuntime());
                intent.putExtra("plot", movieItem.getPlot());
                intent.putExtra("url", movieItem.getUrl());
                intent.putStringArrayListExtra("images", new ArrayList<>(movieItem.getImages()));

                intent.putStringArrayListExtra("trailers", new ArrayList<>(movieItem.getTrailers()));

                intent.putExtra("languages", movieItem.getLanguage());
                intent.putParcelableArrayListExtra("cast", new ArrayList<>(movieItem.getCrew()));

                intent.putParcelableArrayListExtra("popularMovieItemsList", (ArrayList<? extends Parcelable>) new ArrayList<>(itemList));
            } else if (item instanceof PopularSeriesItems) {
                PopularSeriesItems seriesItem = (PopularSeriesItems) item;
                intent = new Intent(context, SeriesScreenActivity.class);
                intent.putExtra("imageResource", seriesItem.getPopularSeriesImg());
                intent.putExtra("title", seriesItem.getPopularSeriesTitle());
                intent.putExtra("rating", seriesItem.getPopularSeriesRating());
                intent.putParcelableArrayListExtra("popularSeriesItemsList", (ArrayList<? extends Parcelable>) new ArrayList<>(itemList));
            } else if (item instanceof FilmographyListItems) {
                intent = new Intent(context, MovieScreenActivity.class);
                FilmographyListItems filmographyItem = (FilmographyListItems) item;
                intent.putExtra("imageResource", filmographyItem.getFilmImg());
                intent.putExtra("title", filmographyItem.getFilmTitle());
                intent.putExtra("rating", filmographyItem.getFilmRating());
                intent.putParcelableArrayListExtra("filmographyListItems", (ArrayList<? extends Parcelable>) new ArrayList<>(itemList));
            } else {
                return; // Do nothing if item type is not recognized
            }

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView filmImg;
        TextView filmRating, itemTitleTv;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            filmImg = itemView.findViewById(R.id.item_iv);
            filmRating = itemView.findViewById(R.id.item_rating);
            itemTitleTv = itemView.findViewById(R.id.item_title);
        }
    }

}
