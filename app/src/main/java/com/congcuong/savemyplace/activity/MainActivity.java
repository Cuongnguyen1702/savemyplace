package com.congcuong.savemyplace.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.congcuong.savemyplace.R;
import com.congcuong.savemyplace.data.model.DBUtils;
import com.congcuong.savemyplace.data.model.Place;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener{

    @BindView(R.id.layoutSplash)
    RelativeLayout layout;
    @BindView(R.id.imgLogoSplash)
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Animation transitionAnim = AnimationUtils.loadAnimation(this, R.anim.transition_icon);
        imgLogo.setAnimation(transitionAnim);

        Animation alphaAnim = AnimationUtils.loadAnimation(this, R.anim.alpha_background);
        layout.setAnimation(alphaAnim);
        alphaAnim.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, CategoriesActivity.class));
                finish();
            }
        }, 2500);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
