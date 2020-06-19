package com.example.e7gzly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e7gzly.R;
import com.example.e7gzly.dialog.ErrorConnectionDialog;
import com.example.e7gzly.model.CheckConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity implements View.OnClickListener {

    Button btn_Login_Now;
    TextInputLayout ed_Email_Login, ed_Password_Login;
    TextView tv_Forgot_Password;

    // FireBase

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        ed_Email_Login = findViewById(R.id.ed_email_login);
        ed_Password_Login = findViewById(R.id.ed_Password_login);
        btn_Login_Now = findViewById(R.id.btn_login_now);
        btn_Login_Now.setOnClickListener(this);
        tv_Forgot_Password = findViewById(R.id.tv_forgot_password);
        tv_Forgot_Password.setOnClickListener(this);

    }


    private void UserLogin() {
        String email = ed_Email_Login.getEditText().getText().toString();
        String password = ed_Password_Login.getEditText().getText().toString();

        if (CheckConnection.isConnected(this)) {
            if (!validationEmail()) {
                ed_Email_Login.requestFocus();

            } else if (!validationPassword()) {
                ed_Password_Login.requestFocus();
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        startActivity(new Intent(login.this, Home.class));
                                    } else {
                                        Toast.makeText(login.this, "Please Check Your Email And Verify Your Account", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(login.this, "Email Or Password Incorrect", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });

            }
        } else {
            ErrorConnectionDialog dialog = new ErrorConnectionDialog(this);
            dialog.show();
            dialog.checkConnection();
        }
        }


    private boolean validationEmail() {
        String input_Email = ed_Email_Login.getEditText().getText().toString();
        if (input_Email.isEmpty()) {
            ed_Email_Login.setError("Enter Your Email");
            return false;
        } else if (!isEmailValid(input_Email)) {
            ed_Email_Login.setError("Enter Correct Email example@example.com");
            return false;
        } else {
            ed_Email_Login.setError(null);
            return true;
        }
    }

    private boolean validationPassword() {
        String input_Password = ed_Password_Login.getEditText().getText().toString();
        if (input_Password.isEmpty()) {
            ed_Password_Login.setError("Please Enter Your Password");
            return false;
        } else if (input_Password.length() < 6) {
            ed_Password_Login.setError("The Password Should Be More Than 6 Digits");
            return false;
        } else {
            ed_Password_Login.setError(null);
            return true;
        }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {
        if (CheckConnection.isConnected(this)){

            switch (v.getId()){
                case R.id.btn_login_now :
                    UserLogin();
                    break;
                case R.id.tv_forgot_password :
                    startActivity(new Intent(login.this , Forgot_Password.class));
                    break;
            }

        }else {
            ErrorConnectionDialog dialog = new ErrorConnectionDialog(this);
            dialog.show();
            dialog.checkConnection();
        }
    }
}
