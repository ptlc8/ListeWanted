package fr.liste_wanted.data;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class Partnership {

    private String name;
    private String description;
    private String color;

    public Partnership(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public Partnership(JSONObject json) throws JSONException  {
        this.name = json.getString("name");
        this.description = json.getString("description");
        this.color = json.getString("color");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public int getDrawableResourceId(Context context) {
        return context.getResources().getIdentifier("partnership_"+name.toLowerCase().replaceAll("[^a-z]","_"), "drawable", context.getPackageName());
    }
}
