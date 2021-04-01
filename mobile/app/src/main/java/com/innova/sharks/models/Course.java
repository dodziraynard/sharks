package com.innova.sharks.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Course.COURSE_TABLE)
public class Course implements Parcelable {
    @Ignore
    public static final String COURSE_TABLE = "course_table";
    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
    @PrimaryKey()
    long id;
    String title;
    @SerializedName("short_description")
    @ColumnInfo(name = "short_description")
    String shortDescription;
    @SerializedName("long_description")
    @ColumnInfo(name = "long_description")
    String longDescription;
    String website;
    String resources;
    int level;
    String image;
    String date;
    @ColumnInfo(name = "is_favourite")
    boolean isFavourite;

    public Course(long id, String title, String shortDescription, String longDescription, String website, String resources, int level, String image, String date) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.website = website;
        this.resources = resources;
        this.level = level;
        this.image = image;
        this.date = date;
        this.isFavourite = false;
    }

    protected Course(Parcel in) {
        id = in.readLong();
        title = in.readString();
        shortDescription = in.readString();
        longDescription = in.readString();
        website = in.readString();
        resources = in.readString();
        level = in.readInt();
        image = in.readString();
        date = in.readString();
        isFavourite = in.readByte() != 0;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        Course lesson = (Course) obj;
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
        parcel.writeString(shortDescription);
        parcel.writeString(longDescription);
        parcel.writeString(website);
        parcel.writeString(resources);
        parcel.writeInt(level);
        parcel.writeString(image);
        parcel.writeString(date);
        parcel.writeByte((byte) (isFavourite ? 1 : 0));
    }
}
