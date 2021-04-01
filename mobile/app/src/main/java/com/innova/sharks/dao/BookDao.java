package com.innova.sharks.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.sharks.models.Book;

import java.util.List;

import static com.innova.sharks.models.Book.BOOK_TABLE;


@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBook(Book lesson);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertBooks(List<Book> lessons);

    @Query("DELETE FROM " + BOOK_TABLE)
    void deleteAll();

    @Delete
    void deleteBook(Book lesson);

    @Query("SELECT * from " + BOOK_TABLE + " ORDER BY id ASC")
    LiveData<List<Book>> getBooks();

    @Query("SELECT * from " + BOOK_TABLE + " WHERE id >=:from ORDER BY id ASC LIMIT :to")
    List<Book> getPageBooks(int from, int to);

    @Query("SELECT * FROM " + BOOK_TABLE + " WHERE id = :id")
    LiveData<Book> getBook(long id);
}
