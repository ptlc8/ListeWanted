package fr.liste_wanted.ui.events;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.liste_wanted.data.Event;
import fr.liste_wanted.databinding.FragmentEventsBinding;
import fr.liste_wanted.notifications.Notifications;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Event> events = getEvents();
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

    public static List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event(8,"Un évent dans 20 secondes", new Date().getTime()+20*1000, new Date().getTime()+80*60*1000, "Forest Crock", "Woof ! Ne t'inquiète pas, j'arrive bientôt ! Le pôle entreprise cherche un lieu, le pôle voyage nettoie, le pôle soirée prépare les cocktails, le pôle évent anime, le bureau encaisse, et le pôle com' t'en informe."));
        events.add(new Event(5,"Salut je suis un évent", new Date().getTime()+6*60*1000, new Date().getTime()+80*60*1000, "Forest Crock", "Woof ! Ne t'inquiète pas, j'arrive bientôt ! Le pôle entreprise cherche un lieu, le pôle voyage nettoie, le pôle soirée prépare les cocktails, le pôle évent anime, le bureau encaisse, et le pôle com' t'en informe."));
        events.add(new Event(1,"Un event passé 30 :/", 0, 300000, "Chépa", "Yeah !"));
        events.add(new Event(6,"Un event passé :/", 0, 10000, "Chépa", "Yeah !"));
        events.add(new Event(0,"Un event passé 100'000 :/", 0, 1000000000, "Chépa", "Yeah !"));
        events.add(new Event(4,"Un event passé :/", 0, 10000, "Chépa", "Yeah !"));
        events.add(new Event(3,"Un test passé :/", 0, 10000, "Chépa", "Yeah !"));
        events.add(new Event(2,"Un event passé :/", 0, 1000, "Chépa", "Yeah !"));
        return events;
    }
}