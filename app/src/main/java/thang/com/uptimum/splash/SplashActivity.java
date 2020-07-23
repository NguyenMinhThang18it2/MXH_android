package thang.com.uptimum.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import thang.com.uptimum.R;
import thang.com.uptimum.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;

    private Animation topAnimation, bottomAnimation;
    private ImageView image;
    private TextView tvSlogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        topAnimation = (Animation) AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = (Animation) AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        image = (ImageView) findViewById(R.id.splashimage);
        tvSlogan = (TextView) findViewById(R.id.slogan);

        image.setAnimation(topAnimation);
        tvSlogan.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
