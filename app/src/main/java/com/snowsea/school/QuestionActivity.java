package com.snowsea.school;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.snowsea.school.model.Question;
import com.snowsea.school.utils.DataManager;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ListView _listView;
    MenuItem _action_answer_all;
    MenuItem _action_answered;
    MenuItem _action_unanswered;
    MenuItem _action_user_all;
    MenuItem _action_user_me;
    String user_id;
    Boolean bAnswerable = false;

    int courseNumber = 0;
    int levelNumber = 0;
    int subjectNumber = 0;

    String filterUser = "ALL";
    String filterAnswer = "ALL";

    QuestionAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handleIntent(getIntent());
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        user_id = intent.getStringExtra("UserID");
        courseNumber = intent.getIntExtra("course_number", 0);
        levelNumber = intent.getIntExtra("level_number", 0);
        subjectNumber = intent.getIntExtra("subject_number", 0);
        bAnswerable = intent.getBooleanExtra("Answerable", false);
    }

    private void initView() {
        _listView = (ListView) findViewById(R.id.list_view);

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
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

    private void loadData() {
        showProgressBar(getResources().getString(R.string.loading));
        DataManager.getManager(this).getQuestions(courseNumber, levelNumber, subjectNumber, new DataManager.QuestionDelegate() {
            @Override
            public void handleResponse(ArrayList<Question> response) {
                hideProgressBar();
                adapter = new QuestionAdapter(QuestionActivity.this, response, bAnswerable);
                _listView.setAdapter(adapter);
            }

            @Override
            public void handleError(Throwable error) {
                hideProgressBar();
                DataManager.getManager(QuestionActivity.this).handleError(error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.questions, menu);

        _action_answered = menu.findItem(R.id.action_answered);
        _action_unanswered = menu.findItem(R.id.action_unanswered);
        _action_answer_all = menu.findItem(R.id.action_answer_all);
        _action_user_all = menu.findItem(R.id.action_user_all);
        _action_user_me = menu.findItem(R.id.action_user_me);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(filterUser + "-" + filterAnswer + "-" + user_id + "-" + newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_answer_all:
                _action_answer_all.setChecked(true);
                break;
            case R.id.action_answered:
                _action_answered.setChecked(true);
                break;
            case R.id.action_unanswered:
                _action_unanswered.setChecked(true);
                break;
            case R.id.action_user_all:
                _action_user_all.setChecked(true);
                break;
            case R.id.action_user_me:
                _action_user_me.setChecked(true);
                break;

        }

        String answer = _action_answered.isChecked() ? "answered" : "unanswered";
        answer = _action_answer_all.isChecked() ? "all" : answer;
        String user = _action_user_all.isChecked() ? "all" : "me";

        if (id == R.id.action_answer_all || id == R.id.action_answered || id == R.id.action_unanswered || id == R.id.action_user_all || id == R.id.action_user_me) {
            adapter.getFilter().filter(user + "-" + answer + "-" + user_id);
            filterUser = user;
            filterAnswer = answer;
        }

        return super.onOptionsItemSelected(item);
    }

}
