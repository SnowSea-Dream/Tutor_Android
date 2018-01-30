package com.snowsea.accountingtutors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snowsea.accountingtutors.model.Response;
import com.snowsea.accountingtutors.network.NetworkUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    TextView _emailText;
    @BindView(R.id.btn_sendcode)
    FancyButton _sendCodeBtn;

    private CompositeSubscription mSubscriptions;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        mSubscriptions = new CompositeSubscription();

        _sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }

    public void sendCode() {
        if (!validate()) {
            return;
        }

        _sendCodeBtn.setEnabled(false);

        progressDialog = new ProgressDialog(ForgotPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending...");
        progressDialog.show();

        String email = _emailText.getText().toString();

        mSubscriptions.add(NetworkUtil.getRetrofit().resetPasswordInit(email)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse,this::handleError));

    }

    private void handleResponse(Response response) {

        _sendCodeBtn.setEnabled(true);
        progressDialog.dismiss();
        //setResult(RESULT_OK, null);
        new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog)
                .setTitle("Send Code")
                .setMessage(R.string.msg_dialog_sendcode_success)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                        intent.putExtra("email", _emailText.getText().toString());
                        startActivity(intent);
//                        setResult(RESULT_OK, null);
//                        finish();
                    }
                }).show();
    }

    private void handleError(Throwable error) {
        _sendCodeBtn.setEnabled(true);
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
