package com.example.streamingappapi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streamingappapi.movie.MovieItem;
import com.example.streamingappapi.series.PopularSeriesItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AboutFragment extends Fragment {

    private RecyclerView recVAbout, recVPhotos;
    private CastRecItemAdapter castRecItemAdapter;
    private RecyclerView.LayoutManager castLayoutManager, aboutPhotosLayoutManager;
    private List<CastItems> castItemsList;
    private AboutPhotosRecItemAdapter aboutPhotosRecItemAdapter;
    private ArrayList<String> aboutPhotosItemsList;
//    private List<PopularMovieItems> popularMovieItemsList;
    private List<MovieItem> movieItemList;
    private ArrayList<String> movieImages;
    private List<PopularSeriesItems> popularSeriesItemsList;
    private boolean isMovieList;
    private TextView audioTrackTv, subtitleTrackTv, countryTv, yearTv;
    private String year;
    private String country;
    private List<CastItems> generateCastList() {
        List<CastItems> itemList = new ArrayList<>();
        itemList.add(new CastItems("Sam Worthington", "Actor", Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/2/2b/Avatar_The_Way_of_Water_Tokyo_Press_Conference_Sam_Worthington_%2852563252594%29_%28cropped%29.jpg"), "Samuel Henry John Worthington (born 2 August 1976) is an Australian actor. He is known for playing Jake Sully in the Avatar franchise, Marcus Wright in Terminator Salvation, and Perseus in Clash of the Titans and its sequel Wrath of the Titans.He has taken other dramatic roles, appearing in The Debt (2010), Everest (2015), Hacksaw Ridge (2016), The Shack (2017), Manhunt (2017), and Fractured (2019). He has received Australia's highest film award three times for his leading roles in Bootmen (2000), Somersault (2004), and Avatar (2009"));
        itemList.add(new CastItems("Zoe Saldana", "Actor",Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/0/0d/Avatar_The_Way_of_Water_Tokyo_Press_Conference_Zoe_Salda%C3%B1a_%2852563431865%29_%28cropped2%29.jpg"), "Zoë Yadira Saldaña-Perego (/sɑːlˈdænə/ sahl-DAN-ə,[2] Spanish: [ˈsoe salˈdaɲa]; née Saldaña Nazario; born June 19, 1978) is an American actress. Known primarily for her work in science fiction film franchises, she has starred in four of the highest-grossing films of all time (Avatar, Avatar: The Way of Water, Avengers: Infinity War, and Avengers: Endgame). Films she has appeared in have grossed more than $15 billion worldwide and, as of 2024, she is the second highest-grossing film actress. Time magazine named her one of the 100 most influential people in the world in 2023."));
        itemList.add(new CastItems("Michele Rodriguez", "Actor",Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/f/fd/Michelle_Rodriguez_Cannes_2018_cropped.jpg"), "Mayte Michelle Rodríguez (born July 12, 1978) is an American actress. She began her career in 2000, playing a troubled boxer in the independent sports drama film Girlfight (2000), where she won the Independent Spirit Award and Gotham Award for Best Debut Performance. Rodriguez plays Letty Ortiz in the Fast & Furious franchise, and portrayed Rain Ocampo in the Resident Evil franchise. She appeared in the crime thriller S.W.A.T. (2003), and later starred in James Cameron's science fiction epic Avatar (2009) and in the action film Battle: Los Angeles (2011)."));
        itemList.add(new CastItems("Sigourney Weaver", "Actor",Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/8/8d/Sigourney_Weaver_by_Gage_Skidmore_4.jpg"), "Susan Alexandra \"Sigourney\" Weaver (/sɪˈɡɔːrni/; born October 8, 1949) is an American actress and producer. A popular culture figure, Weaver is known for her leading roles in science fiction and horror franchises, having amassed several screen and stage credits since her debut in 1971. Her accolades include a British Academy Film Award, two Golden Globe Awards, and a Grammy Award, and nominations for three Academy Awards, four Primetime Emmy Awards, and a Tony Award. She appeared on Channel 4's list of the 100 greatest movie stars of all time in 2003."));
        itemList.add(new CastItems("Stephen Lang", "Actor",Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/a/a5/Stephen_Lang_2014.jpg"), "Stephen Lang (born July 11, 1952) is an American actor. He is known for roles in films such as Manhunter (1986), Gettysburg and Tombstone (both 1993), Gods and Generals (2003), Public Enemies and The Men Who Stare at Goats (both 2009), Conan the Barbarian (2011) and Don't Breathe (2016)."));

        return itemList;
    }

    private List<String> generatePhotosList() {
        List<String> itemsList = new ArrayList<>();
        itemsList.add("https://upload.wikimedia.org/wikipedia/en/d/d8/Game_of_Thrones_title_card.jpg");
        itemsList.add("https://upload.wikimedia.org/wikipedia/en/d/da/DarkNetflixPosterEnglish.jpg");
        itemsList.add("https://upload.wikimedia.org/wikipedia/commons/6/68/The_Boys_TV_series_logo.svg");
        itemsList.add("https://upload.wikimedia.org/wikipedia/en/6/6c/Logo_of_the_100.jpg");
        itemsList.add("https://upload.wikimedia.org/wikipedia/commons/7/77/Breaking_Bad_logo.svg");
        itemsList.add("https://upload.wikimedia.org/wikipedia/en/d/d3/Prison-break-s1-intro.jpg");

        return itemsList;
    }

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstanceWithMoviesAndCrew(List<MovieItem> popularMovieItemsList, List<CastItems> crew, ArrayList<String> movieImages, String year,
                                                             String country) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("popularMovieItemsList", new ArrayList<>(popularMovieItemsList != null ? popularMovieItemsList : new ArrayList<>()));
        args.putParcelableArrayList("crewList", new ArrayList<>(crew != null ? crew : new ArrayList<>()));
        args.putStringArrayList("movieImages", new ArrayList<>(movieImages != null ? movieImages : new ArrayList<>()));
        args.putString("year", year);
        args.putString("country", country);
        args.putBoolean("isMovieList", true);
        fragment.setArguments(args);
        return fragment;
    }

    public static AboutFragment newInstanceWithSeries(List<PopularSeriesItems> popularSeriesItemsList) {
        AboutFragment fragment = new AboutFragment();
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
                castItemsList = getArguments().getParcelableArrayList("crewList");
                movieImages = getArguments().getStringArrayList("movieImages");
                year = getArguments().getString("year");
                country = getArguments().getString("country");
                Log.d("AboutFragment", "Received movie items: " + movieItemList);
            } else {
                popularSeriesItemsList = getArguments().getParcelableArrayList("popularSeriesItemsList");
                Log.d("AboutFragment", "Received series items: " + popularSeriesItemsList);
            }
        } else {
            Log.d("AboutFragment", "getArguments() is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        audioTrackTv = view.findViewById(R.id.audioTrackValTv);
        subtitleTrackTv = view.findViewById(R.id.subtitleValTv);
        countryTv = view.findViewById(R.id.countryValTv);
        yearTv = view.findViewById(R.id.yearValTv);
        recVAbout = view.findViewById(R.id.recVCast);
        recVPhotos = view.findViewById(R.id.recVPhotos);

        castLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recVAbout.setLayoutManager(castLayoutManager);

//        castItemsList = generateCastList();

        aboutPhotosLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recVPhotos.setLayoutManager(aboutPhotosLayoutManager);

        if (isMovieList) {
            if (movieItemList != null && !movieItemList.isEmpty()) {
                castRecItemAdapter = new CastRecItemAdapter<>(getContext(), castItemsList, movieItemList);
                yearTv.setText(year);
                countryTv.setText(country);
                if (movieImages != null) {
                    aboutPhotosRecItemAdapter = new AboutPhotosRecItemAdapter(getContext(), movieImages);
                } else {
                    Log.d("AboutFragment", "movieImages list is null");
                }
            } else {
                Log.d("AboutFragment", "Movie list is empty or null");
            }
        } else {
            if (popularSeriesItemsList != null && !popularSeriesItemsList.isEmpty()) {
                castItemsList = generateCastList();
                castRecItemAdapter = new CastRecItemAdapter<>(getContext(), castItemsList, popularSeriesItemsList);
                aboutPhotosItemsList = (ArrayList<String>) generatePhotosList();
                aboutPhotosRecItemAdapter = new AboutPhotosRecItemAdapter(getContext(), aboutPhotosItemsList);
            }  else {
                Log.d("AboutFragment", "Series list is empty or null");
            }
        }

        recVAbout.setAdapter(castRecItemAdapter);
        recVAbout.setHasFixedSize(true);

        recVPhotos.setAdapter(aboutPhotosRecItemAdapter);
        recVPhotos.setHasFixedSize(true);

        return view;
    }
}