package com.snowsea.school;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.snowsea.school.model.Response;
import com.snowsea.school.utils.DataManager;

public class QuestionDetail extends AppCompatActivity {

    TextView _txtTitle;
    TextView _txtQuestion;
    EditText _edtAnswer;
    String questionId;
    Boolean bAnswerable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataManager.getManager(QuestionDetail.this).answerQuestions(questionId, _edtAnswer.getText().toString(), new DataManager.ResponseDelegate() {
                    @Override
                    public void handleResponse(Response response) {
                        new AlertDialog.Builder(QuestionDetail.this, R.style.AppTheme_Dark_Dialog)
                                .setTitle(R.string.title_dialog_answered)
                                .setMessage(R.string.msg_successfully_answered)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                    }
                                }).show();
                    }

                    @Override
                    public void handleError(Throwable error) {
                        DataManager.getManager(QuestionDetail.this).handleError(error);
                    }
                });
            }
        });

        initView();
        handleIntent(getIntent());

        if (bAnswerable == false) {
            fab.setVisibility(View.GONE);
            _edtAnswer.setHint("");
            _edtAnswer.setClickable(false);
            _edtAnswer.setFocusable(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        _txtTitle.setText(intent.getStringExtra("Title"));
        _txtQuestion.setText(intent.getStringExtra("Question"));
        _edtAnswer.setText(intent.getStringExtra("Answer"));
        bAnswerable = intent.getBooleanExtra("Answerable", false);
        questionId = intent.getStringExtra("Id");

    }

    private void initView() {
        _txtTitle = (TextView) findViewById(R.id.txt_title);
        _txtQuestion = (TextView) findViewById(R.id.txt_content);
        _edtAnswer = (EditText) findViewById(R.id.edt_answer);
    }

}
