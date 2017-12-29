package xebia.ismail.firstblood;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import xebia.ismail.firstblood.view.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView iv;
    TextView tv;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        iv = (ImageView)findViewById(R.id.logo);
        tv = (TextView)findViewById(R.id.label);

        StartAnimations();
        Thread splash = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    finish();
                }
            }
        };
        splash.start();
    }

    private void StartAnimations() {

        Animation popeup1 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup2 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup3 = AnimationUtils.loadAnimation(this, R.anim.showin);

        popeup1.setStartOffset(300);
        popeup2.setStartOffset(350);
        popeup3.setStartOffset(500);

        iv.startAnimation(popeup1);
        tv.startAnimation(popeup3);
    }
}
