package com.example.e7gzly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e7gzly.R;
import com.example.e7gzly.dialog.ErrorConnectionDialog;
import com.example.e7gzly.model.CheckConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot_Password extends AppCompatActivity {


    private Button btn_Forgot_Password;
    private TextInputLayout ed_FGP_Email;

    // Fire Base

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        firebaseAuth = FirebaseAuth.getInstance();

        ed_FGP_Email = findViewById(R.id.ed_fgp_Email);
        btn_Forgot_Password = findViewById(R.id.btn_forgot_password);

        btn_Forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckConnection.isConnected(Forgot_Password.this)) {
                    resetForgotPass();
                }else {
                    ErrorConnectionDialog dialog = new ErrorConnectionDialog(Forgot_Password.this);
                    dialog.show();
                    dialog.checkConnection();
                }
            }
        });

    }

    // This method to kill this page after move from it to login page and can't back again
    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    public void resetForgotPass() {

        String user_Email = ed_FGP_Email.getEditText().getText().toString();

        if (!validationEmail()) {
            ed_FGP_Email.requestFocus();
        } else {

            firebaseAuth.sendPasswordResetEmail(user_Email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(Forgot_Password.this, "Please Check Your Email Account, If You Want Reset Your Password ", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Forgot_Password.this, login.class));
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Forgot_Password.this, "Error Occurred " + message, Toast.LENGTH_LONG).show();
                            }

                        }
                    });

        }

    }

    private boolean validationEmail() {
        String input_FGB_Email = ed_FGP_Email.getEditText().getText().toString();
        if (input_FGB_Email.isEmpty()) {
            ed_FGP_Email.setError("Enter Your Email");
            return false;
        } else if (!isEmailValid(input_FGB_Email)) {
            ed_FGP_Email.setError("Enter Correct Email example@example.com");
            return false;
        } else {
            ed_FGP_Email.setError(null);
            return true;
        }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
