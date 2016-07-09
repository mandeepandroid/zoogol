package com.mandeep.zoogol.explore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mandeep.zoogol.R;

public class YouTubeDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        playerView = (YouTubePlayerView) findViewById(R.id.player_view);

        // initializes the YouTube player view
        playerView.initialize(Config.API_KEY, this);
    }


    // Called when YouTube Player initialization is failed
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorResult) {

        // shows dialog if user interaction may fix the error
        if (errorResult.isUserRecoverableError()) {
            errorResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            // displays the error occured during the initialization
            String error = String.format(
                    "Error", errorResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Called when initialization of Player is successful
     *
     * @param provider    Provider used to initialize the Player
     * @param player      Player instance used to control the video playback
     * @param wasRestored Depicts whether the video is restored from a previous
     *                    state. Returns true if video is resumed from the last
     *                    paused state, else returns false
     */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            player.cueVideo(YouTubeActivity.itemClicked.getLink());
            player.setPlayerStyle(PlayerStyle.DEFAULT);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {

            // initializes the YouTube player view
            getYouTubePlayerProvider().initialize(Config.API_KEY, this);
        }
    }


    // Returns Player view defined in xml file
    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.player_view);
    }


    /**
     *
     */
    private final class EventListener implements YouTubePlayer.PlaybackEventListener {


        /**
         * Called when video starts playing
         */
        @Override
        public void onPlaying() {
            Log.e("Status", "Playing");
        }


        /**
         * Called when video stops playing
         */
        @Override
        public void onPaused() {
            Log.e("Status", "Paused");
        }


        /**
         * Called when video stopped for a reason other than paused
         */
        @Override
        public void onStopped() {
            Log.e("Status", "Stopped");
        }


        /**
         * Called when buffering of video starts or ends
         *
         * @param b True if buffering is on, else false
         */
        @Override
        public void onBuffering(boolean b) {
        }


        /**
         * Called when jump in video happens. Reason can be either user scrubbing
         * or seek method is called explicitely
         *
         * @param i
         */
        @Override
        public void onSeekTo(int i) {
        }
    }

    private final class StateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        /**
         * Called when player begins loading a video. During this duration, player
         * won't accept any command that may affect the video playback
         */
        @Override
        public void onLoading() {
        }

        /**
         * Called when video is loaded. After this player can accept
         * the playback affecting commands
         *
         * @param s Video Id String
         */
        @Override
        public void onLoaded(String s) {
        }


        /**
         * Called when YouTube ad is started
         */
        @Override
        public void onAdStarted() {
        }


        /**
         * Called when video starts playing
         */
        @Override
        public void onVideoStarted() {
        }


        /**
         * Called when video is ended
         */
        @Override
        public void onVideoEnded() {
        }


        /**
         * Called when any kind of error occurs
         *
         * @param errorReason Error string showing the reason behind it
         */
        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
        }
    }
}
