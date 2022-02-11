package fr.liste_wanted.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Timer;
import java.util.TimerTask;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Pole;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Glide.with(this).asGif()
            .load(R.drawable.ic_wanted_logo_animated)
            .listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    startApp();
                    return false;
                }
                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    resource.setLoopCount(1);
                    resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            startApp();
                            super.onAnimationEnd(drawable);
                        }
                    });
                    return false;
                }
            })
            .into(((ImageView)findViewById(R.id.imageView)));

        // Pré-chargement des photos de membres
        new Thread(() -> {
            Pole.getPolesFromJSON(StartActivity.this).forEach(p -> p.forEach(m -> m.getPictureBitmap(StartActivity.this)));
        }).start();

        // Au cas où le gif ne fonctionne pas
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startApp();
            }
        }, 4200);
    }

    private boolean hasStart = false;
    private void startApp() {
        if (hasStart) return;
        hasStart = true;
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}