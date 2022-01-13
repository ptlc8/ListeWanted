package fr.liste_wanted.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import fr.liste_wanted.HttpRequest;

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

    public static void sendNew(String author, String task, Runnable onSuccess, Consumer<IOException> onRequestError, Consumer<String> onServerError) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("author", author);
            jsonBody.put("task", task);
            HttpRequest.get(HttpRequest.API_URL+"/defis/add.php?author="+author+"&task="+task, response -> { // TODO: remplacer par un POST
                System.out.println("*"+response+"*");
                if (response.trim().equals("ok"))
                    onSuccess.run();
                else onServerError.accept(response);
            }, onRequestError);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
