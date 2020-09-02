package com.example.e7gzly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e7gzly.R;
import com.example.e7gzly.dialog.ErrorConnectionDialog;
import com.example.e7gzly.model.CheckConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rbddevs.splashy.Splashy;

public class SplashActivity extends AppCompatActivity {

    Animation animation;
    ImageView Splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Splashy splashy = new Splashy(this);
        splashy.setBackgroundResource(R.drawable.background)
                .setLogo(R.drawable.logochoose)
                .setLogoWHinDp(ViewGroup.LayoutParams.MATCH_PARENT,350)
                .showTitle(false)
                .showProgress(true)
                .setProgressColor(R.color.colorWhite)
                .setFullScreen(true)
                .setAnimation(Splashy.Animation.SLIDE_IN_LEFT_RIGHT,1000)
                .setTime(5000)
                .show();

        splashy.Companion.onComplete(new Splashy.OnComplete() {
            @Override
            public void onComplete() {
                checkUser();
            }
        });
    }

    private void checkUser() {
        if (CheckConnection.isConnected(this)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                if (user.isEmailVerified()) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                // if( firebaseUser == null )
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            ErrorConnectionDialog dialog = new ErrorConnectionDialog(this);
            dialog.show();
            dialog.checkConnection();
        }

    }

}