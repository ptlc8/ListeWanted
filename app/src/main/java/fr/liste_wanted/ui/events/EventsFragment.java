package fr.liste_wanted.ui.events;

import android.content.Intent;
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showEvent(Event event) {
        Intent intent = new Intent(getContext(),EventActivity.class);
        intent.putExtra("name", event.getName());
        intent.putExtra("description", event.getDescription());
        intent.putExtra("place", event.getPlace());
        intent.putExtra("startTime", event.getStartTime());
        intent.putExtra("endTime", event.getEndTime());
        startActivity(intent);
    }

    public static List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Salut je suis un évent", new Date().getTime()+360000, new Date().getTime()+80*60*1000, "Forest Crock", "Woof ! Ne t'inquiète pas, j'arrive bientôt ! Le pôle entreprise cherche un lieu, le pôle voyage nettoie, le pôle soirée prépare les cocktails, le pôle évent anime, le bureau encaisse, et le pôle com' t'en informe."));
        events.add(new Event("Un event passé 30 :/", 0, 300000, "Chépa", "Yeah !"));
        events.add(new Event("Un event passé :/", 0, 10000, "Chépa", "Yeah !"));
        events.add(new Event("Un event passé 100'000 :/", 0, 1000000000, "Chépa", "Yeah !"));
        events.add(new Event("Un event passé :/", 0, 10000, "Chépa", "Yeah !"));
        events.add(new Event("Un test passé :/", 0, 10000, "Chépa", "Yeah !"));
        events.add(new Event("Un event passé :/", 0, 1000, "Chépa", "Yeah !"));
        return events;
    }
}