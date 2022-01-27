package fr.liste_wanted.ui.events;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Event;
import fr.liste_wanted.databinding.FragmentEventsBinding;
import fr.liste_wanted.notifications.Notifications;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Event> events = getEventsFromResources(requireContext());
        List<Event> comingEvents = new ArrayList<>();
        List<Event> pastEvents = new ArrayList<>();
        long now = new Date().getTime();
        for (Event event : events) {
            if (event.getEndTime() < now)
                pastEvents.add(event);
            else
                comingEvents.add(event);
        }
        comingEvents.sort((event1,event2) -> (int)(event1.getStartTime()-event2.getStartTime()));
        pastEvents.sort((event1,event2) -> (int)(event2.getStartTime()-event1.getStartTime()));

        binding.listEvents.setAdapter(new EventsAdapter(getContext(), comingEvents));
        binding.listPastEvents.setAdapter(new EventsAdapter(getContext(), pastEvents));
        binding.listEvents.setOnItemClickListener((lView,view,i,l) -> showEvent(comingEvents.get(i)));
        binding.listPastEvents.setOnItemClickListener((lView,view,i,l) -> showEvent(pastEvents.get(i)));

        for (Event event : comingEvents) {
            Notification notification = Notifications.createEventNotification(getContext(), event);
            long delayInMs = event.getStartTime()-System.currentTimeMillis();
            Notifications.scheduleNotification(getContext(), notification, event.getId(), event.getId()*2+1, delayInMs);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showEvent(Event event) {
        startActivity(EventActivity.getShowEventIntent(getContext(), event));
    }

    public static List<Event> getEventsFromResources(Context context) {
        List<Event> events = new ArrayList<>();
        String json = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.events)));
            String line;
            while ((line = reader.readLine()) != null)
                json += line + "\n";
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonEvents = new JSONArray(json);
            for (int i = 0; i < jsonEvents.length(); i++)
                events.add(new Event(jsonEvents.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return events;
    }
}