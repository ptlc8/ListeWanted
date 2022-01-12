package fr.liste_wanted.ui.home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pole extends ArrayList<Member> {

    private String name;

    public Pole(String name) {
        this.name = name;
    }

    public Pole(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        JSONArray jsonMembers = json.getJSONArray("members");
        for (int i = 0; i < jsonMembers.length(); i++)
            add(new Member(jsonMembers.getJSONObject(i)));
    }

    public String getName() {
        return name;
    }
}
