package com.example.streamingappapi.trailers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streamingappapi.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class TrailerPlayerActivity extends AppCompatActivity {

    ConstraintLayout videoCl;
    YouTubePlayerView youTubePlayerView;
    ImageView favIv, shareIv, downloadIv;
    TextView  titleTv, durationTv;
    private boolean isFavourite = false;
    private boolean isDownload = false;
    private RecyclerView recVTrailers;
    private TrailerPlayerUiController controller;
    private List<String> trailers;
    private List<TrailerItems> trailerItems;
    TrailerPlayerRecItemAdapter adapter;
    String url, title, duration, videoIdfromIntent, videoId;
    private float currentTime;
    private static final int DEFAULT_TINT_COLOR = R.color.white;
    private static final int SELECTED_TINT_COLOR = R.color.bluemain;
    private TrailerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(TrailerViewModel.class);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trailer_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        videoCl = findViewById(R.id.videoCl);
        youTubePlayerView = findViewById(R.id.youtubeplayer);
        titleTv = findViewById(R.id.titleTv);
        favIv = findViewById(R.id.favIv);
        shareIv = findViewById(R.id.shareIv);
        downloadIv = findViewById(R.id.downloadIv);
        durationTv = findViewById(R.id.durationTv);
        recVTrailers = findViewById(R.id.recVTrailers);



        // Get the data from the Intent
        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null) {
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            duration = intent.getStringExtra("runtime");
            currentTime = intent.getFloatExtra("currentTime", 0f);
            videoIdfromIntent = intent.getStringExtra("videoId");
            trailers = intent.getStringArrayListExtra("trailers");
            trailerItems = intent.getParcelableArrayListExtra("trailerItems");

            titleTv.setText(title);
            durationTv.setText("PG-13 - " + duration);

            if (videoIdfromIntent != null) {
                videoId = videoIdfromIntent;
            } else {
                videoId = extractYouTubeVideoId(url);
            }
        } else {

            // Restore data from ViewModel
            title = viewModel.getTitle().getValue();
            trailers = viewModel.getTrailers().getValue();
            trailerItems = viewModel.getTrailerItems().getValue();
            url = viewModel.getUrl().getValue();
            videoId = viewModel.getVideoId().getValue();
            currentTime = viewModel.getCurrentTime().getValue() != null ? viewModel.getCurrentTime().getValue() : 0F;
            if (viewModel.getTrailers().getValue() != null) {
                trailers = viewModel.getTrailers().getValue();
            } else {
                trailerItems = viewModel.getTrailerItems().getValue();
            }
            titleTv.setText(title);
            durationTv.setText("PG-13 - " + duration);
        }

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.setEnableAutomaticInitialization(false);

        View controlsUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_controls);


        YouTubePlayerListener youTubePlayerListener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                controller = new TrailerPlayerUiController(controlsUi, youTubePlayer, youTubePlayerView, videoId, currentTime);

                youTubePlayer.addListener(controller);

                if (videoId != null) {
                    if (currentTime > 0) {
                        youTubePlayer.loadVideo(videoId, currentTime);
                    } else {
                        youTubePlayer.loadVideo(videoId, 0F);
                    }
                } else {
                    // Handle the case where videoId is null
                    Toast.makeText(TrailerPlayerActivity.this, "Invalid video ID", Toast.LENGTH_SHORT).show();
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

        recVTrailers.setLayoutManager(new LinearLayoutManager(this));

        if (trailers != null && !trailers.isEmpty()) {
            // Filter the trailers list to exclude the current video ID
            List<String> filteredTrailers = new ArrayList<>();
            for (String trailer : trailers) {
                String trailerVideoId = extractYouTubeVideoId(trailer);
                if (!trailerVideoId.equals(videoId)) {
                    filteredTrailers.add(trailer);
                }
            }
            adapter = new TrailerPlayerRecItemAdapter(this, filteredTrailers, trailers, title, duration);
        } else if (trailerItems != null && !trailerItems.isEmpty()) {
            // Filter the trailerItems list to exclude the current video ID
            List<TrailerItems> filteredTrailerItems = new ArrayList<>();
            for (TrailerItems item : trailerItems) {
                boolean shouldAdd = true;

                // Iterate over each URL in the list of trailer URLs
                for (String trailerUrl : item.getTrailerUrls()) {
                    String itemVideoId = extractYouTubeVideoId(trailerUrl);

                    // If any of the video IDs match, set shouldAdd to false and break out of the loop
                    if (itemVideoId.equals(videoId)) {
                        shouldAdd = false;
                        break;
                    }
                }

                // Add the item to the filtered list only if none of the video IDs matched
                if (shouldAdd) {
                    filteredTrailerItems.add(item);
                }
            }
            adapter = new TrailerPlayerRecItemAdapter(this, filteredTrailerItems, trailerItems, true);
        } else {
            adapter = null;
        }

        if (adapter != null) {
            recVTrailers.setAdapter(adapter);
        }

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
        if (viewModel != null) {
            // Save data to ViewModel
            if (videoId != null) {
                viewModel.setVideoId(videoId);
            }
            viewModel.setCurrentTime(currentTime);
            viewModel.setTitle(title);
            if (trailers != null) {
                viewModel.setTrailers((ArrayList<String>) trailers);
            } else {
                viewModel.setTrailerItems((ArrayList<TrailerItems>) trailerItems);
            }
            viewModel.setUrl(url);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        // Pass the current values back to the previous activity
        intent.putExtra("title", title);
        intent.putExtra("duration", duration);
        intent.putStringArrayListExtra("trailers", (ArrayList<String>) trailers);
        intent.putParcelableArrayListExtra("trailerItems", (ArrayList<? extends Parcelable>) trailerItems);
        intent.putExtra("url", url);

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