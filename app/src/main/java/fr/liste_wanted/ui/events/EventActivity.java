package fr.liste_wanted.ui.events;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Event;
import fr.liste_wanted.databinding.ActivityEventBinding;
import fr.liste_wanted.notifications.Notifications;

public class EventActivity extends AppCompatActivity {

    private ActivityEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int eventId = intent.getIntExtra("eventId", -1);
        String name = intent.hasExtra("name")?intent.getStringExtra("name"):"Évent";
        String description = intent.hasExtra("description") ? intent.getStringExtra("description") : "";
        String place = intent.hasExtra("place") ? intent.getStringExtra("place") : "";
        long startTime = intent.getLongExtra("startTime", -1L);
        long endTime = intent.getLongExtra("endTime", startTime);
        Event event = new Event(eventId, name, startTime, endTime, place, description);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        binding.toolbarLayout.setTitle(name);
        binding.textDescription.setText(description);
        binding.textPlace.setText(place);
        if (event.hasImageUrl())
            Glide.with(this).load(event.getImageUrl()).into(binding.toolbarImage);
        else
            binding.toolbarImage.setImageResource(event.getDrawableResourceId(this));
        if (startTime != -1) {
            System.out.println(startTime);
            System.out.println(endTime);
            if (startTime==endTime)
                binding.time.setText(getString(R.string.time, format("H",startTime), format("mm",startTime)));
            else binding.time.setText(getString(R.string.time_interval, format("H",startTime), format("mm",startTime), format("H",endTime), format("mm",endTime)));
            if (startTime+24*60*60*1000>endTime)
                binding.date.setText(getString(R.string.date, format("E",startTime), format("d",startTime), format("LLL yyyy",startTime)));
            else binding.date.setText(getString(R.string.date_interval, format("E",startTime), format("d",startTime), format("LLL",startTime), format("E",endTime), format("d",endTime), format("LLL",endTime)));

        }

        FloatingActionButton shareButton = binding.buttonShare;
        shareButton.setOnClickListener((view) -> {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_event_text, intent.getStringExtra("name")));
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
        });
        if (startTime < new Date().getTime())
            shareButton.setVisibility(View.INVISIBLE);

        /*findViewById(R.id.text_place).setOnClickListener(v->{
            Notifications.sendNotification(this, Notifications.createEventNotification(this, new Event(eventId, name, startTime, endTime, place, description)), eventId);
        });*/ // only for debug
    }

    private String format(String format, long time) {
        return new SimpleDateFormat(format,Locale.getDefault()).format(time);
    }

    public static Intent getShowEventIntent(Context context, Event event) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra("eventId", event.getId());
        intent.putExtra("name", event.getName());
        intent.putExtra("description", event.getDescription());
        intent.putExtra("place", event.getPlace());
        intent.putExtra("startTime", event.getStartTime());
        intent.putExtra("endTime", event.getEndTime());
        return intent;
    }

}