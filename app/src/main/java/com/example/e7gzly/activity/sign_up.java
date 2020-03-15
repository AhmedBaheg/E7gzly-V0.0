package com.example.e7gzly.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e7gzly.model.User;
import com.example.e7gzly.R;
import com.example.e7gzly.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {

    Button btn_Sign_Up_Now;
    EditText ed_First_Name, ed_Last_Name, ed_Email, ed_Password, ed_Confirm_Password;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ed_First_Name = findViewById(R.id.ed_First_Name);
        ed_Last_Name = findViewById(R.id.ed_Last_Name);
        ed_Email = findViewById(R.id.ed_Email);
        ed_Password = findViewById(R.id.ed_Password);
        ed_Confirm_Password = findViewById(R.id.ed_confirm_Password);
        btn_Sign_Up_Now = findViewById(R.id.btn_sign_up_now);
        btn_Sign_Up_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });


    }

    private void userSignUp() {

        final String firstName = ed_First_Name.getText().toString().trim();
        final String lastName = ed_Last_Name.getText().toString().trim();
        final String fullName = firstName + " " + lastName;
        final String email = ed_Email.getText().toString().trim();
        final String password = ed_Password.getText().toString().trim();
        String confirmPassword = ed_Confirm_Password.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {

            // First Name Is Empty
            ed_First_Name.setError("Please Enter Your First Name");
            ed_First_Name.requestFocus();

        } else if (TextUtils.isEmpty(lastName)) {

            ed_Last_Name.setError("Please Enter Your Last Name");
            ed_Last_Name.requestFocus();

        } else if (TextUtils.isEmpty(password)) {

            // Password Is Empty
            ed_Password.setError("Please Enter Your Password");
            ed_Password.requestFocus();

        } else if (TextUtils.isEmpty(email)) {

            // Email Is Empty
            ed_Email.setError("Please Enter Your Email");
            ed_Email.requestFocus();

        } else if (!isEmailValid(email)) {

            ed_Email.setError("Please Enter Correct Email example@example.com");
            ed_Email.requestFocus();

        } else if (password.length() < 6) {

            ed_Password.setError("The Password Should Be More Than 6 Digits");
            ed_Password.requestFocus();

        } else if (!password.equals(confirmPassword)) {

            ed_Confirm_Password.setError("The Two Password Not Equal");
            ed_Confirm_Password.requestFocus();

        } else {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                User user = new User(fullName, email);

                                FirebaseDatabase.getInstance().getReference().child(Constants.USERS)
                                        .child(Constants.getUID())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(sign_up.this, "Sign Up Complete", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(sign_up.this, login.class));
                                        finish();
                                    }
                                });

                            } else {

                                Toast.makeText(sign_up.this, "Your Email Already Exists", Toast.LENGTH_SHORT).show();

                            }

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