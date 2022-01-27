package fr.liste_wanted.data;

import android.content.Context;
import android.widget.Toast;

import com.hcaptcha.sdk.HCaptcha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import fr.liste_wanted.HttpRequest;

public class Defis {

    public static final String HCAPTCHA_API_SITE_KEY = "a3112f0a-e78b-4eef-a552-4bed7b61012a";

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

    public static void sendNew(Context context, String author, String task, Runnable onSuccess, Consumer<IOException> onRequestError, Consumer<String> onServerError) {
        HCaptcha.getClient(context).verifyWithHCaptcha(HCAPTCHA_API_SITE_KEY)
            .addOnSuccessListener(hcaptchaResponse -> {
                try {
                    HttpRequest.get(HttpRequest.API_URL + "/defis/add.php?author=" + URLEncoder.encode(author, "UTF-8") + "&task=" + URLEncoder.encode(task,"UTF-8") + "&token=" + URLEncoder.encode(hcaptchaResponse.getTokenResult(),"UTF-8"), response -> {
                        if (response.trim().equals("success"))
                            onSuccess.run();
                        else onServerError.accept(response);
                    }, onRequestError);
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(context, "Unsurported encoding  error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(context, "Captcha error "+e.getStatusCode()+": "+e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}
