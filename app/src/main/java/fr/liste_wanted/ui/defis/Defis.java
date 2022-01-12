package fr.liste_wanted.ui.defis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import fr.liste_wanted.HttpRequest;
import fr.liste_wanted.data.Defi;

public class Defis {

    private boolean canSubmit = false;
    private final List<Defi> defis = new ArrayList<>();

    public Defis() {}

    public void refresh(Consumer<Defis> onResponse, Consumer<IOException> onRequestError, Consumer<String> onServerError) {
        HttpRequest.get(HttpRequest.API_URL+"/defis/get.php", response -> {
            try {
                JSONObject json = new JSONObject(response);
                canSubmit = json.getBoolean("canSubmit");
                defis.clear();
                JSONArray jsonDefisArray = json.getJSONArray("defis");
                for (int i = 0; i < jsonDefisArray.length(); i++) {
                    defis.add(new Defi(jsonDefisArray.getJSONObject(i)));
                }
                onResponse.accept(this);
            } catch (JSONException e) {
                onServerError.accept(response);
                e.printStackTrace();
            }
        }, onRequestError);

    }

    public List<Defi> getDefis() {
        return defis;
    }

    public boolean canSubmit() {
        return canSubmit;
    }
}
