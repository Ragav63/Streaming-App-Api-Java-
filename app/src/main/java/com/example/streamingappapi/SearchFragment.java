package com.example.streamingappapi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.movie.MovieRecItemAdapter;
import com.example.streamingappapi.movie.MovieVolleySingleton;
import com.example.streamingappapi.series.PopularSeriesItems;
import com.example.streamingappapi.series.PopularSeriesRecItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchFragment extends Fragment {
    EditText searchEdt;
    TextView cancelTv, searchTitleVideo, searchTitleSeries, searchTitleNowOnTv, searchTitleActors, filterTv, seeAllTv;
    private RecyclerView recVPopularMovies, recVPopularSeries, recVNowOnTv, recVAbout;
    private GridLayoutManager popularMoviesLayoutManager, popularSeriesLayoutManager, castLayoutManager;
    private LinearLayoutManager popularMoviesLinearLayoutManager, popularSeriesLinearLayoutManager, nowOnTvLinearLayoutManager, castLinearLayoutManager;
    private RequestQueue movieRequestQueue;
    private List<MovieItem> movieItemList;
    private MovieRecItemAdapter movieRecItemAdapter;
//    private PopularMovieRecItemAdapter popularMovieRecItemAdapter;
//    private List<PopularMovieItems> popularMovieItemsList;
    private List<PopularSeriesItems> popularSeriesItemsList;
    private PopularSeriesRecItemAdapter popularSeriesRecItemAdapter;
    private NowOnTvItemAdapter nowOnTvItemAdapter;
    private List<NowOnTvItems> nowOnTvItemsList;
    private CastRecItemAdapter castRecItemAdapter;
    private List<CastItems> castItemsList;

//    private List<PopularMovieItems> generatePopularMovieItemList() {
//        List<PopularMovieItems> itemList = new ArrayList<>();
//        itemList.add(new PopularMovieItems("7.2", "Venom 3", R.drawable.venom3vertical));
//        itemList.add(new PopularMovieItems("6.8", "Kalki", R.drawable.kalkivertical));
//        itemList.add(new PopularMovieItems("8.0", "Avengers Endgame", R.drawable.avengersvertical));
//        itemList.add(new PopularMovieItems("7.0", "Avatar The Last Airbender", R.drawable.avatarthelastairbendervertical));
//        itemList.add(new PopularMovieItems("7.3", "Captain America", R.drawable.captainamericavertical));
//        itemList.add(new PopularMovieItems("6.5", "Avatar The Way of Water", R.drawable.avatarthewayofwatervertical));
//
//        return itemList;
//    }

    private void sendMovieRequest(){
        String url = "https://mocki.io/v1/bcecc02f-8196-4739-bfe5-e7d84875b086";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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

//                                    castItemsList.add(castItem);
//                                    castRecItemAdapter = new CastRecItemAdapter(getContext(), castItemsList, movieItemList);
//                                    recVAbout.setAdapter(castRecItemAdapter);
//                                    castRecItemAdapter.notifyDataSetChanged();
                                }

                                castItemsList.addAll(crew);

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

                            movieRecItemAdapter = new MovieRecItemAdapter(getContext(), movieItemList);
                            recVPopularMovies.setAdapter(movieRecItemAdapter);
                            movieRecItemAdapter.notifyDataSetChanged();

                            castRecItemAdapter = new CastRecItemAdapter(getContext(), castItemsList, movieItemList);
                            recVAbout.setAdapter(castRecItemAdapter);
                            castRecItemAdapter.notifyDataSetChanged();
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

    private List<PopularSeriesItems> generatePopularSeriesItemList() {
        List<PopularSeriesItems> itemList = new ArrayList<>();
        itemList.add(new PopularSeriesItems("7.2", "Game of thrones", R.drawable.got));
        itemList.add(new PopularSeriesItems("6.8", "Dark", R.drawable.dark));
        itemList.add(new PopularSeriesItems("8.0", "The Boys", R.drawable.theboys));
        itemList.add(new PopularSeriesItems("7.0", "The 100", R.drawable.the100));
        itemList.add(new PopularSeriesItems("7.3", "Breaking Bad", R.drawable.brba));
        itemList.add(new PopularSeriesItems("6.5", "Prison Break", R.drawable.prisonbreak));

        return itemList;
    }

    private List<NowOnTvItems> generateNowOnTvList() {
        List<NowOnTvItems> itemList = new ArrayList<>();
        itemList.add(new NowOnTvItems("ESPN", "NBA Playoff Game-2","11.35-12.50",R.drawable.spart));
        itemList.add(new NowOnTvItems("FOX", "Stranger Things","12.35-01.50",R.drawable.strthings));
        itemList.add(new NowOnTvItems("SPORTS 18", "IND VS BAN","11.35-12.50",R.drawable.scifi1));

        return itemList;
    }

//    private List<CastItems> generateCastList() {
//        List<CastItems> itemList = new ArrayList<>();
//        itemList.add(new CastItems("Sam Worthington", "Actor", Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/2/2b/Avatar_The_Way_of_Water_Tokyo_Press_Conference_Sam_Worthington_%2852563252594%29_%28cropped%29.jpg"), "Samuel Henry John Worthington (born 2 August 1976) is an Australian actor. He is known for playing Jake Sully in the Avatar franchise, Marcus Wright in Terminator Salvation, and Perseus in Clash of the Titans and its sequel Wrath of the Titans.He has taken other dramatic roles, appearing in The Debt (2010), Everest (2015), Hacksaw Ridge (2016), The Shack (2017), Manhunt (2017), and Fractured (2019). He has received Australia's highest film award three times for his leading roles in Bootmen (2000), Somersault (2004), and Avatar (2009"));
//        itemList.add(new CastItems("Zoe Saldana", "Actor",Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/0/0d/Avatar_The_Way_of_Water_Tokyo_Press_Conference_Zoe_Salda%C3%B1a_%2852563431865%29_%28cropped2%29.jpg"), "Zoë Yadira Saldaña-Perego (/sɑːlˈdænə/ sahl-DAN-ə,[2] Spanish: [ˈsoe salˈdaɲa]; née Saldaña Nazario; born June 19, 1978) is an American actress. Known primarily for her work in science fiction film franchises, she has starred in four of the highest-grossing films of all time (Avatar, Avatar: The Way of Water, Avengers: Infinity War, and Avengers: Endgame). Films she has appeared in have grossed more than $15 billion worldwide and, as of 2024, she is the second highest-grossing film actress. Time magazine named her one of the 100 most influential people in the world in 2023."));
//        itemList.add(new CastItems("Michele Rodriguez", "Actor",Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/f/fd/Michelle_Rodriguez_Cannes_2018_cropped.jpg"), "Mayte Michelle Rodríguez (born July 12, 1978) is an American actress. She began her career in 2000, playing a troubled boxer in the independent sports drama film Girlfight (2000), where she won the Independent Spirit Award and Gotham Award for Best Debut Performance. Rodriguez plays Letty Ortiz in the Fast & Furious franchise, and portrayed Rain Ocampo in the Resident Evil franchise. She appeared in the crime thriller S.W.A.T. (2003), and later starred in James Cameron's science fiction epic Avatar (2009) and in the action film Battle: Los Angeles (2011)."));
//        itemList.add(new CastItems("Sigourney Weaver", "Actor",Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/8/8d/Sigourney_Weaver_by_Gage_Skidmore_4.jpg"), "Susan Alexandra \"Sigourney\" Weaver (/sɪˈɡɔːrni/; born October 8, 1949) is an American actress and producer. A popular culture figure, Weaver is known for her leading roles in science fiction and horror franchises, having amassed several screen and stage credits since her debut in 1971. Her accolades include a British Academy Film Award, two Golden Globe Awards, and a Grammy Award, and nominations for three Academy Awards, four Primetime Emmy Awards, and a Tony Award. She appeared on Channel 4's list of the 100 greatest movie stars of all time in 2003."));
//        itemList.add(new CastItems("Stephen Lang", "Actor",Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/a/a5/Stephen_Lang_2014.jpg"), "Stephen Lang (born July 11, 1952) is an American actor. He is known for roles in films such as Manhunter (1986), Gettysburg and Tombstone (both 1993), Gods and Generals (2003), Public Enemies and The Men Who Stare at Goats (both 2009), Conan the Barbarian (2011) and Don't Breathe (2016)."));
//
//        return itemList;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            popularMovieItemsList = getArguments().getParcelableArrayList("popularMovieItems");
//        }
        movieItemList = new ArrayList<>();
        castItemsList =new ArrayList<>();
        popularSeriesItemsList = new ArrayList<>();
    }

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEdt = view.findViewById(R.id.searchEdt);
        cancelTv = view.findViewById(R.id.cancelTv);
        searchTitleVideo = view.findViewById(R.id.searchTitleVideo);
        searchTitleSeries = view.findViewById(R.id.searchTitleSeries);
        searchTitleNowOnTv = view.findViewById(R.id.searchTitleNowOnTv);
        searchTitleActors = view.findViewById(R.id.searchTitleActors);
        filterTv = view.findViewById(R.id.filterTv);
        seeAllTv = view.findViewById(R.id.seeAllTv);

        recVPopularMovies = view.findViewById(R.id.recVPopularMovies);
        recVPopularSeries = view.findViewById(R.id.recVPopularSeries);
        recVNowOnTv = view.findViewById(R.id.recVNowonTv);
        recVAbout = view.findViewById(R.id.recVCast);

        recVNowOnTv.setVisibility(View.GONE);
        recVAbout.setVisibility(View.GONE);

        // Initialize layout managers
        popularMoviesLayoutManager = new GridLayoutManager(getContext(), 2);
        popularSeriesLayoutManager = new GridLayoutManager(getContext(), 2);
        castLayoutManager = new GridLayoutManager(getContext(), 2);

        popularMoviesLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        popularSeriesLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        castLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recVPopularMovies.setLayoutManager(popularMoviesLayoutManager);
        recVPopularSeries.setLayoutManager(popularSeriesLayoutManager);
        recVAbout.setLayoutManager(castLayoutManager);

        recVPopularMovies.setLayoutManager(popularMoviesLayoutManager);
//        popularMovieItemsList = generatePopularMovieItemList();
//        popularMovieRecItemAdapter = new PopularMovieRecItemAdapter(getContext(), popularMovieItemsList);
//        recVPopularMovies.setAdapter(popularMovieRecItemAdapter);
        recVPopularMovies.setHasFixedSize(true);

        movieRequestQueue = MovieVolleySingleton.getInstance(getContext()).getMovieRequestQueue();

        sendMovieRequest();

        recVPopularSeries.setLayoutManager(popularSeriesLayoutManager);
        popularSeriesItemsList = generatePopularSeriesItemList();
        popularSeriesRecItemAdapter = new PopularSeriesRecItemAdapter(getContext(), popularSeriesItemsList);
        recVPopularSeries.setAdapter(popularSeriesRecItemAdapter);
        recVPopularSeries.setHasFixedSize(true);

        nowOnTvLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recVNowOnTv.setLayoutManager(nowOnTvLinearLayoutManager);
        nowOnTvItemsList = generateNowOnTvList();
        nowOnTvItemAdapter = new NowOnTvItemAdapter(getContext(), nowOnTvItemsList);
        recVNowOnTv.setAdapter(nowOnTvItemAdapter);
        recVNowOnTv.setHasFixedSize(true);

        recVAbout.setLayoutManager(castLayoutManager);
//        castItemsList = generateCastList();
//        castRecItemAdapter = new CastRecItemAdapter(getContext(), castItemsList, popularSeriesItemsList);
//        castRecItemAdapter.notifyDataSetChanged();
//        recVAbout.setAdapter(castRecItemAdapter);
        recVAbout.setHasFixedSize(true);

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    ViewGroup.LayoutParams params = searchEdt.getLayoutParams();
                    params.width = dpToPx(300); // Set the width to 300dp
                    searchEdt.setLayoutParams(params);
                    cancelTv.setVisibility(View.VISIBLE);
                    seeAllTv.setVisibility(View.VISIBLE);

//                    searchTitleVideo.setVisibility(View.VISIBLE);
//                    searchTitleSeries.setVisibility(View.VISIBLE);
//                    searchTitleNowOnTv.setVisibility(View.VISIBLE);
//                    searchTitleActors.setVisibility(View.VISIBLE);
//
//                    searchTitleVideo.setText("Video");
//                    searchTitleSeries.setText("Series");
//                    searchTitleNowOnTv.setText("Now On Tv");
//                    searchTitleActors.setText("Actors");

                    recVPopularMovies.setLayoutManager(popularMoviesLinearLayoutManager);
                    recVPopularSeries.setLayoutManager(popularSeriesLinearLayoutManager);
                    recVAbout.setLayoutManager(castLinearLayoutManager);

                    recVNowOnTv.setVisibility(View.VISIBLE);
                    recVAbout.setVisibility(View.VISIBLE);

                    movieRecItemAdapter.getFilter().filter(s);
                    popularSeriesRecItemAdapter.getFilter().filter(s);
                    nowOnTvItemAdapter.getFilter().filter(s);
                    castRecItemAdapter.getFilter().filter(s);

//                    List<MovieItem> filteredMovies = new ArrayList<>();
//                    for (MovieItem item : movieItemList) {
//                        if (item.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
//                            filteredMovies.add(item);
//                        }
//                    }
//                    movieRecItemAdapter.getFilter().filter(filteredMovies.toString());
//
//                    // Filter popular series based on search query
//                    List<PopularSeriesItems> filteredSeries = new ArrayList<>();
//                    for (PopularSeriesItems item : popularSeriesItemsList) {
//                        if (item.getPopularSeriesTitle().toLowerCase().contains(s.toString().toLowerCase())) {
//                            filteredSeries.add(item);
//                        }
//                    }
//                    popularSeriesRecItemAdapter.getFilter().filter(filteredSeries.toString());
//
//                    // Filter now on TV items based on search query
//                    List<NowOnTvItems> filteredNowOnTv = new ArrayList<>();
//                    for (NowOnTvItems item : nowOnTvItemsList) {
//                        if (item.getNowOnTvTitle().toLowerCase().contains(s.toString().toLowerCase())) {
//                            filteredNowOnTv.add(item);
//                        }
//                    }
//                    nowOnTvItemAdapter.getFilter().filter(filteredNowOnTv.toString());
//
//                    // Filter cast items based on search query
//                    List<CastItems> filteredCast = new ArrayList<>();
//                    for (CastItems item : castItemsList) {
//                        if (item.getName().toLowerCase().contains(s.toString().toLowerCase())) {
//                            filteredCast.add(item);
//                        }
//                    }
//                    castRecItemAdapter.updateList(filteredCast);

                    updateSearchTitleVisibility();

                } else {
                    ViewGroup.LayoutParams params = searchEdt.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT; // Set the width back to match_parent
                    searchEdt.setLayoutParams(params);
                    cancelTv.setVisibility(View.GONE);

                    searchTitleVideo.setText("What search last");

                    seeAllTv.setVisibility(View.GONE);
                    searchTitleVideo.setVisibility(View.VISIBLE);
                    searchTitleSeries.setVisibility(View.GONE);
                    searchTitleNowOnTv.setVisibility(View.GONE);
                    searchTitleActors.setVisibility(View.GONE);

                    recVNowOnTv.setVisibility(View.GONE);
                    recVAbout.setVisibility(View.GONE);
                    // Reset Layout Managers to GridLayoutManager
                    recVPopularMovies.setLayoutManager(popularMoviesLayoutManager);
                    recVPopularSeries.setLayoutManager(popularSeriesLayoutManager);
                    recVAbout.setLayoutManager(castLayoutManager);

                    movieRecItemAdapter.getFilter().filter("");
                    popularSeriesRecItemAdapter.getFilter().filter("");
                    nowOnTvItemAdapter.getFilter().filter("");
                    castRecItemAdapter.getFilter().filter("");

                    updateSearchTitleVisibility();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cancelTv.setOnClickListener(v -> {
            searchEdt.setText("");
        });

        filterTv.setOnClickListener(v -> {
            FiltersFragment filtersFragment = new FiltersFragment();

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, filtersFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        seeAllTv.setOnClickListener(v -> {
            Log.d("SearchFragment", "See All clicked");

            // Clear the search input
            searchEdt.setText("");

            searchTitleVideo.setVisibility(View.VISIBLE);
            searchTitleSeries.setVisibility(View.VISIBLE);
            searchTitleNowOnTv.setVisibility(View.VISIBLE);
            searchTitleActors.setVisibility(View.VISIBLE);

            searchTitleVideo.setText("Video");
            searchTitleSeries.setText("Series");
            searchTitleNowOnTv.setText("Now On Tv");
            searchTitleActors.setText("Actors");

            recVPopularMovies.setVisibility(View.VISIBLE);
            recVPopularSeries.setVisibility(View.VISIBLE);
            recVNowOnTv.setVisibility(View.VISIBLE);
            recVAbout.setVisibility(View.VISIBLE);

            recVPopularMovies.setLayoutManager(popularMoviesLayoutManager);
            recVPopularSeries.setLayoutManager(popularSeriesLayoutManager);
            recVNowOnTv.setLayoutManager(nowOnTvLinearLayoutManager);
            recVAbout.setLayoutManager(castLayoutManager);

            // Notify adapters
            movieRecItemAdapter.notifyDataSetChanged();
            popularSeriesRecItemAdapter.notifyDataSetChanged();
            nowOnTvItemAdapter.notifyDataSetChanged();
            castRecItemAdapter.notifyDataSetChanged();

//            // Debug logging
//            Log.d("SearchFragment", "PopularMovies items count: " + popularMovieItemsList.size());
//            Log.d("SearchFragment", "PopularSeries items count: " + popularSeriesItemsList.size());
//            Log.d("SearchFragment", "NowOnTv items count: " + nowOnTvItemsList.size());
//            Log.d("SearchFragment", "Cast items count: " + castItemsList.size());
        });

        return view;
    }

    private void updateSearchTitleVisibility() {
        boolean isPopularMoviesEmpty = movieRecItemAdapter.isDataEmpty();
        boolean isPopularSeriesEmpty = popularSeriesRecItemAdapter.isDataEmpty();
        boolean isNowOnTvEmpty = nowOnTvItemAdapter.isDataEmpty();
        boolean isCastEmpty = castRecItemAdapter.isDataEmpty();

        Log.d("SearchFragment", "Popular Movies Empty: " + isPopularMoviesEmpty);
        Log.d("SearchFragment", "Popular Series Empty: " + isPopularSeriesEmpty);
        Log.d("SearchFragment", "Now On TV Empty: " + isNowOnTvEmpty);
        Log.d("SearchFragment", "Cast Empty: " + isCastEmpty);

        if (isPopularMoviesEmpty) {
            searchTitleVideo.setText("No data found");
            searchTitleVideo.setVisibility(View.VISIBLE);
        } else {
            searchTitleVideo.setText("Video");
            searchTitleVideo.setVisibility(View.VISIBLE);
        }

        if (isPopularSeriesEmpty) {
            searchTitleSeries.setVisibility(View.GONE);
        } else {
            searchTitleSeries.setVisibility(View.VISIBLE);
            searchTitleSeries.setText("Series");
        }

        if (isNowOnTvEmpty) {
            searchTitleNowOnTv.setVisibility(View.GONE);
        } else {
            searchTitleNowOnTv.setVisibility(View.VISIBLE);
            searchTitleNowOnTv.setText("Now On TV");
        }

        if (isCastEmpty) {
            searchTitleActors.setVisibility(View.GONE);
        } else {
            searchTitleActors.setVisibility(View.VISIBLE);
            searchTitleActors.setText("Actors");
        }
    }


    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}