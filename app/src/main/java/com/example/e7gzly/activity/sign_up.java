package com.example.e7gzly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e7gzly.R;
import com.example.e7gzly.model.User;
import com.example.e7gzly.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {

    Button btn_Sign_Up_Now;
    TextView tv_Login;
    TextInputLayout ed_First_Name, ed_Last_Name, ed_Email, ed_Password, ed_Confirm_Password;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    ImageView image;

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

        tv_Login = findViewById(R.id.tv_login);
        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sign_up.this , login.class));
            }
        });


    }

    private void userSignUp() {

        final String firstName = ed_First_Name.getEditText().getText().toString().trim();
        final String lastName = ed_Last_Name.getEditText().getText().toString().trim();
        final String fullName = firstName + " " + lastName;
        final String email = ed_Email.getEditText().getText().toString().trim();
        final String password = ed_Password.getEditText().getText().toString().trim();

        if (!validationFirstName()) {
            ed_First_Name.requestFocus();
        } else if (!validationLastName()) {
            ed_Last_Name.requestFocus();
        } else if (!validationEmail()) {
            ed_Email.requestFocus();
        } else if (!validationPassword()) {
            ed_Password.requestFocus();
        } else if (!validationConfirmPassword()) {
            ed_Confirm_Password.requestFocus();
        } else {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            User user = new User(fullName, email);
                                            FirebaseDatabase.getInstance().getReference("User")
                                                    .child(Constants.getUID())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(sign_up.this, "Please Verification Your Account",
                                                            Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(sign_up.this, login.class));
                                                }
                                            });

                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(sign_up.this, "Error Occurred " + message,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(sign_up.this, "Your Email Already Exists", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }
    }

    private boolean validationFirstName() {
        String input_FirstName = ed_First_Name.getEditText().getText().toString();
        if (input_FirstName.isEmpty()) {
            ed_First_Name.setError("Please Enter Your First Name");
            return false;
        } else {
            ed_First_Name.setError(null);
            return true;
        }
    }

    private boolean validationLastName() {
        String input_LastName = ed_Last_Name.getEditText().getText().toString();
        if (input_LastName.isEmpty()) {
            ed_Last_Name.setError("Please Enter Your Last Name");
            return false;
        } else {
            ed_Last_Name.setError(null);
            return true;
        }
    }

    private boolean validationEmail() {
        String input_SignUpEmail = ed_Email.getEditText().getText().toString();
        if (input_SignUpEmail.isEmpty()) {
            ed_Email.setError("Enter Your Email");
            return false;
        } else if (!isEmailValid(input_SignUpEmail)) {
            ed_Email.setError("Enter Correct Email example@example.com");
            return false;
        } else {
            ed_Email.setError(null);
            return true;
        }
    }

    private boolean validationPassword() {
        String input_SignUpPassword = ed_Password.getEditText().getText().toString();
        if (input_SignUpPassword.isEmpty()) {
            ed_Password.setError("Please Enter Your Password");
            return false;
        } else if (input_SignUpPassword.length() < 6) {
            ed_Password.setError("The Password Should Be More Than 6 Digits");
            return false;
        } else {
            ed_Password.setError(null);
            return true;
        }
    }

    private boolean validationConfirmPassword() {
        String input_ConfirmSignUpPassword = ed_Confirm_Password.getEditText().getText().toString();
        String input_SignUpPassword = ed_Password.getEditText().getText().toString();
        if (input_ConfirmSignUpPassword.isEmpty()) {
            ed_Confirm_Password.setError("Please Enter Your Password");
            return false;
        } else if (!input_ConfirmSignUpPassword.equals(input_SignUpPassword)) {
            ed_Confirm_Password.setError("The Two Password Not Equals");
            return false;
        }
        else {
            ed_Confirm_Password.setError(null);
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