package com.innova.sharks.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Student.STUDENT_TABLE)
public class Student implements Parcelable {
    @Ignore
    public static final String STUDENT_TABLE = "student_table";
    @Ignore
    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
    @PrimaryKey()
    long id;
    @SerializedName("full_name")
    String name;
    @SerializedName("background_image")
    @ColumnInfo(name = "background_image")
    String backgroundImage;
    String image;
    String username;
    int level;
    String contact;
    String location;
    String education;
    String introduction;

    public Student(long id, String name, String backgroundImage, String image, String username, int level, String contact, String location, String education, String introduction) {
        this.id = id;
        this.name = name;
        this.backgroundImage = backgroundImage;
        this.image = image;
        this.username = username;
        this.level = level;
        this.contact = contact;
        this.location = location;
        this.education = education;
        this.introduction = introduction;
    }

    protected Student(Parcel in) {
        id = in.readLong();
        name = in.readString();
        backgroundImage = in.readString();
        image = in.readString();
        username = in.readString();
        level = in.readInt();
        contact = in.readString();
        location = in.readString();
        education = in.readString();
        introduction = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(backgroundImage);
        parcel.writeString(image);
        parcel.writeString(username);
        parcel.writeInt(level);
        parcel.writeString(contact);
        parcel.writeString(location);
        parcel.writeString(education);
        parcel.writeString(introduction);
    }
}
