package com.example.e7gzly.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.e7gzly.R;
import com.example.e7gzly.model.CheckConnection;

public class ErrorConnectionDialog extends Dialog{

    public ImageView dialog_Img_error;
    public TextView dialog_tv_message, dialog_tv_no_internet;
    public Button btn_retry_error_connection;
    public Context context;

    public ErrorConnectionDialog(@NonNull Context context) {
        super(context);


        // Set Custom Dialog
        setContentView(R.layout.error_connection_dialog);
        setCanceledOnTouchOutside(false);

        // Set Dialog Width and Height
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        getWindow().getAttributes().windowAnimations =
                android.R.style.Animation_Dialog;


        dialog_Img_error = findViewById(R.id.dialog_img_error);
        dialog_tv_message = findViewById(R.id.dialog_tv_message);
        dialog_tv_no_internet = findViewById(R.id.dialog_tv_no_internet);
        btn_retry_error_connection = findViewById(R.id.btn_retry_error_connection);

    }
    
    public void checkConnection(){
        btn_retry_error_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnection.isConnected(getContext())){
                    dismiss();
                }else {
                    Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
