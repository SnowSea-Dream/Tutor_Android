package com.snowsea.school;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.btn_login) FancyButton _btnLogin;
    @BindView(R.id.btn_signup) FancyButton _btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
            }
        });

        _btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
            }
        });
    }
}
