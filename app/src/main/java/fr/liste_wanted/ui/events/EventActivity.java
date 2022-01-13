package fr.liste_wanted.ui.events;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.liste_wanted.R;
import fr.liste_wanted.databinding.ActivityEventBinding;

public class EventActivity extends AppCompatActivity {

    private ActivityEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        binding.toolbarLayout.setTitle(intent.hasExtra("name")?intent.getStringExtra("name"):"Évent");
        if (intent.hasExtra("description"))
            binding.textDescription.setText(intent.getStringExtra("description"));
        if (intent.hasExtra("place"))
            binding.textPlace.setText(intent.getStringExtra("place"));
        long startTime = intent.getLongExtra("startTime", -1L);
        long endTime = intent.getLongExtra("endTime", startTime);
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
    }

    private String format(String format, long time) {
        return new SimpleDateFormat(format,Locale.getDefault()).format(time);
    }
}