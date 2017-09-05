package com.snowsea.accountingtutors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.snowsea.accountingtutors.model.Course;
import com.snowsea.accountingtutors.model.Lecturer;
import com.snowsea.accountingtutors.utils.DataManager;
import com.snowsea.accountingtutors.utils.GlobalData;

import java.util.ArrayList;
import java.util.List;

public class LecturerActivity extends AppCompatActivity implements DataManager.LecturerDelegate {

    private Toolbar toolbar;
    Lecturer lecturer;
    AssignedSubjectAdapter adapter;
    ListView _listView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handleIntent(getIntent());

        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initView() {

        _listView = (ListView) findViewById(R.id.list_view);
        _listView.setDivider(null);

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    private void loadData() {
        showProgressBar(getResources().getString(R.string.loading));
        DataManager.getManager(this).getCourses(new DataManager.CourseDelegate() {
            @Override
            public void handleResponse(ArrayList<Course> response) {
                GlobalData.lstCourse = response;
                DataManager.getManager(LecturerActivity.this).getLecturer(LecturerActivity.this);
            }

            @Override
            public void handleError(Throwable error) {
                hideProgressBar();
                DataManager.getManager(LecturerActivity.this).handleError(error);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

    }

    public void showProgressBar(String message) {
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideProgressBar() {
        progressDialog.dismiss();
    }



    @Override
    public void handleResponse(Lecturer response) {
        hideProgressBar();
        lecturer = response;
        adapter = new AssignedSubjectAdapter(this, lecturer);

        _listView.setAdapter(adapter);
    }

    @Override
    public void handleError(Throwable error) {
        hideProgressBar();
        DataManager.getManager(this).handleError(error);
    }
}
