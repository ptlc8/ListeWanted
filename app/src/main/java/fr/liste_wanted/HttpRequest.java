package fr.liste_wanted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public abstract class HttpRequest {

    public static String API_URL = "http://cd.codermdy.com/api-test";

    public static void get(String urlString, Consumer<String> onResponse, Consumer<IOException> onError) {
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000/* ms */);
                urlConnection.setConnectTimeout(15000/* ms */);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null)
                    sb.append(line + "\n");
                br.close();

                onResponse.accept(sb.toString());
            } catch (IOException e) {
                onError.accept(e);
            }
        }).start();
    }

}
