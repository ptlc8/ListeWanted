package fr.liste_wanted.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.function.Consumer;

import fr.liste_wanted.HttpRequest;
import fr.liste_wanted.R;

public class Partnerships extends ArrayList<Partnership> {

    public Partnerships() {}

    public void refresh(Consumer<Partnerships> onResponse, Consumer<IOException> onRequestError, Consumer<String> onServerError) {
        HttpRequest.get(HttpRequest.API_URL+"/partnerships/get.php", response -> {
            try {
                JSONObject json = new JSONObject(response);
                clear();
                JSONArray jsonEventsArray = json.getJSONArray("partnerships");
                for (int i = 0; i < jsonEventsArray.length(); i++)
                    add(new Partnership(jsonEventsArray.getJSONObject(i)));
                onResponse.accept(this);
            } catch (JSONException e) {
                onServerError.accept(response);
                e.printStackTrace();
            }
        }, onRequestError);

    }

    public static Partnerships getFromResources(Context context) {
        Partnerships partnerships = new Partnerships();
        String json = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.partnerships)));
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
                partnerships.add(new Partnership(jsonEvents.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return partnerships;
    }

}
