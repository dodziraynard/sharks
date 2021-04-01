package com.innova.sharks.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = Book.BOOK_TABLE)
public class Book implements Parcelable {
    @Ignore
    public static final String BOOK_TABLE = "book_table";
    @Ignore
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    @PrimaryKey()
    long id;
    String title;
    String description;
    String image;
    String file;
    String author;

    public Book(long id, String title, String description, String image, String file, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.file = file;
        this.author = author;
    }

    protected Book(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        image = in.readString();
        file = in.readString();
        author = in.readString();
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                Objects.equals(title, book.title) &&
                Objects.equals(description, book.description) &&
                Objects.equals(image, book.image) &&
                Objects.equals(author, book.author) &&
                Objects.equals(file, book.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, image, file, author);
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
        parcel.writeString(file);
        parcel.writeString(author);
    }
}
