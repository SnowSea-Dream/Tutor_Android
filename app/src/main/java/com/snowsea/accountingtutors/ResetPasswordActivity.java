package com.snowsea.accountingtutors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snowsea.accountingtutors.model.Response;
import com.snowsea.accountingtutors.model.User;
import com.snowsea.accountingtutors.network.NetworkUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.input_code)
    TextView _codeText;
    @BindView(R.id.input_password)
    TextView _passwordText;
    @BindView(R.id.btn_reset)
    FancyButton _resetBtn;

    private CompositeSubscription mSubscriptions;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        mSubscriptions = new CompositeSubscription();

        _resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String code = _codeText.getText().toString();
        String password = _passwordText.getText().toString();

        if (code.isEmpty() || code.length() != 8) {
            _codeText.setError("enter a valid code");
            valid = false;
        } else {
            _codeText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void resetPassword() {
        if (!validate()) {
            return;
        }

        _resetBtn.setEnabled(false);

        progressDialog = new ProgressDialog(ResetPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending...");
        progressDialog.show();

        String email = getIntent().getExtras().getString("email");
        String code = _codeText.getText().toString();
        String password = _passwordText.getText().toString();

        User user = new User();
        user.setCode(code);
        user.setPassword(password);
        mSubscriptions.add(NetworkUtil.getRetrofit().resetPasswordFinish(email, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));

    }

    private void handleResponse(Response response) {

        _resetBtn.setEnabled(true);
        progressDialog.dismiss();
        //setResult(RESULT_OK, null);
        new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog)
                .setTitle("Reset Password")
                .setMessage(R.string.msg_dialog_reset_password_success)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        setResult(RESULT_OK, null);
                        finish();
                    }
                }).show();
    }

    private void handleError(Throwable error) {
        _resetBtn.setEnabled(true);
        progressDialog.dismiss();

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                //showSnackBarMessage(response.getMessage());
                new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog)
                        .setTitle(R.string.error)
                        .setMessage(response.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                            }
                        }).show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog)
                    .setTitle(R.string.error)
                    .setMessage(R.string.msg_dialog_network_error)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                        }
                    }).show();
        }

    }
}
