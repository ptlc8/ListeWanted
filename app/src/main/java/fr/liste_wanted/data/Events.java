package fr.liste_wanted.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import fr.liste_wanted.HttpRequest;
import fr.liste_wanted.R;

public class Events extends ArrayList<Event> {

    public Events() {}

    public List<Event> getPastEvents() {
        List<Event> pastEvents = new ArrayList<>();
        long now = new Date().getTime();
        for (Event event : this) {
            if (event.getEndTime() < now)
                pastEvents.add(event);
        }
        pastEvents.sort((event1,event2) -> (int)(event2.getStartTime()-event1.getStartTime()));
        return pastEvents;
    }

    public List<Event> getComingEvents() {
        List<Event> comingEvents = new ArrayList<>();
        long now = new Date().getTime();
        for (Event event : this) {
            if (event.getEndTime() >= now)
                comingEvents.add(event);
        }
        comingEvents.sort((event1,event2) -> (int)(event1.getStartTime()-event2.getStartTime()));
        return comingEvents;
    }

    public void refresh(Consumer<Events> onResponse, Consumer<IOException> onRequestError, Consumer<String> onServerError) {
        HttpRequest.get(HttpRequest.API_URL+"/events/get.php", response -> {
            try {
                JSONObject json = new JSONObject(response);
                clear();
                JSONArray jsonEventsArray = json.getJSONArray("events");
                for (int i = 0; i < jsonEventsArray.length(); i++) {
                    add(new Event(jsonEventsArray.getJSONObject(i)));
                }
                onResponse.accept(this);
            } catch (JSONException e) {
                onServerError.accept(response);
                e.printStackTrace();
            }
        }, onRequestError);

    }

    public static Events getFromResources(Context context) {
        Events events = new Events();
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
