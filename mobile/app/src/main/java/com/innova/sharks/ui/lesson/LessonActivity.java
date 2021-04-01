package com.innova.sharks.ui.lesson;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ActivityLessonBinding;
import com.innova.sharks.models.Lesson;

import static com.innova.sharks.utils.Constants.LESSON_INTENT_LABEL;
import static com.innova.sharks.utils.Constants.USER_AGENT;

public class LessonActivity extends AppCompatActivity {
    private static final String TAG = "LessonActivity";
    private ActivityLessonBinding mBinding;
    private SimpleExoPlayer mPlayer;
    private long mPlayerCurrentPosition = 0;
    private Lesson mLesson;
    private boolean fullscreen = false;
    private ImageView mFullscreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLessonBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mLesson = getIntent().getParcelableExtra(LESSON_INTENT_LABEL);
        mFullscreenButton = findViewById(R.id.exo_fullscreen_icon);

        mFullscreenButton.setOnClickListener(view -> hideSystemUiFullScreen());
        mBinding.note.setText(mLesson.getNote());
    }

    private void initializePlayer() {
        if (mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl()
            );
            mBinding.videoView.setPlayer(mPlayer);
            mPlayer.setPlayWhenReady(true);
            mPlayer.seekTo(mPlayerCurrentPosition);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse(mLesson.getVideo()));
        mPlayer.prepare(mediaSource, true, false);

        mPlayer.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);
                switch (playbackState) {
                    case Player.STATE_READY:
                        mBinding.progressBar.setVisibility(View.GONE);
                        break;
                    case Player.STATE_BUFFERING:
                    case Player.STATE_IDLE:
                        mBinding.progressBar.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(USER_AGENT)).createMediaSource(uri);
        } else if (uri.getLastPathSegment().contains("m3u8")) {
            return new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory(USER_AGENT)).createMediaSource(uri);
        } else {
            DefaultDashChunkSource.Factory dashChunkFactory =
                    new DefaultDashChunkSource.Factory(
                            new DefaultHttpDataSourceFactory("ua",
                                    new DefaultBandwidthMeter()));
            return new DashMediaSource.Factory(dashChunkFactory, new DefaultHttpDataSourceFactory(USER_AGENT)).createMediaSource(uri);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayerCurrentPosition = mPlayer.getCurrentPosition();
            int index = mPlayer.getCurrentWindowIndex();
            boolean ready = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void hideSystemUiFullScreen() {
        if (fullscreen) {
            mFullscreenButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.exo_controls_fullscreen_enter));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mBinding.videoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
            mBinding.videoView.setLayoutParams(params);
            mBinding.detailLayout.setVisibility(View.VISIBLE);
            fullscreen = false;
        } else {
            mFullscreenButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.exo_controls_fullscreen_exit));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mBinding.videoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mBinding.videoView.setLayoutParams(params);
            mBinding.detailLayout.setVisibility(View.GONE);
            fullscreen = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }
}