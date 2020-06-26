package com.example.e7gzly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e7gzly.R;
import com.example.e7gzly.dialog.ErrorConnectionDialog;
import com.example.e7gzly.model.CheckConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    @Override
    public void onClick(View v) {
        if (CheckConnection.isConnected(this)) {
            switch (v.getId()) {

                case R.id.btn_sign_up:
                    startActivity(new Intent(MainActivity.this, sign_up.class));
                    break;
                case R.id.btn_login:
                    startActivity(new Intent(MainActivity.this , login.class));
                    break;

            }
        }else {
            ErrorConnectionDialog dialog = new ErrorConnectionDialog(this);
            dialog.show();
            dialog.checkConnection();
        }
    }
}
