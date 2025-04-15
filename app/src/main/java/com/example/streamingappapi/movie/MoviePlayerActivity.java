package com.example.streamingappapi.movie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.streamingappapi.AboutFragment;
import com.example.streamingappapi.CastItems;
import com.example.streamingappapi.FullScreenImageActivity;
import com.example.streamingappapi.R;
import com.example.streamingappapi.trailers.TrailersFragment;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Objects;

public class MoviePlayerActivity extends AppCompatActivity {
/*
    ConstraintLayout container;
    ConstraintLayout videoCl;
    //    TextView playerTimingTv;
*/
ImageView posterIv;
//    YouTubePlayerSeekBar playerSBar;
    YouTubePlayerView youTubePlayerView;
//    private Runnable updateSeekBarRunnable;
//    private Runnable hideControlsRunnable;
    private static final int DEFAULT_TINT_COLOR = R.color.white;
    private static final int SELECTED_TINT_COLOR = R.color.bluemain;
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TrailersFragment trailersFragment = new TrailersFragment();
    ImageView favIv, shareIv, downloadIv;
    TextView ratingTv, titleTv, playTv, yearTv, originTv, genreTv, durationTv, plotTv;
    private boolean isFavourite = false;
    private boolean isDownload = false;
//    private static final int REQUEST_CODE_TV_LANDSCAPE = 1001;
    String imageResource;
    private PlayerUiController controller;
    private ArrayList<String> genres = new ArrayList<>();
    private ArrayList<String> trailers = new ArrayList<>();
    private ArrayList<CastItems> crew = new ArrayList<>();
    private ArrayList<MovieItem> movieItemList = new ArrayList<>();
    private String rating, title, plot, year, country, runtime, url;
    private ArrayList<String> movieImages = new ArrayList<>();
    private YouTubePlayerTracker playerTracker;
    private YouTubePlayer youTubePlayer;
//    private boolean isfullScreen = false;
    String videoId, videoIdfromIntent;
    private float currentTime;
/*
    private Handler handler = new Handler(Looper.getMainLooper());
    private final long AUTO_HIDE_DELAY = 10000; // 10 seconds
    private static final String KEY_RATING = "rating";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PLOT = "plot";
    private static final String KEY_YEAR = "year";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_RUNTIME = "runtime";
    private static final String KEY_IMAGE_RESOURCE = "imageResource";
    private static final String KEY_GENRES = "genres";
    private static final String KEY_TRAILERS = "trailers";
    private static final String KEY_CREW = "crew";
    private static final String KEY_MOVIE_IMAGES = "images";
    private static final String KEY_URL = "url";
    private static final String KEY_MOVIE_ITEMS_LIST = "movieItemList";
    private static final String KEY_VIDEO_ID = "videoId";
    private static final String KEY_CURRENT_TIME = "currentTime";
*/
private MovieViewModel viewModel;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

/*
        videoCl = findViewById(R.id.videoCl);
        container = findViewById(R.id.container);
        playerTimingTv = findViewById(R.id.playerTimingTv);
        minmaxScreenIv = findViewById(R.id.minScreenIv);
        shareVideoIv = findViewById(R.id.sharePlayerIv);
        settingsIv = findViewById(R.id.settingsIv);
        fastBackwardIv = findViewById(R.id.backwardIv);
        playIv = findViewById(R.id.playIv);
        fastForwardIv = findViewById(R.id.forwardIv);
        fullScreenIv = findViewById(R.id.fullScreenIv);
        playerSBar = findViewById(R.id.playerSBar);
*/
        youTubePlayerView = findViewById(R.id.youtubeplayer);
        ratingTv = findViewById(R.id.ratingTv);
        titleTv = findViewById(R.id.titleTv);
        yearTv = findViewById(R.id.yearTv);
        genreTv = findViewById(R.id.genreTv);
        originTv = findViewById(R.id.originTv);
//        playTv = findViewById(R.id.playTv);
        posterIv = findViewById(R.id.posterIv);
        favIv = findViewById(R.id.favIv);
        shareIv = findViewById(R.id.shareIv);
        downloadIv = findViewById(R.id.downloadIv);
        durationTv = findViewById(R.id.durationTv);
        plotTv = findViewById(R.id.descTv);
        tabLayout = findViewById(R.id.tab_layout);
        frameLayout = findViewById(R.id.framelayout);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null) {

            imageResource = intent.getStringExtra("imageResource");
            rating = String.valueOf(intent.getDoubleExtra("rating", 0.0));
            title = intent.getStringExtra("title");

            plot = intent.getStringExtra("plot");
            year = String.valueOf(intent.getIntExtra("year", 0));
            country = intent.getStringExtra("country");
            runtime = String.valueOf(intent.getIntExtra("runtime", 0));

            genres = intent.getStringArrayListExtra("genres") != null ? intent.getStringArrayListExtra("genres") : new ArrayList<>();
            trailers = intent.getStringArrayListExtra("trailers") != null ? intent.getStringArrayListExtra("trailers") : new ArrayList<>();
            crew = intent.getParcelableArrayListExtra("crew") != null ? intent.getParcelableArrayListExtra("crew") : new ArrayList<>();
            url = intent.getStringExtra("url");
            movieImages = intent.getStringArrayListExtra("images") != null ? intent.getStringArrayListExtra("images") : new ArrayList<>();
            movieItemList = intent.getParcelableArrayListExtra("popularMovieItemsList") != null ? intent.getParcelableArrayListExtra("popularMovieItemsList") : new ArrayList<>();

            videoIdfromIntent = intent.getStringExtra("videoId");
            currentTime = intent.getFloatExtra("currentTime", 0f);
//        sourceActivity = intent.getStringExtra("sourceActivity");


            if (videoIdfromIntent != null) {
                videoId = videoIdfromIntent;
            } else {
                videoId = extractYouTubeVideoId(url);
            }

            Glide.with(this).load(imageResource).into(posterIv);
            ratingTv.setText(rating);
            titleTv.setText(title);
            String genresText = TextUtils.join(", ", genres);
            genreTv.setText(genresText);
            yearTv.setText(year);
            originTv.setText(country);
            durationTv.setText("PG-13 - "+ formatRuntime(Integer.parseInt(runtime)));
            plotTv.setText(plot);

        } else {

            // Restore data from ViewModel
            imageResource = viewModel.getImageResource().getValue();
            rating = viewModel.getRating().getValue();
            title = viewModel.getTitle().getValue();
            plot = viewModel.getPlot().getValue();
            year = viewModel.getYear().getValue();
            country = viewModel.getCountry().getValue();
            runtime = viewModel.getRuntime().getValue();
            genres = viewModel.getGenres().getValue() != null ? viewModel.getGenres().getValue() : new ArrayList<>();
            trailers = viewModel.getTrailers().getValue() != null ? viewModel.getTrailers().getValue() : new ArrayList<>();
            crew = viewModel.getCrew().getValue() != null ? viewModel.getCrew().getValue() : new ArrayList<>();
            url = viewModel.getUrl().getValue();
            movieImages = viewModel.getMovieImages().getValue() != null ? viewModel.getMovieImages().getValue() : new ArrayList<>();
            movieItemList = viewModel.getMovieItemList().getValue() != null ? viewModel.getMovieItemList().getValue() : new ArrayList<>();
            videoId = viewModel.getVideoId().getValue();
            currentTime = viewModel.getCurrentTime().getValue() != null ? viewModel.getCurrentTime().getValue() : 0F;

            Glide.with(this).load(imageResource).into(posterIv);
            ratingTv.setText(rating);
            titleTv.setText(title);
            String genresText = TextUtils.join(", ", genres);
            genreTv.setText(genresText);
            yearTv.setText(year);
            originTv.setText(country);
            durationTv.setText("PG-13 - "+ formatRuntime(Integer.parseInt(runtime)));
            plotTv.setText(plot);

        }

/*
        if (movieItemList == null) {
            // Handle case where list is not passed correctly
            Toast.makeText(this, "Error: No movie items found", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no list is found
            return;
        } else {
            Log.d("MovieScreenActivity", "popularMovieItemsList size: " + movieItemList.size());
        }

        if (movieItemList.isEmpty()) {
            // Handle case where list is empty
            Toast.makeText(this, "Error: Empty movie items list", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if list is empty
            return;
        }
        if (imageResource != null) {

        } else {
            Toast.makeText(this, "Movie Screen Img Not Found", Toast.LENGTH_SHORT).show();
        }

        Log.d("MoviePlayerActivity", "Current Time Passed: " + currentTime);
 Initialize the YouTubePlayerView
*/

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.setEnableAutomaticInitialization(false);

        View controlsUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_controls);


        YouTubePlayerListener youTubePlayerListener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                controller = new PlayerUiController(controlsUi, youTubePlayer, youTubePlayerView, videoId, currentTime);

                youTubePlayer.addListener(controller);

/*
                // Use currentTime when loading or cueing the video
                if (currentTime > 0) {
                    // If there's a specific currentTime, start from there
                    youTubePlayer.loadVideo(videoId, currentTime);
                } else {
                    // Otherwise, start from the beginning
                    YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), videoId, 0F);
                }
*/
                if (currentTime > 0) {
                    youTubePlayer.loadVideo(videoId, currentTime);
                } else {
                    youTubePlayer.loadVideo(videoId, 0F);
                }

            }
        };

        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).fullscreen(0).build();
        youTubePlayerView.initialize(youTubePlayerListener, options);



        favIv.setOnClickListener(v -> {
            if (isFavourite) {
                favIv.setColorFilter(ContextCompat.getColor(this, DEFAULT_TINT_COLOR), PorterDuff.Mode.SRC_IN);
                Toast.makeText(this, "Removed from Favourite", Toast.LENGTH_SHORT).show();
                isFavourite = false;
            } else {
                favIv.setColorFilter(ContextCompat.getColor(this, SELECTED_TINT_COLOR), PorterDuff.Mode.SRC_IN);
                Toast.makeText(this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                isFavourite = true;
            }
        });

        shareIv.setOnClickListener(v -> {
            shareIv.setColorFilter(ContextCompat.getColor(this, SELECTED_TINT_COLOR), PorterDuff.Mode.SRC_IN);
            Toast.makeText(this, "Starting to share", Toast.LENGTH_SHORT).show();

            String videoUrl = "https://www.youtube.com/watch?v=" + videoId; // Use the video ID passed from MainActivity

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this video: " + videoUrl);
            startActivity(Intent.createChooser(shareIntent, "Share via"));

            shareIv.postDelayed(() -> shareIv.setColorFilter(ContextCompat.getColor(this, DEFAULT_TINT_COLOR), PorterDuff.Mode.SRC_IN), 1000);
        });

        downloadIv.setOnClickListener(v -> {
            if (isDownload) {
                downloadIv.setColorFilter(ContextCompat.getColor(this, DEFAULT_TINT_COLOR), PorterDuff.Mode.SRC_IN);
                Toast.makeText(this, "Removed from Downloads", Toast.LENGTH_SHORT).show();
                isDownload = false;
            } else {
                downloadIv.setColorFilter(ContextCompat.getColor(this, SELECTED_TINT_COLOR), PorterDuff.Mode.SRC_IN);
                Toast.makeText(this, "Added to Download", Toast.LENGTH_SHORT).show();
                isDownload = true;
            }
        });

        posterIv.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, FullScreenImageActivity.class);
            intent1.putExtra("imageResource", imageResource); // Assuming this gets the image resource ID
            startActivity(intent1);
        });

        trailersFragment = TrailersFragment.newInstance(trailers, title, runtime);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.framelayout, trailersFragment);
        fragmentTransaction.commit();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Objects.requireNonNull(tabLayout.getTabAt(tab.getPosition()));

                String tag = "android:switcher:" + R.id.framelayout + ":" + position;
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    switch (position) {
                        case 0:
                            fragment = trailersFragment;
                            break;
                        case 1:
                            fragment = AboutFragment.newInstanceWithMoviesAndCrew(movieItemList, crew, movieImages, year, country);
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment, tag).commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(0);

    }


    private String extractYouTubeVideoId(String url) {
        String videoId = null;
        Uri uri = Uri.parse(url);

        if (uri.getHost() != null && (uri.getHost().equals("youtu.be"))) {
            videoId = uri.getPathSegments().get(0);
        } else if (uri.getQueryParameter("v") != null) {
            videoId = uri.getQueryParameter("v");
        }

        return videoId;
    }

    private String formatRuntime(int runtimeMinutes) {
        int hours = runtimeMinutes / 60;
        int minutes = runtimeMinutes % 60;
        return String.format("%d h %d min", hours, minutes);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Handle the configuration change
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Ensure YouTubePlayerView is in fullscreen mode
            controller.exitFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Ensure YouTubePlayerView exits fullscreen mode
            controller.enterFullScreen();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save data to ViewModel
        viewModel.setVideoId(videoId);
        viewModel.setCurrentTime(currentTime);
        viewModel.setImageResource(imageResource);
        viewModel.setRating(rating);
        viewModel.setTitle(title);
        viewModel.setPlot(plot);
        viewModel.setYear(year);
        viewModel.setCountry(country);
        viewModel.setRuntime(runtime);
        viewModel.setGenres(genres);
        viewModel.setTrailers(trailers);
        viewModel.setCrew(crew);
        viewModel.setUrl(url);
        viewModel.setMovieImages(movieImages);
        viewModel.setMovieItemList(movieItemList);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        // Pass the current values back to the previous activity
        intent.putExtra("imageResource", imageResource);
        intent.putExtra("rating", rating);
        intent.putExtra("title", title);
        intent.putExtra("plot", plot);
        intent.putExtra("year", year);
        intent.putExtra("country", country);
        intent.putExtra("runtime", runtime);
        intent.putStringArrayListExtra("genres", genres);
        intent.putStringArrayListExtra("trailers", trailers);
        intent.putParcelableArrayListExtra("crew", crew);
        intent.putExtra("url", url);
        intent.putStringArrayListExtra("images", movieImages);
        intent.putParcelableArrayListExtra("popularMovieItemsList", movieItemList);
//        intent.putExtra("videoId", videoId);
//        intent.putExtra("currentTime", playerTracker != null ? playerTracker.getCurrentSecond() : 0f);

        setResult(RESULT_OK, intent);
        // Call the super method to handle the back press
        super.onBackPressed();
        // Close the activity
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }
}