package fr.liste_wanted.ui.events;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Defi;
import fr.liste_wanted.data.Event;
import fr.liste_wanted.data.Events;
import fr.liste_wanted.databinding.FragmentEventsBinding;
import fr.liste_wanted.notifications.Notifications;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;
    private Events events;
    private List<Event> pastEvents = new ArrayList<>();
    private List<Event> comingEvents = new ArrayList<>();
    private EventsAdapter pastEventsAdapter;
    private EventsAdapter comingEventsAdapter;
    private View connectionErrorView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        events = new Events();

        binding.listEvents.setAdapter(comingEventsAdapter = new EventsAdapter(getContext(), comingEvents));
        binding.listPastEvents.setAdapter(pastEventsAdapter = new EventsAdapter(getContext(), pastEvents));
        binding.listEvents.setOnItemClickListener((lView,view,i,l) -> showEvent(comingEvents.get(i)));
        binding.listPastEvents.setOnItemClickListener((lView,view,i,l) -> showEvent(pastEvents.get(i)));

        SwipeRefreshLayout swipe2refresh = binding.swipe2refresh;
        swipe2refresh.setOnRefreshListener(() -> refresh(() -> swipe2refresh.setRefreshing(false)));

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh(()->{});
    }

    public void refresh(Runnable onRefresh) {
        events.refresh(events -> {
            if (getActivity()==null) return;
            getActivity().runOnUiThread(() -> {
                binding.connectionError.getRoot().setVisibility(View.GONE);
                comingEvents = events.getComingEvents();
                pastEvents = events.getPastEvents();
                for (Event event : comingEvents) {
                    Notification notification = Notifications.createEventNotification(getContext(), event);
                    long delayInMs = event.getStartTime()-System.currentTimeMillis();
                    Notifications.scheduleNotification(getContext(), notification, event.getId(), event.getId()*2+1, delayInMs);
                }
                comingEventsAdapter.setEvents(comingEvents);
                pastEventsAdapter.setEvents(pastEvents);
            });
            onRefresh.run();
        }, ioe -> {
            if (getActivity()==null) return;
            getActivity().runOnUiThread(() -> {
                if (events.size() == 0)
                    binding.connectionError.getRoot().setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            });
            onRefresh.run();
        }, se -> {
            if (getActivity()==null) return;
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_LONG).show());
            onRefresh.run();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showEvent(Event event) {
        startActivity(EventActivity.getShowEventIntent(getContext(), event));
    }

}