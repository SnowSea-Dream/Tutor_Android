package com.snowsea.school;


import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.snowsea.school.model.Chapter;
import com.snowsea.school.utils.Constants;

import java.io.File;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 *
 */
public class VideoFragment extends Fragment{

    Chapter chapter;
    RecyclerView _lstChapter;
    ChapterAdapter adapter;

    int courseNumber;
    int levelNumber;

    VideoView _video;
    FancyButton _btnFullScreen;
    MediaController mediaController;
    TextView _txtChapter;
    View _layout;
    private int height;

    String email;
    String token;
    boolean bFullScreen = false;

    String url;

    public VideoFragment() {
        // Required empty public constructor
    }

    public void setInfo(Chapter chapter, String email, String token) {
        this.chapter = chapter;
        this.email = email;
        this.token = email;
    }

    private void initViews(View v) {

        _video = (VideoView)v.findViewById(R.id.videoView);
        mediaController = new
                MediaController(getActivity());
        mediaController.setAnchorView(_video);
        _video.setMediaController(mediaController);

        _txtChapter = (TextView)v.findViewById(R.id.txt_chapter);
        _txtChapter.setText(chapter.getName());
        _layout = v.findViewById(R.id.layout);

        DashBoardActivity activity = (DashBoardActivity)getActivity();

        String []path = chapter.getVideoUrl().split("/");
        String dirPath = activity.getFilesDir().getAbsolutePath();
        String localPath = dirPath + "/" + path[path.length - 1];
        String remotePath = Constants.VIDEO_FOLDER_URL + chapter.getVideoUrl();

        File file = new File ( localPath );

        if ( file.exists() )
        {
            url = localPath;
        }
        else {
            url = remotePath;
        }

        _video.setVideoURI(Uri.parse(url));


        activity.showProgressBar("Loading...");

        _video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                _video.start();
                activity.hideProgressBar();
            }
        });

        _video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                activity.hideProgressBar();
                return false;
            }
        });

        _btnFullScreen = (FancyButton)v.findViewById(R.id.btn_fullscreen);
        _btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportActionBar().hide();
                _txtChapter.setVisibility(View.GONE);
                _btnFullScreen.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)_video.getLayoutParams();
                height = params.height;

                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                _video.setLayoutParams(params);

                FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams)_layout.getLayoutParams();

                params1.topMargin = 0;
                _layout.setLayoutParams(params1);
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                bFullScreen = true;
            }
        });

        activity.setStage(DashBoardActivity.Stage.VIDEO);

    }

    public void onNormalSize() {
        DashBoardActivity activity = (DashBoardActivity)getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity.getSupportActionBar().show();
        _txtChapter.setVisibility(View.VISIBLE);
        _btnFullScreen.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)_video.getLayoutParams();
        params.height = height;
        _video.setLayoutParams(params);
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams)_layout.getLayoutParams();
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            params1.topMargin = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        _layout.setLayoutParams(params1);
        bFullScreen = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        initViews(v);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
