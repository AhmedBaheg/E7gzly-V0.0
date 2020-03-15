package com.example.e7gzly.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e7gzly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    Button btn_Login_Now;
    TextInputLayout ed_Email_Login, ed_Password_Login;
    // FireBase
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        ed_Email_Login = findViewById(R.id.ed_email_login);
        ed_Password_Login = findViewById(R.id.ed_Password_login);
        btn_Login_Now = findViewById(R.id.btn_login_now);
        btn_Login_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UserLogin(ed_Email_Login.getText().toString(), ed_Password_Login.getText().toString());
                UserLogin();
            }
        });

    }

    private void UserLogin() {
        String email = ed_Email_Login.getEditText().getText().toString();
        String password = ed_Password_Login.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {

            // Email Is Empty
            ed_Email_Login.setError("Please Enter Your Email");
            ed_Email_Login.requestFocus();

        } else if (!isEmailValid(email)) {

            ed_Email_Login.setError("Please Enter Correct Email example@example.com");
            ed_Email_Login.requestFocus();

        } else if (TextUtils.isEmpty(password)) {

            // Password Is Empty
            ed_Password_Login.setError("Please Enter Your Password");
            ed_Password_Login.requestFocus();

        } else if (password.length() < 6) {

            ed_Password_Login.setError("The Password Should Be More Than 6 Digits");
            ed_Password_Login.requestFocus();

        } else {

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                startActivity(new Intent(login.this, Home.class));

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(login.this, "Email Or Password Incorrect", Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });

        }


    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
