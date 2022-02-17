package fr.liste_wanted.data;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class Partnership {

    private String name;
    private String description;
    private String color = "#ffffff";
    private String link = null;
    private String imageUrl = null;

    public Partnership(String name, String description, String color, String imageUrl) {
        this(name, description, color, imageUrl, null);
    }
    public Partnership(String name, String description, String color, String imageUrl, String link) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.link = link;
        this.imageUrl = imageUrl;
    }

    public Partnership(JSONObject json) throws JSONException  {
        this.name = json.getString("name");
        this.description = json.getString("description");
        if (json.has("color"))
            this.color = json.getString("color");
        if (json.has("link"))
            this.link = json.getString("link");
        if (json.has("imageUrl"))
            this.imageUrl = json.getString("imageUrl");
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

    public String getLink() {
        return link;
    }

    public int getDrawableResourceId(Context context) {
        return context.getResources().getIdentifier("partnership_"+name.toLowerCase().replaceAll("[^a-z]","_"), "drawable", context.getPackageName());
    }

    public boolean hasImageUrl() {
        return imageUrl!=null;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
