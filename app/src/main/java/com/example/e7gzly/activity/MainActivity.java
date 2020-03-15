package com.example.e7gzly.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.e7gzly.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_Sign_Up;
    Button btn_Login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Sign_Up = findViewById(R.id.btn_sign_up);
        btn_Sign_Up.setOnClickListener(this);
        btn_Login = findViewById(R.id.btn_login);
        btn_Login.setOnClickListener(this);


    }

    private void openSignUpActivity() {

        Intent intent = new Intent(this , sign_up.class);
        startActivity(intent);

    }

    private void openLoginActivity() {

        Intent intent = new Intent(this , login.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_sign_up :
                openSignUpActivity();
                break;
            case R.id.btn_login :
                openLoginActivity();
                break;

        }
    }
}
