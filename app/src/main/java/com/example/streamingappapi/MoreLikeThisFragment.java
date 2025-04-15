package com.example.streamingappapi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.movie.MovieRecItemAdapter;
import com.example.streamingappapi.series.PopularSeriesItems;
import com.example.streamingappapi.series.PopularSeriesRecItemAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MoreLikeThisFragment extends Fragment {
    private RecyclerView recVMorelikeThis;
    private RecyclerView.LayoutManager popularMoviesLayoutManager;
//    private List<PopularMovieItems> popularMovieItemsList;
    private List<MovieItem> movieItemList;
    private List<PopularSeriesItems> popularSeriesItemsList;
//    private PopularMovieRecItemAdapter movieAdapter;
    private MovieRecItemAdapter movieRecItemAdapter;
    private PopularSeriesRecItemAdapter seriesAdapter;
    private boolean isMovieList;

    public MoreLikeThisFragment() {
        // Required empty public constructor
    }

    public static MoreLikeThisFragment newInstanceWithMovies(List<MovieItem> popularMovieItemsList) {
        MoreLikeThisFragment fragment = new MoreLikeThisFragment();
        Bundle args = new Bundle();
        // Log the movie items to check for duplicates
        Log.d("MoreLikeThisFragment", "Original movie items: " + popularMovieItemsList);
        // Remove duplicates before passing
        List<MovieItem> deduplicatedList = new ArrayList<>();
        Set<MovieItem> seen = new HashSet<>();
        for (MovieItem item : popularMovieItemsList) {
            if (seen.add(item)) {
                deduplicatedList.add(item);
            }
        }
        args.putParcelableArrayList("popularMovieItemsList", new ArrayList<>(deduplicatedList));
        args.putBoolean("isMovieList", true);
        fragment.setArguments(args);
        return fragment;
    }

    public static MoreLikeThisFragment newInstanceWithSeries(List<PopularSeriesItems> popularSeriesItemsList) {
        MoreLikeThisFragment fragment = new MoreLikeThisFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("popularSeriesItemsList", new ArrayList<>(popularSeriesItemsList != null ? popularSeriesItemsList : new ArrayList<>()));
        args.putBoolean("isMovieList", false);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isMovieList = getArguments().getBoolean("isMovieList");
            if (isMovieList) {
                movieItemList = getArguments().getParcelableArrayList("popularMovieItemsList");
                Log.d("MoreLikeThisFragment", "Received movie items: " + movieItemList);
            } else {
                popularSeriesItemsList = getArguments().getParcelableArrayList("popularSeriesItemsList");
                Log.d("MoreLikeThisFragment", "Received series items: " + popularSeriesItemsList);
            }
        } else {
            Log.d("MoreLikeThisFragment", "getArguments() is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_like_this, container, false);

        recVMorelikeThis = view.findViewById(R.id.recVMorelikeThis);

        popularMoviesLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recVMorelikeThis.setLayoutManager(popularMoviesLayoutManager);

        if (isMovieList) {
            if (movieItemList != null && !movieItemList.isEmpty()) {
                movieRecItemAdapter = new MovieRecItemAdapter(getContext(), movieItemList);
                recVMorelikeThis.setAdapter(movieRecItemAdapter);
                recVMorelikeThis.setHasFixedSize(true);
            } else {
                Log.d("MoreLikeThisFragment", "Movie list is empty or null");
            }
        } else {
            if (popularSeriesItemsList != null && !popularSeriesItemsList.isEmpty()) {
                seriesAdapter = new PopularSeriesRecItemAdapter(getContext(), popularSeriesItemsList);
                recVMorelikeThis.setAdapter(seriesAdapter);
                recVMorelikeThis.setHasFixedSize(true);
            } else {
                Log.d("MoreLikeThisFragment", "Series list is empty or null");
            }
        }


        return view;
    }

    public void updateMovieList(List<MovieItem> items) {
        if (movieItemList != null) {
            Set<MovieItem> uniqueItems = new HashSet<>(items);
            movieItemList.clear();
            movieItemList.addAll(uniqueItems);
            movieRecItemAdapter.notifyDataSetChanged();
        }
    }

    public void updateSeriesList(List<PopularSeriesItems> items) {
        if (popularSeriesItemsList != null) {
            popularSeriesItemsList.clear();
            popularSeriesItemsList.addAll(items);
            seriesAdapter.notifyDataSetChanged();
        }
    }
}