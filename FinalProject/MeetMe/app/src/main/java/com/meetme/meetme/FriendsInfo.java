package com.meetme.meetme;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by azolotushko on 1/18/2016.
 */
public class FriendsInfo {
    String id;
    String name;
    String pictureURL;
    Bitmap photo;

    public FriendsInfo(String id, String name, Bitmap photo) {
        this.id =id;
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
