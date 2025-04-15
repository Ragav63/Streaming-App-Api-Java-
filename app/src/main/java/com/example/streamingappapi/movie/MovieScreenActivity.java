package com.example.streamingappapi.movie;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.streamingappapi.AboutFragment;
import com.example.streamingappapi.CastItems;
import com.example.streamingappapi.MoreLikeThisFragment;
import com.example.streamingappapi.R;
import com.example.streamingappapi.trailers.TrailersFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MovieScreenActivity extends AppCompatActivity {
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment=null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TrailersFragment trailersFragment = new TrailersFragment();
    ImageView backIv, movieScreenIv, downloadIv, favIv, shareIv;
    TextView ratingTv, titleTv, watchNowTv, yearTv, genreTv, originTv, durationTv, plotTv;
//    private List<PopularMovieItems> popularMovieItemsList;
    private boolean isDownloaded = false;
    private boolean isFavourite = false;
    String imageResource;
    private ArrayList<String> genres;
    private ArrayList<String> trailers;
    private ArrayList<CastItems> crew;
    private ArrayList<MovieItem> movieItemList;
    private String url;
    private ArrayList<String> movieImages;
    String rating, title, plot, year, country, runtime;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backIv = findViewById(R.id.backIv);
        movieScreenIv = findViewById(R.id.movieScreenIv);
        ratingTv = findViewById(R.id.ratingTv);
        titleTv = findViewById(R.id.titleTv);
        genreTv = findViewById(R.id.genreTv);
        yearTv = findViewById(R.id.yearTv);
        originTv = findViewById(R.id.originTv);
        durationTv = findViewById(R.id.durationTv);
        plotTv = findViewById(R.id.plotTv);
        watchNowTv = findViewById(R.id.watchNowTv);
        downloadIv = findViewById(R.id.downloadIv);
        favIv = findViewById(R.id.favIv);
        shareIv = findViewById(R.id.shareIv);
        tabLayout = findViewById(R.id.tab_layout);
        frameLayout = findViewById(R.id.framelayout);

        backIv.setOnClickListener(v -> {
            finish();
        });

        Intent intent = getIntent();

        imageResource = intent.getStringExtra("imageResource");
        rating = String.valueOf(intent.getDoubleExtra("rating", 0.0));
        title = intent.getStringExtra("title");

        plot = intent.getStringExtra("plot");
        year = String.valueOf(intent.getIntExtra("year", 0));
        country = intent.getStringExtra("country");
        runtime = String.valueOf(intent.getIntExtra("runtime", 0));

        genres = intent.getStringArrayListExtra("genres");
        trailers = intent.getStringArrayListExtra("trailers");
        crew = intent.getParcelableArrayListExtra("cast");
        url = intent.getStringExtra("url");
        movieImages = intent.getStringArrayListExtra("images");
        movieItemList = intent.getParcelableArrayListExtra("popularMovieItemsList");

        if (movieItemList == null || movieItemList.isEmpty()) {
            Log.d("MovieScreenActivity", "movieItemList is null or empty");
            Toast.makeText(this, "Error: No movie items found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (imageResource != null) {
            Glide.with(this).load(imageResource).into(movieScreenIv);
        } else {
            Toast.makeText(this, "Movie Screen Img Not Found", Toast.LENGTH_SHORT).show();
        }

        if (rating != null) {
            ratingTv.setText(rating);
        } else {
            Toast.makeText(this, "Rating Not Found", Toast.LENGTH_SHORT).show();
        }

        if (title != null) {
            titleTv.setText(title);
            String genresText = TextUtils.join(", ", genres);
            genreTv.setText(genresText);
            yearTv.setText(year);
            originTv.setText(country);
            durationTv.setText("PG-13 - "+ formatRuntime(Integer.parseInt(runtime)));
            plotTv.setText(plot);
        } else {
            Toast.makeText(this, "Title Not Found", Toast.LENGTH_SHORT).show();
        }

        int defaultTintColor = ContextCompat.getColor(this, R.color.white);
        int selectedTintColor = ContextCompat.getColor(this, R.color.bluemain);

        downloadIv.setOnClickListener(v -> {
            if (isDownloaded) {
                Toast.makeText(this, "Already added to Download", Toast.LENGTH_SHORT).show();
            } else {
                downloadIv.setColorFilter(selectedTintColor, PorterDuff.Mode.SRC_IN);
                Toast.makeText(this, "Added to Download", Toast.LENGTH_SHORT).show();
                openDownloadDialog(movieScreenIv, titleTv.getText().toString());
                isDownloaded = true;
            }
        });

        favIv.setOnClickListener(v -> {
            if (isFavourite) {
                favIv.setColorFilter(defaultTintColor, PorterDuff.Mode.SRC_IN);
                Toast.makeText(this, "Removed from Favourite", Toast.LENGTH_SHORT).show();
                isFavourite = false;
            } else {
                favIv.setColorFilter(selectedTintColor, PorterDuff.Mode.SRC_IN);
                Toast.makeText(this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                isFavourite = true;
            }
        });

        shareIv.setOnClickListener(v -> {
            shareIv.setColorFilter(selectedTintColor, PorterDuff.Mode.SRC_IN);
            Toast.makeText(this, "Starting to share", Toast.LENGTH_SHORT).show();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this movie!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            shareIv.postDelayed(() -> shareIv.setColorFilter(defaultTintColor, PorterDuff.Mode.SRC_IN), 1000);
        });


        watchNowTv.setOnClickListener(v -> {
            Toast.makeText(this, "Added to Download", Toast.LENGTH_SHORT).show();
            Intent watchIntent = new Intent(MovieScreenActivity.this, MoviePlayerActivity.class);
            watchIntent.putExtra("imageResource", imageResource);
            watchIntent.putExtra("rating", Double.parseDouble(ratingTv.getText().toString()));
            watchIntent.putExtra("title", titleTv.getText().toString());
            watchIntent.putExtra("plot", plotTv.getText().toString());
            watchIntent.putExtra("year", Integer.parseInt(yearTv.getText().toString()));
            watchIntent.putExtra("country", originTv.getText().toString());
            watchIntent.putExtra("runtime", Integer.parseInt(durationTv.getText().toString().split(" ")[2]));  // Assuming "PG-13 - x h y min" format
            watchIntent.putStringArrayListExtra("genres", genres);
            watchIntent.putStringArrayListExtra("trailers", trailers);
            watchIntent.putParcelableArrayListExtra("crew", crew);
            watchIntent.putExtra("url", url);
            watchIntent.putStringArrayListExtra("images", movieImages);

            watchIntent.putParcelableArrayListExtra("popularMovieItemsList", new ArrayList<>(movieItemList));

            startActivity(watchIntent);
//            openDownloadDialog(movieScreenIv, titleTv.getText().toString());
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
                            // Exclude the current movie item from the list
                            ArrayList<MovieItem> filteredMovieList = new ArrayList<>();
                            for (MovieItem item : movieItemList) {
                                if (!item.getTitle().trim().equalsIgnoreCase(title.trim())) {
                                    filteredMovieList.add(item);
                                }
                            }
                            fragment = MoreLikeThisFragment.newInstanceWithMovies(filteredMovieList);
                            break;
                        case 2:
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

    private String formatRuntime(int runtimeMinutes) {
        int hours = runtimeMinutes / 60;
        int minutes = runtimeMinutes % 60;
        return String.format("%d h %d min", hours, minutes);
    }

    private MovieItem getCurrentMovieItem() {
        // Example: Return the first item in the list or use intent data
        // Adjust according to how you determine the "current" movie item
        if (movieItemList != null && !movieItemList.isEmpty()) {
            return movieItemList.get(0); // Replace this with the actual current movie logic
        }
        return null;
    }

    private void openDownloadDialog(ImageView movieImageView, String movieTitle) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_download);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView downloadIv = dialog.findViewById(R.id.downloadIv);
        TextView downloadTv = dialog.findViewById(R.id.downloadTv);
        TextView downloadTitle, downloadQualityVal, downloadAudio1, downloadAudio2, downloadSubtitleOff, downloadSubtitle1, downloadSubtitle2;
        SeekBar qualitySbar = dialog.findViewById(R.id.qualitySeekbar);

        downloadTitle = dialog.findViewById(R.id.downloadTitleTv);
        downloadQualityVal = dialog.findViewById(R.id.qualityValTv);
        downloadAudio1 = dialog.findViewById(R.id.audio1Tv);
        downloadAudio2 = dialog.findViewById(R.id.audio2Tv);
        downloadSubtitleOff = dialog.findViewById(R.id.subtitleOffTv);
        downloadSubtitle1 = dialog.findViewById(R.id.subtitle1Tv);
        downloadSubtitle2 = dialog.findViewById(R.id.subtitle2Tv);

        downloadIv.setImageDrawable(movieImageView.getDrawable());
        downloadTitle.setText(movieTitle);

        // Set default audio and subtitle selections
        downloadAudio1.setBackgroundResource(R.drawable.lgtransparentbluestroke_bg);
        downloadSubtitleOff.setBackgroundResource(R.drawable.lgtransparentbluestroke_bg);

        // Configure SeekBar for quality selection
        qualitySbar.setMax(100);
        qualitySbar.setProgress(25); // Default to 25%

        qualitySbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 25) {
                    downloadQualityVal.setText("Low (360p)");
                } else if (progress < 50) {
                    downloadQualityVal.setText("Medium (480p)");
                } else if (progress < 75) {
                    downloadQualityVal.setText("High (720p)");
                } else {
                    downloadQualityVal.setText("HD (1080p)");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Set listeners for audio options
        downloadAudio1.setOnClickListener(v -> {
            downloadAudio1.setBackgroundResource(R.drawable.lgtransparentbluestroke_bg);
            downloadAudio2.setBackgroundResource(R.drawable.dimcircle_bg);
        });

        downloadAudio2.setOnClickListener(v -> {
            downloadAudio2.setBackgroundResource(R.drawable.lgtransparentbluestroke_bg);
            downloadAudio1.setBackgroundResource(R.drawable.dimcircle_bg);
        });

        downloadSubtitleOff.setOnClickListener(v -> {
            downloadSubtitleOff.setBackgroundResource(R.drawable.lgtransparentbluestroke_bg);
            downloadSubtitle1.setBackgroundResource(R.drawable.dimcircle_bg);
            downloadSubtitle2.setBackgroundResource(R.drawable.dimcircle_bg);
        });
        // Set listeners for subtitle options
        downloadSubtitle1.setOnClickListener(v -> {
            downloadSubtitle1.setBackgroundResource(R.drawable.lgtransparentbluestroke_bg);
            downloadSubtitle2.setBackgroundResource(R.drawable.dimcircle_bg);
            downloadSubtitleOff.setBackgroundResource(R.drawable.dimcircle_bg);
        });

        downloadSubtitle2.setOnClickListener(v -> {
            downloadSubtitle2.setBackgroundResource(R.drawable.lgtransparentbluestroke_bg);
            downloadSubtitle1.setBackgroundResource(R.drawable.dimcircle_bg);
            downloadSubtitleOff.setBackgroundResource(R.drawable.dimcircle_bg);
        });


        downloadTv.setOnClickListener(v -> {


            Toast.makeText(this, "Started to Download", Toast.LENGTH_SHORT).show();
            dialog.dismiss();


        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK && data != null) {
            imageResource = data.getStringExtra("imageResource");
            rating = data.getStringExtra("rating");
            title = data.getStringExtra("title");
            plot = data.getStringExtra("plot");
            year = data.getStringExtra("year");
            country = data.getStringExtra("country");
            runtime = data.getStringExtra("runtime");
            url = data.getStringExtra("url");
            genres = data.getStringArrayListExtra("genres");
            trailers = data.getStringArrayListExtra("trailers");
            crew = data.getParcelableArrayListExtra("crew");
            movieImages = data.getStringArrayListExtra("images");
            movieItemList = data.getParcelableArrayListExtra("popularMovieItemsList");
//            String videoId = data.getStringExtra("videoId");
//            float currentTime = data.getFloatExtra("currentTime", 0f);

            // Handle the received data as needed
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("imageResource", imageResource);
        outState.putString("rating", rating);
        outState.putString("title", title);
        outState.putString("plot", plot);
        outState.putString("year", year);
        outState.putString("country", country);
        outState.putString("runtime", runtime);
        outState.putString("url", url);
        outState.putStringArrayList("genres", genres);
        outState.putStringArrayList("trailers", trailers);
        outState.putParcelableArrayList("crew", crew);
        outState.putStringArrayList("images", movieImages);
        outState.putParcelableArrayList("popularMovieItemsList", movieItemList);

        outState.putBoolean("isDownloaded", isDownloaded);
        outState.putBoolean("isFavourite", isFavourite);
    }


}