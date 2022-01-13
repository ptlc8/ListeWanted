package fr.liste_wanted.data;

import org.json.JSONException;
import org.json.JSONObject;

import fr.liste_wanted.R;

public class Member {

    private String name;
    private String nickname;
    private String role;
    private String description;
    private int pictureResource;

    public Member(String name, String nickname, String role, String description, int pictureResource) {
        this.name = name;
        this.nickname = nickname;
        this.role = role;
        this.description = description;
        this.pictureResource = pictureResource;
    }

    public Member(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        this.nickname = json.getString("nickname");
        this.role = json.has("tag") ? json.getString("tag") : null;
        this.description = json.getString("desc");
        this.pictureResource = R.drawable.unset_picture;
    }

    public String getName() {
        return name;
    }

    public String getFirstname() {
        return name.substring(0, name.indexOf(' '));
    }

    public String getNickname() {
        return nickname;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }

    public int getPictureResource() {
        return pictureResource;
    }
}
