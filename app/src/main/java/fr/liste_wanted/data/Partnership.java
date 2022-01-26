package fr.liste_wanted.data;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class Partnership {

    private String name;
    private String description;

    public Partnership(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Partnership(JSONObject json) throws JSONException  {
        this.name = json.getString("name");
        this.description = json.getString("description");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDrawableResourceId(Context context) {
        return context.getResources().getIdentifier("partnership_"+name.toLowerCase().replaceAll("[^a-z]","_"), "drawable", context.getPackageName());
    }
}
