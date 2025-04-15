package com.example.streamingappapi.trailers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.streamingappapi.AdItem;
import com.example.streamingappapi.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.utils.FadeViewHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBarListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrailerPlayerUiController extends AbstractYouTubePlayerListener {
    private final YouTubePlayerTracker playerTracker;
    private final YouTubePlayer youTubePlayer;
    private final YouTubePlayerView youTubePlayerView;
    private final String videoId;
    private boolean isfullScreen = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable hideControlsRunnable;
    private final long AUTO_HIDE_DELAY = 10000; // 10 seconds
    RelativeLayout container, relativeLayout, fastForwardRl, fastBackwardRl;
    YouTubePlayerSeekBar seekBar;
    ImageButton pausePlay, fullScreen, settings, share, minScreen;
    ImageView fastForward, fastBackward;
    private float currentTime;
/*
    private List<AdItem> adItems = new ArrayList<>();
    private int currentAdIndex = 0;  // Index to track which ad is currently playing
    private boolean isAdPlaying = false;
    private long adInterval = 60000; // 1 minute interval
    private Handler adHandler = new Handler(Looper.getMainLooper());
*/

    public TrailerPlayerUiController(View controlsUi, YouTubePlayer youTubePlayer, YouTubePlayerView youTubePlayerView, String videoId, float currentTime) {
        this.youTubePlayer = youTubePlayer;
        this.youTubePlayerView = youTubePlayerView;
        this.videoId = videoId;
        this.currentTime = currentTime;
        playerTracker = new YouTubePlayerTracker();
        youTubePlayer.addListener(playerTracker);

        youTubePlayer.addListener(this);

        initViews(controlsUi);

//        fetchAdData();
    }



    private void initViews(View view) {
        container = view.findViewById(R.id.container);
        relativeLayout = view.findViewById(R.id.root);
        seekBar = view.findViewById(R.id.playerSbar);
        pausePlay = view.findViewById(R.id.pausePlay);
        fullScreen = view.findViewById(R.id.toggleFullScreen);
        fastForward = view.findViewById(R.id.forwardIv);
        fastForwardRl = view.findViewById(R.id.fastForwardRl);
        fastBackward = view.findViewById(R.id.backwardIv);
        fastBackwardRl = view.findViewById(R.id.fastBackwardRl);
        settings = view.findViewById(R.id.settings);
        share = view.findViewById(R.id.share);
        minScreen = view.findViewById(R.id.minScreen);

        TextView titleContainer = view.findViewById(R.id.titleContainer);
        ImageButton channelPictureContainer = view.findViewById(R.id.channelPictureContainer);

        titleContainer.setVisibility(View.GONE);
        channelPictureContainer.setVisibility(View.GONE);

        youTubePlayer.addListener(seekBar);

        seekBar.setYoutubePlayerSeekBarListener(new YouTubePlayerSeekBarListener() {
            @Override
            public void seekTo(float v) {
                youTubePlayer.seekTo(v);
            }
        });

        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerTracker.getState() == PlayerConstants.PlayerState.PLAYING) {
                    pausePlay.setImageResource(android.R.drawable.ic_media_play);
                    youTubePlayer.pause();
                } else {
                    pausePlay.setImageResource(android.R.drawable.ic_media_pause);
                    youTubePlayer.play();
                }
            }
        });

        fastForward.setOnClickListener(v -> fastForward());

        fastBackward.setOnClickListener(v -> fastBackward());

        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                Activity activity = (Activity) youTubePlayerView.getContext();
                currentTime = playerTracker.getCurrentSecond(); // Get the current time of the video

                Intent intent;

                // If the current orientation is portrait, switch to landscape activity
                Log.d("PlayerUiController", "Switching to activity: TrailerPlayerLandscapeActivity");
                intent = new Intent(activity, TrailerPlayerLandscapeActivity.class);
                intent.putExtra("videoId", videoId);
                intent.putExtra("currentTime", currentTime);

                activity.startActivity(intent);
                activity.finish(); // Finish the current activity
*/
                if (isfullScreen) {
                    exitFullScreen();
//                    youTubePlayerView.wrapContent();
                } else {
                    enterFullScreen();
//                    youTubePlayerView.matchParent();
                }
                isfullScreen = !isfullScreen;
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsDialog();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCurrentVideo();
            }
        });

        FrameLayout commonParent = new FrameLayout(youTubePlayerView.getContext());
        RelativeLayout.LayoutParams commonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        commonParent.setLayoutParams(commonParams);
        relativeLayout.addView(commonParent);

        if (container.getParent() != null) {
            ((ViewGroup) container.getParent()).removeView(container);
        }

        commonParent.addView(container);

        View overlayView = new View(youTubePlayerView.getContext());
        overlayView.setBackgroundColor(Color.TRANSPARENT); // Set the overlay color to transparent
        FrameLayout.LayoutParams overlayParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        overlayParams.gravity = Gravity.TOP;
        overlayView.setLayoutParams(overlayParams);
        commonParent.addView(overlayView);

        FadeViewHelper fadeViewHelper = new FadeViewHelper(commonParent);
        fadeViewHelper.setAnimationDuration(FadeViewHelper.DEFAULT_ANIMATION_DURATION);
        fadeViewHelper.setFadeOutDelay(FadeViewHelper.DEFAULT_FADE_OUT_DELAY);
        youTubePlayer.addListener(fadeViewHelper);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeViewHelper.toggleVisibility();
            }
        });

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeViewHelper.toggleVisibility();
                resetHideControlsTimer();
            }
        });

        startHideControlsTimer();

    }

    public void enterFullScreen() {
        // Adjust the layout parameters to fill the screen
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        youTubePlayerView.setLayoutParams(params);

        // Hide system UI for fullscreen
        View decorView = ((Activity) youTubePlayerView.getContext()).getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

    }

    public void exitFullScreen() {
        // Restore the layout parameters to wrap content
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        youTubePlayerView.setLayoutParams(params);

        // Show system UI when exiting fullscreen
        View decorView = ((Activity) youTubePlayerView.getContext()).getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

    }

    private void startHideControlsTimer() {
        if (hideControlsRunnable != null) {
            handler.removeCallbacks(hideControlsRunnable);
        }
        hideControlsRunnable = new Runnable() {
            @Override
            public void run() {
                hideControls();
            }
        };
        handler.postDelayed(hideControlsRunnable, AUTO_HIDE_DELAY);
    }

    private void resetHideControlsTimer() {
        startHideControlsTimer();
        showControls();
    }

    private void showControls() {
        pausePlay.setVisibility(View.VISIBLE);
        fastForward.setVisibility(View.VISIBLE);
        fastBackward.setVisibility(View.VISIBLE);
        fastForwardRl.setVisibility(View.VISIBLE);
        fastBackwardRl.setVisibility(View.VISIBLE);
        fullScreen.setVisibility(View.VISIBLE);
        settings.setVisibility(View.VISIBLE);
        share.setVisibility(View.VISIBLE);
        minScreen.setVisibility(View.VISIBLE);
    }

    private void hideControls() {
        pausePlay.setVisibility(View.GONE);
        fastForward.setVisibility(View.GONE);
        fastBackward.setVisibility(View.GONE);
        fastForwardRl.setVisibility(View.GONE);
        fastBackwardRl.setVisibility(View.GONE);
        fullScreen.setVisibility(View.GONE);
        settings.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
        minScreen.setVisibility(View.GONE);
    }


    @Override
    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        super.onReady(youTubePlayer);
        Log.d("PlayerUiController", "YouTube Player is ready.");

        if (currentTime == 0) {
            // If currentTime is 0 (which you might use as null equivalent), start from the beginning
            youTubePlayer.loadVideo(videoId, 0);
        } else {
            // Otherwise, start from the provided currentTime
            youTubePlayer.loadVideo(videoId, currentTime);
        }
    }



    private void fastForward() {
        float currentTime = playerTracker.getCurrentSecond();
        float newTime = currentTime + 5;
        youTubePlayer.seekTo(newTime);
    }

    private void fastBackward() {
        float currentTime = playerTracker.getCurrentSecond();
        float newTime = Math.max(currentTime - 5, 0);
        youTubePlayer.seekTo(newTime);
    }

    private void shareCurrentVideo() {
        String videoUrl = "https://www.youtube.com/watch?v=" + videoId; // Use the video ID passed from MainActivity

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this video: " + videoUrl);
        // Use the context from YouTubePlayerView to start the activity
        youTubePlayerView.getContext().startActivity(Intent.createChooser(shareIntent, "Share Video"));
    }

/*
    private void fetchAdData() {
        String url = "https://mocki.io/v1/0665d347-37ba-457d-b5dd-da5a1ba30f2a";

        RequestQueue requestQueue = Volley.newRequestQueue(youTubePlayerView.getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        adItems.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject jsonObject = response.getJSONObject(i);

                                int id =  jsonObject.getInt("id");
                                String title = jsonObject.getString("title");
                                String video = jsonObject.getString("video");
                                double runtime = jsonObject.getDouble("runtime");
                                String website = jsonObject.getString("website");

                                AdItem adItem = new AdItem(id, title, video, runtime, website);
                                adItems.add(adItem);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            // Handle the ad items (e.g., play them or display their information)
//                            handleAdItems(adItems);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
*/

    @SuppressLint("ClickableViewAccessibility")
    private void openSettingsDialog() {

        final Dialog dialog = new Dialog(youTubePlayerView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quality);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ConstraintLayout constraintLayout = dialog.findViewById(R.id.constraint);
        TextView qualityVal = dialog.findViewById(R.id.qualityVal);
        SeekBar qualitySbar = dialog.findViewById(R.id.qualitySeekbar);

        qualitySbar.setMax(100);
        qualitySbar.setProgress(25); // Default to 25%

        qualitySbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 25) {
                    qualityVal.setText("Low (360p)");
                } else if (progress < 50) {
                    qualityVal.setText("Medium (480p)");
                } else if (progress < 75) {
                    qualityVal.setText("High (720p)");
                } else {
                    qualityVal.setText("HD (1080p)");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Add GestureDetector to detect swipe down
        GestureDetector gestureDetector = new GestureDetector(dialog.getContext(), new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        dialog.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });

        constraintLayout.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}
