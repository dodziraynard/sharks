package com.innova.sharks.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Quiz.QUIZ_TABLE)
public class Quiz implements Parcelable {
    @Ignore
    public static final String QUIZ_TABLE = "quiz_table";
    @Ignore
    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    @PrimaryKey(autoGenerate = true)
    long id;
    @SerializedName("lesson_id")
    @ColumnInfo(name = "lesson_id")
    long lessonId;
    String question;
    String options;
    int duration;
    @ColumnInfo(name = "correct_option")
    @SerializedName("str_correct_option")
    String correctOption;

    public Quiz(long lessonId, String question, String options, String correctOption, int duration) {
        this.lessonId = lessonId;
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
        this.duration = duration;
    }

    private Quiz(Parcel in) {
        id = in.readLong();
        lessonId = in.readLong();
        question = in.readString();
        options = in.readString();
        correctOption = in.readString();
        duration = in.readInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        Quiz lesson = (Quiz) obj;
        return lesson.getId() == getId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(lessonId);
        parcel.writeString(question);
        parcel.writeString(options);
        parcel.writeString(correctOption);
        parcel.writeInt(duration);
    }
}
