package com.example.streamingappapi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.movie.MovieRecItemAdapter;
import com.example.streamingappapi.movie.MovieVolleySingleton;
import com.example.streamingappapi.movie.PopularMoviesFragment;
import com.example.streamingappapi.series.PopularSeriesFragment;
import com.example.streamingappapi.series.PopularSeriesItems;
import com.example.streamingappapi.series.PopularSeriesRecItemAdapter;
import com.example.streamingappapi.tv.TvFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    ViewPager2 viewPager2;
    LinearLayout dotIndicator;
    private int dotCount;
    private HomeStartPagerAdapter homeStartPagerAdapter;
    private List<HomeStartItems> homeStartItemsList;
    private HomeStartCardRecItemAdapter homeStartCardRecItemAdapter;
    private List<HomeStartCardListItems> homeStartCardListItems;
    private Handler sliderHandler;
    private Runnable sliderRunnable;
    private int currentPage = 0;
//    CardView mainCv, firstCv, secondCv, thirdCv, fourthCv, fifthCv;
//    ImageView mainIv, firstIv, secondIv, thirdIv, fourthIv, fifthIv;
    private RecyclerView recVHomeStartCardItems, recVPopularMovies, recVContinueWatching, recVCategoryHome, recVNowOnTv, recVPopularMovies1, recVPopularSeries;
    RecyclerView.LayoutManager homeStartCardItemsLayoutManager, popularMoviesLayoutManager, continueWatchingLayoutManager, nowOnTvLayoutManager, popularMovies1LayoutManager, popularSeriesLayoutManager;
    private GridLayoutManager categoryHomeLayoutManager;
    TextView seeAllPopularMoviesTv, seeAllContinueWatchingTv, seeAllNowOnTv, seeAllPopularMoviesTv1, seeAllPopularSeriesTv;
//    private PopularMovieRecItemAdapter popularMovieRecItemAdapter;
    private RequestQueue movieRequestQueue;
    private List<MovieItem> movieItemList;
    private MovieRecItemAdapter movieRecItemAdapter;
//    private List<PopularMovieItems> popularMovieItemsList;
    private ContinueWatchingItemAdapter continueWatchingItemAdapter;
    private List<ContinueWatchingItems> continueWatchingItemsList;
    private CategoryHomeRecItemAdapter categoryHomeRecItemAdapter;
    private List<CategoryHomeItems> categoryHomeItemsList;
    private NowOnTvItemAdapter nowOnTvItemAdapter;
    private List<NowOnTvItems> nowOnTvItemsList;
    private List<PopularSeriesItems> popularSeriesItemsList;
    private PopularSeriesRecItemAdapter popularSeriesRecItemAdapter;
/*
    private StringRequest movieStringRequest;
    private List<HomeStartItems> generateHomeStartItems() {
        List<HomeStartItems> itemList = new ArrayList<>();
        itemList.add(new HomeStartItems("7.3","Venom 3", R.drawable.venom3verticalnew));
        itemList.add(new HomeStartItems("7.0","Avatar The Last Airbender", R.drawable.avatarthelastairbenderverticalnew));
        itemList.add(new HomeStartItems("8.0","Avengers : Endgame", R.drawable.avengersverticalnew));
        itemList.add(new HomeStartItems("6.5","Avatar The Way of Water", R.drawable.avatarthewayofwaterverticalnew2));
        itemList.add(new HomeStartItems("7.2","Kalki - 1", R.drawable.kalkiverticalnew));
        itemList.add(new HomeStartItems("7.3","Captain Americe", R.drawable.captainamericaverticalnew));

        return itemList;
    }
    private List<HomeStartCardListItems> generateHomeStartCardListItems() {
        List<HomeStartCardListItems> itemsList = new ArrayList<>();
        itemsList.add(new HomeStartCardListItems(R.drawable.venom3));
        itemsList.add(new HomeStartCardListItems(R.drawable.avatarthelastairbender));
        itemsList.add(new HomeStartCardListItems(R.drawable.avengers));
        itemsList.add(new HomeStartCardListItems(R.drawable.avatarthewayofwater));
        itemsList.add(new HomeStartCardListItems(R.drawable.kalki));
        itemsList.add(new HomeStartCardListItems(R.drawable.captainamerica));
        return itemsList;
    }
*/

    private void sendMovieRequest(){
        String url = "https://mocki.io/v1/bcecc02f-8196-4739-bfe5-e7d84875b086";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        movieItemList.clear(); // Clear the list before adding new items

                        for (int i = 0 ; i < response.length() ; i ++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                int id = jsonObject.getInt("id");
                                String title = jsonObject.getString("title");
                                String poster = jsonObject.getString("poster");
                                int year = jsonObject.getInt("year");
                                String country = jsonObject.getString("country");
                                double imdb_rating = jsonObject.getDouble("imdb_rating");

                                JSONArray genresArray = jsonObject.getJSONArray("genres");
                                List<String> genres = new ArrayList<>();

                                for (int j = 0; j < genresArray.length(); j++) {
                                    genres.add(genresArray.getString(j));
                                }

                                JSONArray crewArray = jsonObject.getJSONArray("crew");
                                List<CastItems> crew = new ArrayList<>();
                                for (int j = 0; j < crewArray.length(); j++) {
                                    JSONObject crewObject = crewArray.getJSONObject(j);
                                    String name = crewObject.getString("name");
                                    String designation = crewObject.getString("designation");

                                    JSONArray imagesArray = crewObject.getJSONArray("images");
                                    List<String> images = new ArrayList<>();
                                    for (int k = 0; k < imagesArray.length(); k++) {
                                        images.add(imagesArray.getString(k));
                                    }

                                    String about = crewObject.getString("about");
                                    CastItems castItem = new CastItems(name, designation, images, about);
                                    crew.add(castItem);
                                }

                                String plot = jsonObject.getString("plot");

                                JSONArray trailersArray = jsonObject.getJSONArray("trailers");
                                List<String> trailers = new ArrayList<>();
                                for (int j = 0; j < trailersArray.length(); j++) {
                                    trailers.add(trailersArray.getString(j));
                                }

                                int runtime = jsonObject.getInt("runtime");
                                String awards = jsonObject.getString("awards");
                                String language = jsonObject.getString("language");
                                String boxOffice = jsonObject.getString("boxOffice");
                                String production = jsonObject.getString("production");
                                String website = jsonObject.getString("website");

                                JSONArray imagesArray = jsonObject.getJSONArray("images");
                                List<String> images = new ArrayList<>();
                                for (int j = 0; j < imagesArray.length(); j++) {
                                    images.add(imagesArray.getString(j));
                                }

                                String url = jsonObject.getString("url");

                                MovieItem movie = new MovieItem(id, title, poster, year, country, imdb_rating, genres, crew, plot, trailers, runtime, awards, language, boxOffice, production, website, images, url);
                                movieItemList.add(movie);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Only take the first 5 items for recVHomeStartCardItems
                            List<MovieItem> firstSixMovies = movieItemList.subList(0, Math.min(6, movieItemList.size()));
//                            MovieRecItemAdapter homeStartCardAdapter = new MovieRecItemAdapter(getContext(), firstFiveMovies);
//                            recVHomeStartCardItems.setAdapter(homeStartCardAdapter);

//                            homeStartItemsList = generateHomeStartItems();
                            homeStartPagerAdapter = new HomeStartPagerAdapter(firstSixMovies, movieItemList);
                            viewPager2.setAdapter(homeStartPagerAdapter);

                            dotCount = firstSixMovies.size();
                            setupDotIndicator();

//                            homeStartCardListItems = generateHomeStartCardListItems();
                            homeStartCardRecItemAdapter = new HomeStartCardRecItemAdapter(firstSixMovies, position -> {
                                viewPager2.setCurrentItem(position, true);
                            });
                            recVHomeStartCardItems.setAdapter(homeStartCardRecItemAdapter);

                            movieRecItemAdapter = new MovieRecItemAdapter(getContext(), movieItemList);
                            recVPopularMovies.setAdapter(movieRecItemAdapter);
                            recVPopularMovies1.setAdapter(movieRecItemAdapter);

                            categoryHomeItemsList = generateCategoryHomeItemList();
                            categoryHomeRecItemAdapter = new CategoryHomeRecItemAdapter(getContext(), categoryHomeItemsList, movieItemList, popularSeriesItemsList, getFragmentManager());
                            recVCategoryHome.setAdapter(categoryHomeRecItemAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("HomeFragment", error.getMessage());
            }
        });
        movieRequestQueue.add(jsonArrayRequest);
    }
/*
    private List<PopularMovieItems> generatePopularMovieItemList() {
        List<PopularMovieItems> itemList = new ArrayList<>();
        itemList.add(new PopularMovieItems("7.2", "Venom 3", R.drawable.venom3verticalnew));
        itemList.add(new PopularMovieItems("6.8", "Kalki", R.drawable.kalkiverticalnew));
        itemList.add(new PopularMovieItems("8.0", "Avengers Endgame", R.drawable.avengersverticalnew));
        itemList.add(new PopularMovieItems("7.0", "Avatar The Last Airbender", R.drawable.avatarthelastairbenderverticalnew));
        itemList.add(new PopularMovieItems("7.3", "Captain America", R.drawable.captainamericaverticalnew));
        itemList.add(new PopularMovieItems("6.5", "Avatar The Way of Water", R.drawable.avatarthewayofwaterverticalnew1));

        return itemList;
    }
*/

    private List<ContinueWatchingItems> generateContinueWatchingItemList() {
        List<ContinueWatchingItems> itemList = new ArrayList<>();
        itemList.add(new ContinueWatchingItems("Venom 3", "",R.drawable.venom3));
        itemList.add(new ContinueWatchingItems("Stranger Things - Season 1","Episode 1 Winter is Coming",R.drawable.strangerthings1));

        return itemList;
    }

    private List<CategoryHomeItems> generateCategoryHomeItemList() {
        List<CategoryHomeItems> itemList = new ArrayList<>();
        itemList.add(new CategoryHomeItems("TV CHANNELS", R.drawable.strthings));
        itemList.add(new CategoryHomeItems("MOVIES", R.drawable.spartans));
        itemList.add(new CategoryHomeItems("CARTOONS", R.drawable.anime));
        itemList.add(new CategoryHomeItems("SCI-FI", R.drawable.scifi));
        itemList.add(new CategoryHomeItems("SPORT", R.drawable.sports));
        itemList.add(new CategoryHomeItems("SERIES", R.drawable.strthings));
        itemList.add(new CategoryHomeItems("TV SHOWS", R.drawable.tvshows));

        return itemList;
    }

    private List<NowOnTvItems> generateNowOnTvList() {
        List<NowOnTvItems> itemList = new ArrayList<>();
        itemList.add(new NowOnTvItems("ESPN", "NBA Playoff Game-2","11.35-12.50",R.drawable.spart));
        itemList.add(new NowOnTvItems("FOX", "Stranger Things","12.35-01.50",R.drawable.strthings));
        itemList.add(new NowOnTvItems("SPORTS 18", "IND VS BAN","11.35-12.50",R.drawable.scifi1));

        return itemList;
    }

    private List<PopularSeriesItems> generatePopularSeriesItemList() {
        List<PopularSeriesItems> itemList = new ArrayList<>();
        itemList.add(new PopularSeriesItems("7.2", "Game of thrones", R.drawable.got));
        itemList.add(new PopularSeriesItems("6.8", "Dark", R.drawable.dark));
        itemList.add(new PopularSeriesItems("8.0", "The Boys", R.drawable.theboys));
        itemList.add(new PopularSeriesItems("7.0", "The 100", R.drawable.the100));
        itemList.add(new PopularSeriesItems("7.3", "Breaking Bad", R.drawable.brbanew));
        itemList.add(new PopularSeriesItems("6.5", "Prison Break", R.drawable.prisonbreakverticalnew));

        return itemList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager2=view.findViewById(R.id.homeStartViewPager);
        dotIndicator=view.findViewById(R.id.dotIndicator);

        recVHomeStartCardItems = view.findViewById(R.id.homeStartCardItems);

        seeAllPopularMoviesTv = view.findViewById(R.id.seeAllPopularMoviesTv);
        recVPopularMovies = view.findViewById(R.id.recVPopularMovies);

        seeAllContinueWatchingTv = view.findViewById(R.id.seeAllContinueWatchingTv);
        recVContinueWatching = view.findViewById(R.id.recVContinueWatching);

        recVCategoryHome = view.findViewById(R.id.recVCategories);

        seeAllNowOnTv = view.findViewById(R.id.seeAllNowOnTv);
        recVNowOnTv = view.findViewById(R.id.recVNowonTv);

        seeAllPopularMoviesTv1 = view.findViewById(R.id.seeAllPopularMovies1Tv);
        recVPopularMovies1 = view.findViewById(R.id.recVPopularMovies1);

        seeAllPopularSeriesTv = view.findViewById(R.id.seeAllPopularSeriesTv);
        recVPopularSeries = view.findViewById(R.id.recVPopularSeries);

//        homeStartItemsList = generateHomeStartItems();
//        homeStartPagerAdapter = new HomeStartPagerAdapter(homeStartItemsList);
//        viewPager2.setAdapter(homeStartPagerAdapter);

        homeStartCardItemsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recVHomeStartCardItems.setLayoutManager(homeStartCardItemsLayoutManager);

        recVHomeStartCardItems.setHasFixedSize(true);

//        homeStartCardRecItemAdapter.notifyDataSetChanged();


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDotIndicator(position);
                homeStartCardRecItemAdapter.updateSelectedPosition(position);
                scrollToPosition(position);
                currentPage = position;
            }
        });

        sliderHandler = new Handler(Looper.getMainLooper());
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextPage = (currentPage + 1) % dotCount;
                viewPager2.setCurrentItem(nextPage, true);
                scrollToPosition(nextPage); // Scroll RecyclerView to match ViewPager2
                sliderHandler.postDelayed(this, 5000);
            }
        };

        sliderHandler.postDelayed(sliderRunnable, 5000);

//        sendMovieRequest();


        seeAllPopularMoviesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        PopularMoviesFragment popularMoviesFragment = new PopularMoviesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("popularMovieItems", (ArrayList<? extends Parcelable>) movieItemList);
                        popularMoviesFragment.setArguments(bundle);

                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, popularMoviesFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
            }
        });

        popularMoviesLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recVPopularMovies.setLayoutManager(popularMoviesLayoutManager);
        recVPopularMovies.setHasFixedSize(true);
//        popularMovieItemsList = generatePopularMovieItemList();
//        movieItemList = sendMovieRequest();
        movieRequestQueue = MovieVolleySingleton.getInstance(getContext()).getMovieRequestQueue();
        movieItemList = new ArrayList<>();

        sendMovieRequest();
//        movieRecItemAdapter = new MovieRecItemAdapter(getContext(), movieItemList);
//        recVPopularMovies.setAdapter(movieRecItemAdapter);
//        popularMovieRecItemAdapter = new PopularMovieRecItemAdapter(getContext(), popularMovieItemsList);
//        recVPopularMovies.setAdapter(popularMovieRecItemAdapter);


        seeAllContinueWatchingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContinueWatchingFragment continueWatchingFragment = new ContinueWatchingFragment();

                // Prepare data to pass to the ContinueWatchingFragment
                List<ContinueWatchingItems> continueWatchingItemsList = generateContinueWatchingItemList();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("continueWatchingItems", (ArrayList<? extends Parcelable>) new ArrayList<>(continueWatchingItemsList));
                continueWatchingFragment.setArguments(bundle);

                // Transition to ContinueWatchingFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, continueWatchingFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        continueWatchingLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recVContinueWatching.setLayoutManager(continueWatchingLayoutManager);
        continueWatchingItemsList = generateContinueWatchingItemList();
        continueWatchingItemAdapter = new ContinueWatchingItemAdapter(getContext(), continueWatchingItemsList);
        recVContinueWatching.setAdapter(continueWatchingItemAdapter);
        recVContinueWatching.setHasFixedSize(true);

        categoryHomeLayoutManager=new GridLayoutManager(getContext(), 2);
        recVCategoryHome.setLayoutManager(categoryHomeLayoutManager);


        recVCategoryHome.setHasFixedSize(true);

        seeAllNowOnTv.setOnClickListener(v -> {
            TvFragment tvFragment = new TvFragment();

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, tvFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        nowOnTvLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recVNowOnTv.setLayoutManager(nowOnTvLayoutManager);
        nowOnTvItemsList = generateNowOnTvList();
        nowOnTvItemAdapter = new NowOnTvItemAdapter(getContext(), nowOnTvItemsList);
        recVNowOnTv.setAdapter(nowOnTvItemAdapter);
        recVNowOnTv.setHasFixedSize(true);

        seeAllPopularMoviesTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        PopularMoviesFragment popularMoviesFragment = new PopularMoviesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("popularMovieItems", (ArrayList<? extends Parcelable>) movieItemList);
                        popularMoviesFragment.setArguments(bundle);

                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, popularMoviesFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
            }
        });

        popularMovies1LayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recVPopularMovies1.setLayoutManager(popularMovies1LayoutManager);
        recVPopularMovies1.setHasFixedSize(true);

//        movieRequestQueue = MovieVolleySingleton.getInstance(getContext()).getMovieRequestQueue();
//        movieItemList = new ArrayList<>();
//
//        sendMovieRequest();


        popularSeriesLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recVPopularSeries.setLayoutManager(popularSeriesLayoutManager);
        popularSeriesItemsList = generatePopularSeriesItemList();
        popularSeriesRecItemAdapter = new PopularSeriesRecItemAdapter(getContext(), popularSeriesItemsList);
        recVPopularSeries.setAdapter(popularSeriesRecItemAdapter);
        recVPopularSeries.setHasFixedSize(true);

        seeAllPopularSeriesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopularSeriesFragment popularSeriesFragment = new PopularSeriesFragment();

                List<PopularSeriesItems> popularSeriesItemsList = generatePopularSeriesItemList();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("popularSeriesItems", (ArrayList<? extends Parcelable>) new ArrayList<>(popularSeriesItemsList));
                popularSeriesFragment.setArguments(bundle);


                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, popularSeriesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void setupDotIndicator() {
        dotIndicator.removeAllViews(); // Clear previous dots

        for (int i = 0; i < dotCount; i++) {
            ImageView dot = new ImageView(requireContext());
            dot.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
            dot.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.dot_selector));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotIndicator.addView(dot, params);
        }

        updateDotIndicator(0); // Set the first dot as active
    }

    private void updateDotIndicator(int index) {
        for (int i = 0; i < dotIndicator.getChildCount(); i++) {
            ImageView dot = (ImageView) dotIndicator.getChildAt(i);
            if (i == index) {
                dot.setSelected(true);
            } else {
                dot.setSelected(false);
            }
        }
    }

    private void scrollToPosition(int position) {
        // Ensure RecyclerView and Adapter are initialized
        if (recVHomeStartCardItems != null && homeStartCardRecItemAdapter != null) {
            recVHomeStartCardItems.smoothScrollToPosition(position);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}