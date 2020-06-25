package com.example.e7gzly.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.e7gzly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordDialog extends Dialog {

    public TextInputLayout current_Password, new_Password, confirm_New_Password;
    public Button btn_Save_Change, btn_Cancel_Change;
    public String input_Current_Password, input_New_Password, input_Confirm_New_Password;

    public ChangePasswordDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.change_password_dialog);
        setCanceledOnTouchOutside(false);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        getWindow().getAttributes().windowAnimations =
                android.R.style.Animation_Dialog;

        current_Password = findViewById(R.id.current_password);
        new_Password = findViewById(R.id.new_password);
        confirm_New_Password = findViewById(R.id.confirm_new_password);
        btn_Cancel_Change = findViewById(R.id.btn_cancel_change);
        btn_Save_Change = findViewById(R.id.btn_save_change);

        btn_Cancel_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public boolean validationCurrentPassword() {

        input_Current_Password = current_Password.getEditText().getText().toString();

        if (input_Current_Password.isEmpty()) {
            current_Password.setError("Please Enter Current Password");
            return false;
        } else if (input_Current_Password.length() < 6) {
            current_Password.setError("Password must be more than 6 digits");
            return false;
        } else {
            current_Password.setError(null);
            return true;
        }
    }

    public boolean validationNewPassword() {

        input_New_Password = new_Password.getEditText().getText().toString();

        if (input_New_Password.isEmpty()) {
            new_Password.setError("Please Enter New Password");
            return false;
        } else if (input_New_Password.length() < 6) {
            new_Password.setError("Password must be more than 6 digits");
            return false;
        } else {
            new_Password.setError(null);
            return true;
        }

    }

    public boolean validationConfirmNewPassword() {

        input_Confirm_New_Password = confirm_New_Password.getEditText().getText().toString();

        input_New_Password = new_Password.getEditText().getText().toString();

        if (input_Confirm_New_Password.isEmpty()) {
            confirm_New_Password.setError("Please Enter New Password");
            return false;
        } else if (!input_Confirm_New_Password.equals(input_New_Password)) {
            confirm_New_Password.setError("The two password not equals");
            return false;
        } else {
            confirm_New_Password.setError(null);
            return true;
        }

    }

    public void changePassword() {

        if (!validationCurrentPassword()) {
            current_Password.requestFocus();
        } else if (!validationNewPassword()) {
            new_Password.requestFocus();
        } else if (!validationConfirmNewPassword()) {
            confirm_New_Password.requestFocus();
        } else {


            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), input_Current_Password);
            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        firebaseUser.updatePassword(input_New_Password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Password has been change", Toast.LENGTH_LONG).show();
                                    dismiss();
                                } else {
                                    String meesage = task.getException().getMessage();
                                    Toast.makeText(getContext(), "Error Occurred " + meesage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        String meesage = task.getException().getMessage();
                        Toast.makeText(getContext(), "Error Occurred " + meesage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

}
