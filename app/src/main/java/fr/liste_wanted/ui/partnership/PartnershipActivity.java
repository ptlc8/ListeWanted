package fr.liste_wanted.ui.partnership;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Partnership;

public class PartnershipActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnership);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String color = intent.getStringExtra("color");
        Partnership partnership = new Partnership(name, description, color);

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(partnership.getName());
        ((TextView)findViewById(R.id.text_description)).setText(partnership.getDescription());
        toolbarLayout.setBackgroundColor(Color.parseColor(partnership.getColor()));
        int backgroundResourceId = partnership.getDrawableResourceId(this);
        if (backgroundResourceId != 0)
            toolbarLayout.setBackgroundResource(backgroundResourceId);
    }

    public static Intent getShowEventIntent(Context context, Partnership partnership) {
        Intent intent = new Intent(context, PartnershipActivity.class);
        intent.putExtra("name", partnership.getName());
        intent.putExtra("description", partnership.getDescription());
        intent.putExtra("color", partnership.getColor());
        return intent;
    }

}
