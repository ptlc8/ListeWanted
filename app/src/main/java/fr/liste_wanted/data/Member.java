package fr.liste_wanted.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.liste_wanted.R;

public class Member {

    private String name;
    private String nickname;
    private String role;
    private String description;

    private static transient Map<Integer, Bitmap> bitmaps = new HashMap<>();

    public Member(String name, String nickname, String role, String description) {
        this.name = name;
        this.nickname = nickname;
        this.role = role;
        this.description = description;
    }

    public Member(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        this.nickname = json.getString("nickname");
        this.role = json.has("tag") ? json.getString("tag") : null;
        this.description = json.getString("desc");
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

    public int getPictureResourceId(Context context) {
        int pictureResource = context.getResources().getIdentifier("member_"+this.nickname.toLowerCase().replaceAll("[^a-z0-9]","_"), "drawable", context.getPackageName());
        if (pictureResource==0) return R.drawable.unset_picture;
        return pictureResource;
    }

    public Bitmap getPictureBitmap(Context context) {
        int resourceId = getPictureResourceId(context);
        if (bitmaps.get(resourceId) != null)
            return bitmaps.get(resourceId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (resourceId != R.drawable.unset_picture)
            options.inSampleSize = 4;
        options.inScaled = true;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        bitmaps.put(resourceId, bitmap);
        return bitmap;
    }
}
