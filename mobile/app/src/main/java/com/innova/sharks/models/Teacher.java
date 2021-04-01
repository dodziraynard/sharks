package com.innova.sharks.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Teacher.TEACHER_TABLE)
public class Teacher implements Parcelable {
    @Ignore
    public static final String TEACHER_TABLE = "teacher_table";
    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
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
    String about;
    @SerializedName("show_contact")
    @ColumnInfo(name = "show_contact")
    int showContact;
    String contact;
    String education;
    float rating;
    int reviews;
    String header;
    String location;
    String introduction;
    String email;


    public Teacher(long id, String name, String backgroundImage, String image, String about, int showContact, String contact, String education, float rating, int reviews, String header, String location, String introduction, String email) {
        this.id = id;
        this.name = name;
        this.backgroundImage = backgroundImage;
        this.image = image;
        this.about = about;
        this.showContact = showContact;
        this.contact = contact;
        this.education = education;
        this.rating = rating;
        this.reviews = reviews;
        this.header = header;
        this.location = location;
        this.introduction = introduction;
        this.email = email;
    }

    protected Teacher(Parcel in) {
        id = in.readLong();
        name = in.readString();
        backgroundImage = in.readString();
        image = in.readString();
        about = in.readString();
        showContact = in.readInt();
        contact = in.readString();
        education = in.readString();
        rating = in.readFloat();
        reviews = in.readInt();
        header = in.readString();
        location = in.readString();
        introduction = in.readString();
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(backgroundImage);
        dest.writeString(image);
        dest.writeString(about);
        dest.writeInt(showContact);
        dest.writeString(contact);
        dest.writeString(education);
        dest.writeFloat(rating);
        dest.writeInt(reviews);
        dest.writeString(header);
        dest.writeString(location);
        dest.writeString(introduction);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getShowContact() {
        return showContact;
    }

    public void setShowContact(int showContact) {
        this.showContact = showContact;
    }

    public boolean canShowContact() {
        return showContact == 1;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        Teacher lesson = (Teacher) obj;
        return lesson.getId() == getId();
    }
}
