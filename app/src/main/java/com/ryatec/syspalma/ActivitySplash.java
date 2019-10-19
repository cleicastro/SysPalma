package com.ryatec.syspalma;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryatec.syspalma.R;

import java.io.File;

public class ActivitySplash extends Activity {
    private static int SPLASH_TIME_OUT = 5000;
    private TextView textversion;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textversion = (TextView) findViewById(R.id.idversion);

        //Display da versão do sistema
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            textversion.setText("V."+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        StartAnimations();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Cria a pasta para os arquivos
                getDirFromSDCard();
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                Intent intent = new Intent(ActivitySplash.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        ConstraintLayout l=(ConstraintLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView v = (TextView) findViewById(R.id.titulo);
        v.clearAnimation();
        v.startAnimation(anim);
    }

    private File getDirFromSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory()
                    .getAbsoluteFile();
            File dir = new File(sdcard, "SysPalma" + File.separator + "database");
            if (!dir.exists())
                dir.mkdirs();
            return dir;
        } else {
            return null;
        }
    }
}