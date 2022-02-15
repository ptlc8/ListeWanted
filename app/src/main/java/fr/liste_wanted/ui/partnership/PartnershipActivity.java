package fr.liste_wanted.ui.partnership;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URI;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Partnership;
import fr.liste_wanted.databinding.ActivityPartnershipBinding;

public class PartnershipActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnership);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String color = intent.getStringExtra("color");
        String link = intent.getStringExtra("link");
        String imageUrl = intent.getStringExtra("imageUrl");
        Partnership partnership = new Partnership(name, description, color, imageUrl, link);

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        ((TextView)findViewById(R.id.text_name)).setText(partnership.getName());
        ((TextView)findViewById(R.id.text_description)).setText(partnership.getDescription());
        toolbarLayout.setBackgroundColor(Color.parseColor(partnership.getColor()));
        ImageView background = findViewById(R.id.toolbar_image);
        if (partnership.hasImageUrl()) Glide.with(this).load(partnership.getImageUrl()).into(background);
        else background.setImageResource(partnership.getDrawableResourceId(this));

        FloatingActionButton openButton = findViewById(R.id.button_open);
        if (partnership.getLink()!=null && !partnership.getLink().equals("null")) {
            openButton.setOnClickListener((view) ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(partnership.getLink())))
            );
        } else {
            openButton.setVisibility(View.INVISIBLE);
        }
    }

    public static Intent getShowEventIntent(Context context, Partnership partnership) {
        Intent intent = new Intent(context, PartnershipActivity.class);
        intent.putExtra("name", partnership.getName());
        intent.putExtra("description", partnership.getDescription());
        intent.putExtra("color", partnership.getColor());
        intent.putExtra("link", partnership.getLink());
        intent.putExtra("imageUrl", partnership.getImageUrl());
        return intent;
    }

}
