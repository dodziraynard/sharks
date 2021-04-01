package com.innova.sharks.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Scholarship.SCHOLARSHIP_TABLE)
public class Scholarship implements Parcelable {
    @Ignore
    public static final String SCHOLARSHIP_TABLE = "scholarship_table";
    @Ignore
    public static final Creator<Scholarship> CREATOR = new Creator<Scholarship>() {
        @Override
        public Scholarship createFromParcel(Parcel in) {
            return new Scholarship(in);
        }

        @Override
        public Scholarship[] newArray(int size) {
            return new Scholarship[size];
        }
    };

    @PrimaryKey()
    long id;
    String title;
    String description;
    String image;
    String url;
    String location;
    @SerializedName("opens_in_millis")
    long opens;
    @SerializedName("closes_in_millis")
    long closes;

    public Scholarship(long id, String title, String description, String image, String url, String location, long opens, long closes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.url = url;
        this.location = location;
        this.opens = opens;
        this.closes = closes;
    }

    private Scholarship(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        image = in.readString();
        url = in.readString();
        location = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getOpens() {
        return opens;
    }

    public void setOpens(long opens) {
        this.opens = opens;
    }

    public long getCloses() {
        return closes;
    }

    public void setCloses(long closes) {
        this.closes = closes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        Scholarship lesson = (Scholarship) obj;
        return lesson.getId() == getId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeString(url);
        parcel.writeString(location);
    }
}
