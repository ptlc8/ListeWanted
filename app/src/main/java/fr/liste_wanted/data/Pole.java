package fr.liste_wanted.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.liste_wanted.data.Member;

public class Pole extends ArrayList<Member> {

    private String name;

    public Pole(String name) {
        this.name = name;
    }

    public Pole(JSONObject json, Context context) throws JSONException {
        this.name = json.getString("name");
        JSONArray jsonMembers = json.getJSONArray("members");
        for (int i = 0; i < jsonMembers.length(); i++)
            add(new Member(jsonMembers.getJSONObject(i), context));
    }

    public String getName() {
        return name;
    }
}
