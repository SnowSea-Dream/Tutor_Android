package com.snowsea.accountingtutors;

import android.app.SearchManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {


    VideoView _video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        _video = (VideoView) this.findViewById(R.id.video_view);

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(_video);
        _video.setMediaController(mediaController);

        _video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                _video.start();
            }
        });

        _video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        _video.setVideoURI(Uri.parse(url));
    }
}
