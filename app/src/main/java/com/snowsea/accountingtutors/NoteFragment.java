package com.snowsea.accountingtutors;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.snowsea.accountingtutors.model.Chapter;
import com.snowsea.accountingtutors.utils.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 *
 */
public class NoteFragment extends Fragment{

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        public DashBoardActivity activity;
        public PDFView pdfView;
        public String localUrl;

        public Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.showProgressBar(activity.getResources().getString(R.string.downloading));
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
                OutputStream output = new FileOutputStream(f_url[1]);

                byte data[] = new byte[1024];

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

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            activity.hideProgressBar();
            pdfView.fromFile(new File(localUrl))
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    .load();
        }

    }

    Chapter chapter;

    String email;
    String token;

    String url;

    public NoteFragment() {
        // Required empty public constructor
    }

    public void setInfo(Chapter chapter, String email, String token) {
        this.chapter = chapter;
        this.email = email;
        this.token = email;
    }

    private void initViews(View v) {

        DashBoardActivity activity = (DashBoardActivity)getActivity();

        String dirPath = activity.getFilesDir().getAbsolutePath();
        String []path = chapter.getNoteUrl().split("/");
        String localUrl = dirPath + "/" +  path[path.length - 1];
        String remoteUrl = Constants.NOTE_FOLDER_URL + chapter.getNoteUrl();


        PDFView pdfView = (PDFView) v.findViewById(R.id.pdf_view);

        DownloadFileFromURL download = new DownloadFileFromURL();
        download.activity = activity;
        download.pdfView = pdfView;
        download.localUrl = localUrl;
        File file = new File(localUrl);

        if (file.exists()) {
            pdfView.fromFile(new File(localUrl))
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    .load();
        }
        else {
            download.execute(remoteUrl, localUrl);
        }

        activity.setStage(DashBoardActivity.Stage.NOTE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_note, container, false);
        initViews(v);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
