package com.example.streamingappapi.trailers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streamingappapi.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.utils.FadeViewHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class TrailerPlayerRecItemAdapter extends RecyclerView.Adapter<TrailerPlayerRecItemAdapter.ItemViewHolder>{

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private List<String> filteredTrailers;
    private List<String> trailers;
    private List<TrailerItems> filteredTrailerItems;
    private List<TrailerItems> trailerItems;
    private String title, runtime;
    private final boolean isTrailerItems;


    public TrailerPlayerRecItemAdapter(Context context, List<String> filteredTrailers, List<String> trailers, String title, String runtime) {
        TrailerPlayerRecItemAdapter.context = context;
        this.filteredTrailers = filteredTrailers;
        this.trailers = trailers;
        this.title = title;
        this.runtime = runtime;
        this.isTrailerItems = false;
    }

    // Constructor for TrailerItems
    public TrailerPlayerRecItemAdapter(Context context, List<TrailerItems> filteredTrailerItems, List<TrailerItems> trailerItems, boolean isTrailerItems) {
        TrailerPlayerRecItemAdapter.context = context;
        this.filteredTrailerItems = filteredTrailerItems;
        this.trailerItems = trailerItems;
        this.isTrailerItems = isTrailerItems;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (filteredTrailers != null  && position < filteredTrailers.size()) {
            String trailerUrl = filteredTrailers.get(position);
            String videoId = extractVideoId(trailerUrl);
            // Optionally set a default title and timing if only URLs are provided
            String updatedTitle = updateTrailerTitle(title, position);
            holder.trailerTitle.setText(updatedTitle);
            holder.trailerTiming.setText("3 Min");

            loadYouTubeVideo(holder, videoId);

            // Set onClickListener to playIv to start TrailerPlayerActivity
            holder.playIv.setOnClickListener(v -> {
                Intent intent = new Intent(context, TrailerPlayerActivity.class);
                intent.putExtra("url", trailerUrl);
                intent.putExtra("title", holder.trailerTitle.getText().toString());
                intent.putExtra("runtime", holder.trailerTiming.getText().toString());
                intent.putStringArrayListExtra("trailers", new ArrayList<>(trailers)); // Pass the trailers list
                context.startActivity(intent);
            });

        } else if (trailerItems != null && position < trailerItems.size()) {
            if (position < filteredTrailerItems.size()) { // Ensure position is valid
                TrailerItems item = filteredTrailerItems.get(position);
                // Check if the lists have elements and handle accordingly
                List<String> trailerUrls = item.getTrailerUrls();
                List<String> trailerTimings = item.getTrailerTimings();
                String trailerTitles = item.getTrailerTitle();

                // Display each trailer URL and its corresponding timing
                if (trailerUrls != null && trailerTimings != null && position < trailerUrls.size() && position < trailerTimings.size()) {
//            for (int i = 0; i < item.getTrailerUrls().size(); i++) {
//                if (i < trailerUrls.size() && i < trailerTimings.size()) {
//                    String trailerUrl = item.getTrailerUrls().get(i);
//                    String trailerTiming = item.getTrailerTimings().get(i);
                    String trailerUrl = trailerUrls.get(position);
                    String trailerTiming = trailerTimings.get(position);
                    String videoId = extractVideoId(trailerUrl);

                    String updatedTitle = updateTrailerTitle(trailerTitles, position);
                    holder.trailerTitle.setText(updatedTitle);
                    holder.trailerTiming.setText(trailerTiming);

                    // Initialize the YouTubePlayerView
                    loadYouTubeVideo(holder, videoId);

                    holder.playIv.setOnClickListener(v -> {
                        Intent intent = new Intent(context, TrailerPlayerActivity.class);
                        intent.putExtra("videoId", trailerUrl);
                        intent.putExtra("title", holder.trailerTitle.getText().toString());
                        intent.putExtra("runtime", holder.trailerTiming.getText().toString());
                        intent.putParcelableArrayListExtra("trailerItems", new ArrayList<>(trailerItems)); // Pass the trailerItems list
                        context.startActivity(intent);
                    });
                } else {
                    // Handle cases where list sizes do not match
                    holder.trailerTitle.setText("Title not available");
                    holder.trailerTiming.setText("Timing not available");
                }
            }
        } else {
            // Handle case when no trailers are available
            holder.trailerTitle.setText("No trailers available");
            holder.trailerTiming.setText("Duration not available");
        }
    }

    @Override
    public int getItemCount() {
        if (filteredTrailers != null) {
            return filteredTrailers.size();
        } else if (trailerItems != null) {
            return trailerItems.size();
        } else {
            return 0;
        }
    }

    private String updateTrailerTitle(String originalTitle, int position) {
        if (originalTitle == null) {
            originalTitle = "";
        }

        // Remove any existing "Trailer X" from the title
        String cleanedTitle = originalTitle.replaceAll("Trailer \\d+", "").trim();

        // Add the correct "Trailer X" based on the position
        return cleanedTitle + " Trailer " + (position + 1);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
//        ImageView trailerImg;
        ImageView playIv;
        YouTubePlayerView trailersView;
        TextView trailerTitle, trailerTiming;
        LinearLayout itemll;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            trailerImg = itemView.findViewById(R.id.trailerImg);
            playIv = itemView.findViewById(R.id.playIv);
            trailersView = itemView.findViewById(R.id.youtubeplayer);
            trailerTitle = itemView.findViewById(R.id.trailerTitle_tv);
            trailerTiming = itemView.findViewById(R.id.trailerTiming_tv);
            itemll=itemView.findViewById(R.id.itemll);
        }
    }

    private String extractVideoId(String url) {
        // Assuming the URL is of the form "https://www.youtube.com/watch?v=VIDEO_ID"
        String[] parts = url.split("v=");
        if (parts.length > 1) {
            return parts[1].split("&")[0]; // Remove any additional parameters
        }
        return url; // In case the URL is just the video ID
    }

    private void loadYouTubeVideo(ItemViewHolder holder, String videoId) {
        // Configure IFramePlayerOptions to hide controls and video title
        IFramePlayerOptions options = new IFramePlayerOptions.Builder()
                .controls(0) // Hide controls
                .fullscreen(0)
                .ivLoadPolicy(3) // Hide video annotations
                .modestBranding(1) // Hide YouTube logo
                .rel(0) // Disable related videos at the end
                .build();

        holder.trailersView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0); // Load video but don't start playing
            }
        }, options);

        holder.playIv.setOnClickListener(v -> holder.trailersView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.loadVideo(videoId, 0)));

        // Optional: Handle fade in/out visibility of the video player view
        FadeViewHelper fadeViewHelper = new FadeViewHelper(holder.trailersView);
        fadeViewHelper.setAnimationDuration(FadeViewHelper.DEFAULT_ANIMATION_DURATION);
        fadeViewHelper.setFadeOutDelay(FadeViewHelper.DEFAULT_FADE_OUT_DELAY);

        holder.itemll.setOnClickListener(v -> fadeViewHelper.toggleVisibility());
        holder.trailersView.setOnClickListener(v -> {
            fadeViewHelper.toggleVisibility();
            resetHideControlsTimer();
        });
    }

    @SuppressLint("DefaultLocale")
    private String formatRuntime(int runtimeMinutes) {
        int hours = runtimeMinutes / 60;
        int minutes = runtimeMinutes % 60;
        return String.format("%d h %d min", hours, minutes);
    }

    private void resetHideControlsTimer() {
        // Implement this to handle visibility toggling or reset hide controls timer
    }

}
