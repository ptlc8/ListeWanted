package fr.liste_wanted.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.liste_wanted.R;

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

    public static List<Pole> getPolesFromJSON(Context context) {
        List<Pole> poles = new ArrayList<>();
        String json = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.poles)));
            String line;
            while ((line = reader.readLine()) != null)
                json += line + "\n";
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonPoles = new JSONArray(json);
            for (int i = 0; i < jsonPoles.length(); i++)
                poles.add(new Pole(jsonPoles.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return poles;
    }

}
