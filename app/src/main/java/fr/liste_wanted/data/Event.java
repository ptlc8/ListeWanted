package fr.liste_wanted.data;

import android.content.Context;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {

    private int id;
    private String name;
    private long startTime;
    private long endTime;
    private String place;
    private String description;
    private String imageUrl = null;

    public Event(int id, String name, long startTime, long endTime, String place, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Event(JSONObject json) throws JSONException {
        this.id = json.getInt("id");
        this.name = json.getString("name");
        this.startTime = json.getLong("start")*1000L;
        this.endTime = json.getLong("end")*1000L;
        this.place = json.getString("place");
        this.description = json.getString("description");
        if (json.has("imageUrl"))
            imageUrl = json.getString("imageUrl");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getPlace() {
        return place;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        if (description.length()<=128)
            return description;
        return description.substring(0, 128).replaceFirst(" [^ ]*$", "...");
    }

    public int getDrawableResourceId(Context context) {
        return context.getResources().getIdentifier("event_"+name.toLowerCase().replaceAll("[^a-z]","_"), "drawable", context.getPackageName());
    }

    public boolean hasImageUrl() {
        return imageUrl!=null;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
