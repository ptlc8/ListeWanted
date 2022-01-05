package fr.liste_wanted.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Defi {

    private int number = -1;
    private String author = "???";
    private String task = "???";
    private boolean finished = false;
    private String evidenceLink = null;

    public Defi(int number, String author, String task) {
        this.number = number;
        this.author = author;
        this.task = task;
    }
    public Defi(int number, String author, String task, String evidenceLink) {
        this.number = number;
        this.author = author;
        this.task = task;
        finished = true;
        this.evidenceLink = evidenceLink;
    }
    public Defi(JSONObject json) throws JSONException {
        this.number = json.getInt("number");
        this.author = json.getString("author");
        this.task = json.getString("task");
        this.finished = json.getBoolean("finished");
        this.evidenceLink = json.has("evidenceLink")?json.getString("evidenceLink"):null;
    }

    public int getNumber() {
        return number;
    }

    public String getAuthor() {
        return author;
    }

    public String getTask() {
        return task;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getEvidenceLink() {
        return evidenceLink;
    }
}
