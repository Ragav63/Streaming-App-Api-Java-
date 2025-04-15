package com.example.streamingappapi.movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streamingappapi.R;
import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.movie.MovieRecItemAdapter;

import java.util.List;


public class PopularMoviesFragment extends Fragment {
    ImageView backIv;
    private RecyclerView recVPopularMovies;
    private GridLayoutManager popularMoviesLayoutManager;
    private MovieRecItemAdapter movieRecItemAdapter;
    private List<MovieItem> movieItemList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieItemList = getArguments().getParcelableArrayList("popularMovieItems");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        backIv = view.findViewById(R.id.backIv);
        recVPopularMovies = view.findViewById(R.id.recVPopularMovies);

        backIv.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack();
        });

        if (movieItemList != null) {
            popularMoviesLayoutManager=new GridLayoutManager(getContext(), 2);
            recVPopularMovies.setLayoutManager(popularMoviesLayoutManager);
            movieRecItemAdapter = new MovieRecItemAdapter(getContext(), movieItemList);
            recVPopularMovies.setAdapter(movieRecItemAdapter);
            recVPopularMovies.setHasFixedSize(true);
        }

        return view;
    }

    public void updatePopularMovies(List<MovieItem> items) {
        movieItemList.clear();
        movieItemList.addAll(items);
        movieRecItemAdapter.notifyDataSetChanged();
    }
}