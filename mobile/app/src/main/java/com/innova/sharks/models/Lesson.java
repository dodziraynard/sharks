package com.innova.sharks.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Lesson.LESSON_TABLE)
public class Lesson implements Parcelable {
    @Ignore
    public static final String LESSON_TABLE = "lesson_table";
    @Ignore
    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };
    @PrimaryKey()
    long id;
    String title;
    String description;
    String note;
    String slide;
    String image;
    String video;
    @SerializedName("has_quizzes")
    @ColumnInfo(name = "has_quizzes")
    int hasQuizzes;
    @SerializedName("course")
    @ColumnInfo(name = "course_id")
    long courseId;
    String quizScore;

    public Lesson(long id, String title, String description, String note, String slide, String image, String video, int hasQuizzes, long courseId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.note = note;
        this.slide = slide;
        this.image = image;
        this.video = video;
        this.hasQuizzes = hasQuizzes;
        this.courseId = courseId;
    }

    protected Lesson(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        note = in.readString();
        slide = in.readString();
        image = in.readString();
        video = in.readString();
        hasQuizzes = in.readInt();
        courseId = in.readLong();
        quizScore = in.readString();
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSlide() {
        return slide;
    }

    public void setSlide(String slide) {
        this.slide = slide;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getHasQuizzes() {
        return hasQuizzes;
    }

    public void setHasQuizzes(int hasQuizzes) {
        this.hasQuizzes = hasQuizzes;
    }

    public String getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(String quizScore) {
        this.quizScore = quizScore;
    }

    public boolean hasQuizzes() {
        return hasQuizzes == 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        Lesson lesson = (Lesson) obj;
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
        parcel.writeString(note);
        parcel.writeString(slide);
        parcel.writeString(image);
        parcel.writeString(video);
        parcel.writeInt(hasQuizzes);
        parcel.writeLong(courseId);
        parcel.writeString(quizScore);
    }
}
