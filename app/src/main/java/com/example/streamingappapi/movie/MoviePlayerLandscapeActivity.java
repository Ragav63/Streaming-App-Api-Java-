package com.example.streamingappapi.movie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.streamingappapi.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MoviePlayerLandscapeActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private String videoId;
    private float currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_player_landscape);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");
        currentTime = intent.getFloatExtra("currentTime", 0f);

        youTubePlayerView = findViewById(R.id.youtubeplayer);

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.setEnableAutomaticInitialization(false);

        View controlsUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_controls);

        YouTubePlayerListener youTubePlayerListener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                MovieLandscapePlayerUiController controller = new MovieLandscapePlayerUiController(controlsUi, youTubePlayer, youTubePlayerView, videoId, currentTime);

                youTubePlayer.addListener(controller);


                if (currentTime > 0) {
                    youTubePlayer.loadVideo(videoId, currentTime);
                } else {
                    youTubePlayer.loadVideo(videoId, 0F);
                }

            }
        };

        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).fullscreen(0).build();
        youTubePlayerView.initialize(youTubePlayerListener, options);

    }


}