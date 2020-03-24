package com.example.e7gzly.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.e7gzly.R;

import java.util.regex.Pattern;

public class EditDialog extends Dialog {

    public TextView tv_title_dialog;
    public EditText ed_dialog_name;
    public Button btn_cancel, btn_save;
    View view;


    public EditDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.edit_dialog);
        setCanceledOnTouchOutside(false);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        getWindow().getAttributes().windowAnimations =
                android.R.style.Animation_Dialog;

        tv_title_dialog = findViewById(R.id.tv_title_dialog);
        ed_dialog_name = findViewById(R.id.ed_dialog_name);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
        view = findViewById(R.id.view);

        ed_dialog_name.requestFocus();
//        ed_dialog_name.setSelected(true);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private final static Pattern PATTERN_NAME = Pattern.compile("[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FFa-zA-Z ]+[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FFa-zA-Z-_ ]");

    public boolean validationName() {
        String text = ed_dialog_name.getText().toString();
        if (text.isEmpty()) {
            ed_dialog_name.setError("Please enter your name");
            return false;
        } else if (!PATTERN_NAME.matcher(text).matches()) {
            ed_dialog_name.setError("Please enter alphabet letters only");
            return false;
        } else {
            return true;

        }

    }
}
