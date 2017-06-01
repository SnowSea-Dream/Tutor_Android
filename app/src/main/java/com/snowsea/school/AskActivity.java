package com.snowsea.school;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.snowsea.school.model.Response;
import com.snowsea.school.utils.Constants;
import com.snowsea.school.utils.DataManager;

public class AskActivity extends AppCompatActivity implements DataManager.ResponseDelegate {

    EditText _edtTitle;
    EditText _edtContent;

    SharedPreferences mSharedPreferences;
    int courseNumber = 0;
    int levelNumber = 0;
    int subjectNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _edtTitle = (EditText) findViewById(R.id.edt_title);
        _edtContent = (EditText) findViewById(R.id.edt_content);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() == false)
                    return;
                DataManager.getManager(AskActivity.this).askQuestions(courseNumber, levelNumber, subjectNumber, _edtTitle.getText().toString(), _edtContent.getText().toString(), AskActivity.this);
            }
        });
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {
        courseNumber = intent.getIntExtra("course_number", 0);
        levelNumber = intent.getIntExtra("level_number", 0);
        subjectNumber = intent.getIntExtra("subject_number", 0);
    }

    @Override
    public void handleResponse(Response response) {
        new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog)
            .setTitle("Make Question")
            .setMessage(response.getMessage())
            .setCancelable(false)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, close
                    // current activity
                    AskActivity.this.finish();
                }
            }).show();
    }

    public boolean validate() {
        boolean valid = true;

        String title = _edtTitle.getText().toString();
        String content = _edtContent.getText().toString();

        if (title.isEmpty()) {
            _edtTitle.setError("Please Enter Title!");
            valid = false;
        } else {
            _edtTitle.setError(null);
        }

        if (content.isEmpty()) {
            _edtContent.setError("Please Enter Content!");
            valid = false;
        } else {
            _edtContent.setError(null);
        }

        return valid;
    }

    @Override
    public void handleError(Throwable error) {
        DataManager.getManager(this).handleError(error);
    }
}
