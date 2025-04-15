package com.example.streamingappapi.tv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.streamingappapi.R;

import java.util.ArrayList;
import java.util.List;


public class TvFragment extends Fragment  implements TvProgramTimingRecItemAdapter.OnTimingSelectedListener{
    ConstraintLayout videoCl;
    TextView liveTv, playerTimingTv;
    ImageView minmaxScreenIv, shareIv, settingsIv, fastBackwardIv, playIv, fastForwardIv, fullScreenIv;
    SeekBar playerSBar;
    VideoView videoView;
    FrameLayout tvFrameLayout;
    private Handler handler = new Handler();
    private Runnable updateSeekBarRunnable;
    private Runnable hideControlsRunnable;
    private TvProgramRecItemAdapter tvProgramRecItemAdapter;
    private static final int REQUEST_CODE_TV_LANDSCAPE = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv, container, false);

        videoCl = view.findViewById(R.id.videoCl);
        liveTv = view.findViewById(R.id.liveTv);
        playerTimingTv = view.findViewById(R.id.playerTimingTv);
        minmaxScreenIv = view.findViewById(R.id.minScreenIv);
        shareIv = view.findViewById(R.id.shareIv);
        settingsIv = view.findViewById(R.id.settingsIv);
        fastBackwardIv = view.findViewById(R.id.backwardIv);
        playIv = view.findViewById(R.id.playIv);
        fastForwardIv = view.findViewById(R.id.forwardIv);
        fullScreenIv = view.findViewById(R.id.fullScreenIv);
        playerSBar = view.findViewById(R.id.playerSBar);
        videoView = view.findViewById(R.id.videoView);
        tvFrameLayout = view.findViewById(R.id.tvFrameLayout);


//        MediaController mediaController = new MediaController(getActivity());
//        mediaController.setAnchorView(videoView);
//        videoView.setMediaController(mediaController);

        hideControls();

        videoView.setVideoURI(Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.videohz));
        videoView.start();

        videoView.setOnPreparedListener(mp -> {
            playerSBar.setMax(videoView.getDuration());
            updateSeekBar();
        });

        videoView.setOnCompletionListener(mp -> playIv.setImageResource(android.R.drawable.ic_media_play));

        shareIv.setOnClickListener(v -> {
            Uri videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.videohz);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("video/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
            startActivity(Intent.createChooser(shareIntent, "Share Video"));
        });

        settingsIv.setOnClickListener(v -> openSettingsDialog());

        playerSBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
                updatePlayerTiming();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        playIv.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                liveTv.setText("To the Live");
                liveTv.setBackgroundResource(R.drawable.lgtransparentwhitestroke_bg);
                playIv.setImageResource(android.R.drawable.ic_media_play);
                handler.postDelayed(hideControlsRunnable, 5000);
            } else {
                videoView.start();
                playIv.setImageResource(android.R.drawable.ic_media_pause);
                liveTv.setText("Live");
                liveTv.setBackgroundResource(R.drawable.lgblackcircle_bg);
            }
        });

        liveTv.setOnClickListener(v -> {
            if ("To the Live".equals(liveTv.getText().toString())) {
                videoView.start();
                playIv.setImageResource(android.R.drawable.ic_media_pause);
                liveTv.setText("Live");
                liveTv.setBackgroundResource(R.drawable.lgblackcircle_bg);
                showControls();
                handler.postDelayed(hideControlsRunnable, 10000);
            }
        });

        fastBackwardIv.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            videoView.seekTo(Math.max(currentPosition - 10000, 0));
        });

        fastForwardIv.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            videoView.seekTo(Math.min(currentPosition + 10000, videoView.getDuration()));
        });

        videoCl.setOnClickListener(v -> {
            if (liveTv.getVisibility() == View.VISIBLE) {
                hideControls();
                handler.removeCallbacks(hideControlsRunnable);
            } else {
                showControls();
                handler.removeCallbacks(hideControlsRunnable);
                handler.postDelayed(hideControlsRunnable, 10000);
            }
        });

        hideControlsRunnable = this::hideControls;

        initTvSelectionFragment();

        fullScreenIv.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TvLandscapeActivity.class);
            Uri videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.videohz);
            intent.putExtra("VIDEO_URI", videoUri.toString()); // Pass the URI as a string
            intent.putExtra("CURRENT_POSITION", videoView.getCurrentPosition()); // Pass the current playback position
            startActivityForResult(intent, REQUEST_CODE_TV_LANDSCAPE);
        });

        return view;
    }

    private void updateSeekBar() {
        playerSBar.setProgress(videoView.getCurrentPosition());
        updatePlayerTiming();
        handler.postDelayed(updateSeekBarRunnable = this::updateSeekBar, 1000);
    }

    private void updatePlayerTiming() {
        int currentPos = videoView.getCurrentPosition();
        playerTimingTv.setText(String.format("%02d:%02d:%02d",
                (currentPos / 1000) / 3600, ((currentPos / 1000) % 3600) / 60, (currentPos / 1000) % 60));
    }

    private void showControls() {
        liveTv.setVisibility(View.VISIBLE);
        playerTimingTv.setVisibility(View.VISIBLE);
        minmaxScreenIv.setVisibility(View.VISIBLE);
        shareIv.setVisibility(View.VISIBLE);
        settingsIv.setVisibility(View.VISIBLE);
        fastBackwardIv.setVisibility(View.VISIBLE);
        playIv.setVisibility(View.VISIBLE);
        fastForwardIv.setVisibility(View.VISIBLE);
        fullScreenIv.setVisibility(View.VISIBLE);
        playerSBar.setVisibility(View.VISIBLE);
    }

    private void hideControls() {
        liveTv.setVisibility(View.GONE);
        playerTimingTv.setVisibility(View.GONE);
        minmaxScreenIv.setVisibility(View.GONE);
        shareIv.setVisibility(View.GONE);
        settingsIv.setVisibility(View.GONE);
        fastBackwardIv.setVisibility(View.GONE);
        playIv.setVisibility(View.GONE);
        fastForwardIv.setVisibility(View.GONE);
        fullScreenIv.setVisibility(View.GONE);
        playerSBar.setVisibility(View.GONE);
    }

    private void initTvSelectionFragment() {
        TvSelectionFragment tvSelectionFragment = new TvSelectionFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.tvFrameLayout, tvSelectionFragment);
        transaction.commit();
        Log.d("TvFragment", "TvSelectionFragment transaction committed");
    }


    @Override
    public void onTimingSelected(int position) {
        // Initialize tvProgramRecItemAdapter if not already initialized
        if (tvProgramRecItemAdapter == null) {
            tvProgramRecItemAdapter = new TvProgramRecItemAdapter(getActivity(), getProgramsForTiming(position));
            // Set adapter to RecyclerView or initialize it as needed
        } else {
            List<TvProgramItems> updatedProgramItems = getProgramsForTiming(position);
            tvProgramRecItemAdapter.updateProgramList(updatedProgramItems);
        }
    }

    private List<TvProgramItems> getProgramsForTiming(int position) {
        // Implement this method to return the list of programs for the selected timing
        return new ArrayList<>(); // Placeholder implementation
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateSeekBarRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSeekBar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TV_LANDSCAPE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Retrieve the video URI and playback position from the result
                String videoUriString = data.getStringExtra("VIDEO_URI");
                int currentPosition = data.getIntExtra("CURRENT_POSITION", 0);

                if (videoUriString != null) {
                    Uri videoUri = Uri.parse(videoUriString);
                    videoView.setVideoURI(videoUri);
                    videoView.seekTo(currentPosition);
                    videoView.start();
                    playIv.setImageResource(android.R.drawable.ic_media_pause);
                    liveTv.setText("Live");
                    liveTv.setBackgroundResource(R.drawable.lgblackcircle_bg);
                }
            }
        }
    }


    private void openSettingsDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quality);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ConstraintLayout constraintLayout = dialog.findViewById(R.id.constraint);
        TextView qualityVal = dialog.findViewById(R.id.qualityVal);
        SeekBar qualitySbar = dialog.findViewById(R.id.qualitySeekbar);

        // Configure SeekBar for quality selection
        qualitySbar.setMax(100);
        qualitySbar.setProgress(100); // Default to 25%

        qualitySbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


}