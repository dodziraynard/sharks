package com.innova.sharks.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.sharks.models.Lesson;

import java.util.List;

import static com.innova.sharks.models.Lesson.LESSON_TABLE;


@Dao
public interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLesson(Lesson lesson);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertLessons(List<Lesson> lessons);

    @Query("DELETE FROM " + LESSON_TABLE)
    void deleteAll();

    @Delete
    void deleteLesson(Lesson lesson);

    @Query("SELECT * from " + LESSON_TABLE + " WHERE course_id=:course_id ORDER BY id ASC")
    LiveData<List<Lesson>> getLessons(long course_id);

    @Query("SELECT * from " + LESSON_TABLE + " ORDER BY id DESC")
    LiveData<List<Lesson>> getLessons();

    @Query("SELECT * from " + LESSON_TABLE + " WHERE id >=:from AND course_id=:course_id  ORDER BY id ASC LIMIT :to")
    List<Lesson> getPageLessons(long course_id, int from, int to);

    @Query("SELECT * FROM " + LESSON_TABLE + " WHERE id = :id")
    LiveData<Lesson> getLesson(long id);
}
