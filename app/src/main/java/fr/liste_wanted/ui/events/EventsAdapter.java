package fr.liste_wanted.ui.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Event;

public class EventsAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<Event> events;

    public EventsAdapter(Context context, List<Event> events) {
        this.events = events;
        inflater = LayoutInflater.from(context);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View eventView, ViewGroup viewGroup) {
        if (eventView==null)
            eventView = inflater.inflate(R.layout.view_event, viewGroup, false);
        Event event = events.get(i);
        ((TextView)eventView.findViewById(R.id.name)).setText(event.getName());
        ImageView imageView = eventView.findViewById(R.id.image_event);
        if (event.hasImageUrl()) Glide.with(eventView).load(event.getImageUrl()).into(imageView);
        else imageView.setImageResource(event.getDrawableResourceId(inflater.getContext()));
        long startTime = event.getStartTime();
        Locale locale = Locale.getDefault();
        ((TextView)eventView.findViewById(R.id.weekday)).setText(new SimpleDateFormat("E",locale).format(startTime));
        ((TextView)eventView.findViewById(R.id.day)).setText(new SimpleDateFormat("d",locale).format(startTime));
        ((TextView)eventView.findViewById(R.id.month_year)).setText(new SimpleDateFormat("LLLL yyyy",locale).format(startTime));
        ((TextView)eventView.findViewById(R.id.time)).setText(new SimpleDateFormat("H:mm",locale).format(startTime));
        ((TextView)eventView.findViewById(R.id.place)).setText(event.getPlace());
        ((TextView)eventView.findViewById(R.id.description)).setText(event.getShortDescription());
        return eventView;
    }
}
