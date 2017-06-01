package com.snowsea.school;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.snowsea.school.model.Chapter;
import com.snowsea.school.utils.Constants;

import java.io.BufferedInputStream;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import mehdi.sakout.fancybuttons.FancyButton;

class DownloadFileFromURL extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */

    public ProgressBar _progressBar;
    public FancyButton _btnDownload;
    public Context context;

    private String fileUrl;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _progressBar.setVisibility(View.VISIBLE);
        _btnDownload.setIconResource(context.getResources().getString(R.string.icon_download));
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            fileUrl = f_url[1];
            OutputStream output = new FileOutputStream(f_url[1] + "_");

            byte data[] = new byte[2048];

            //long total = 0;

            while ((count = input.read(data)) != -1) {
                //total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                //publishProgress(""+(int)((total*100)/lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        _progressBar.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        File from = new File(fileUrl + "_");
        File to = new File(fileUrl);
        from.renameTo(to);

        _progressBar.setVisibility(View.GONE);
        _btnDownload.setIconResource(context.getResources().getString(R.string.icon_checked));
    }

}

/**
 * A simple {@link Fragment} subclass.
 */
public class ChapterTypeFragment extends Fragment {

    FancyButton _btnVideo;
    FancyButton _btnNote;
    FancyButton _btnVideoDownload;
    FancyButton _btnNoteDownload;
    ProgressBar _proVideo;
    ProgressBar _proNote;
    Chapter chapter;

    public ChapterTypeFragment() {
        // Required empty public constructor

    }

    public void setInfo(Chapter chapter) {
        this.chapter = chapter;
    }

    public void setDownloadButton(FancyButton btn, String urlSrc, String urlDest, ProgressBar prog) {

        DashBoardActivity activity = (DashBoardActivity) getActivity();
        File file = new File ( urlDest );

        if ( file.exists() )
        {
            btn.setIconResource(activity.getResources().getString(R.string.icon_checked));
        }
        else {
            btn.setIconResource(activity.getResources().getString(R.string.icon_download));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prog.getVisibility() != View.VISIBLE) {
                    DownloadFileFromURL download = new DownloadFileFromURL();
                    download._progressBar = prog;
                    download._btnDownload = btn;
                    download.context = getActivity();
                    download.execute(urlSrc, urlDest);
                }
            }
        });
    }

    public void initView(View v) {

        _btnVideo = (FancyButton) v.findViewById(R.id.btn_video);
        _btnNote = (FancyButton) v.findViewById(R.id.btn_note);
        _btnVideoDownload = (FancyButton) v.findViewById(R.id.btn_video_download);
        _btnNoteDownload = (FancyButton) v.findViewById(R.id.btn_note_download);
        _proVideo = (ProgressBar) v.findViewById(R.id.pro_video);
        _proNote = (ProgressBar) v.findViewById(R.id.pro_note);

        DashBoardActivity activity = (DashBoardActivity) getActivity();

        String dirPath = activity.getFilesDir().getAbsolutePath();

        String urlSrcVideo = Constants.VIDEO_FOLDER_URL + chapter.getVideoUrl();
        String[] path;
        Log.e("Video_Url", chapter.getVideoUrl());
        Log.e("Note_Url", chapter.getNoteUrl());
        if (chapter.getVideoUrl() != null) {
            path = chapter.getVideoUrl().split("/");
            String fileName = path[path.length - 1];
            String urlDestVideo = dirPath + "/" + fileName;
            setDownloadButton(_btnVideoDownload, urlSrcVideo, urlDestVideo, _proVideo);
        }

        if (chapter.getNoteUrl() != null) {
            String urlSrcNote = Constants.NOTE_FOLDER_URL + chapter.getNoteUrl();
            path = chapter.getNoteUrl().split("/");
            String urlDestNote = dirPath + "/" + path[path.length - 1];
            setDownloadButton(_btnNoteDownload, urlSrcNote, urlDestNote, _proNote);
        }

        _btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.loadVideoFragment();
            }
        });

        _btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.loadNoteFragment();
            }
        });

        activity.setStage(DashBoardActivity.Stage.CHAPTER_TYPE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chapter_type, container, false);

        initView(v);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
