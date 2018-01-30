package com.snowsea.accountingtutors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snowsea.accountingtutors.model.Login;
import com.snowsea.accountingtutors.model.Response;
import com.snowsea.accountingtutors.model.User;
import com.snowsea.accountingtutors.network.NetworkUtil;
import com.snowsea.accountingtutors.utils.Constants;
import com.snowsea.accountingtutors.utils.DataManager;
import com.snowsea.accountingtutors.utils.GlobalData;

import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    FancyButton _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.link_forgotpassword)
    TextView _forgotLink;

    @BindView(R.id.scrollView)
    ScrollView _scrollView;

    private SharedPreferences mSharedPreferences;

    private CompositeSubscription mSubscriptions;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mSubscriptions = new CompositeSubscription();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
                //Intent intent = new Intent(getBaseContext(), DashBoardActivity.class);
                //startActivity(intent);
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _forgotLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        initSharedPreferences();
    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }


    public void login() {
        if (!validate()) {
            //onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.


        loginProcess(email, password);
    }

    private void loginProcess(String email, String password) {

        mSubscriptions.add(NetworkUtil.getLoginRetrofit(email, password).login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(Login response) {

        progressDialog.dismiss();

        User user = response.getUser();
        GlobalData.user = user;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN, response.getToken());
        editor.putString(Constants.EMAIL, user.getEmail());
        editor.apply();

        _emailText.setText(null);
        _passwordText.setText(null);
        _loginButton.setEnabled(true);

        if (user.getUserType() == 0) {
            Intent intent = new Intent(getBaseContext(), DashBoardActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getBaseContext(), LecturerActivity.class);
            startActivity(intent);
        }
        //Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
    }

    private void handleError(Throwable error) {

        progressDialog.dismiss();
        _loginButton.setEnabled(true);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);

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

    private void showSnackBarMessage(String message) {
        Snackbar.make(_scrollView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        super.onBackPressed();
        //moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}