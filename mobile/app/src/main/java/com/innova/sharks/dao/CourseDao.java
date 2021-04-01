package com.innova.sharks.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.sharks.models.Course;

import java.util.List;

import static com.innova.sharks.models.Course.COURSE_TABLE;


@Dao
public interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCourse(Course course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertCourses(List<Course> courses);

    @Query("DELETE FROM " + COURSE_TABLE)
    void deleteAll();

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * from " + COURSE_TABLE + " ORDER BY id ASC")
    LiveData<List<Course>> getCourses();

    @Query("SELECT * from " + COURSE_TABLE + " WHERE id >=:from  ORDER BY id ASC LIMIT :to")
    List<Course> getPageCourses(int from, int to);


    @Query("SELECT * FROM " + COURSE_TABLE + " WHERE id = :id")
    LiveData<Course> getCourse(long id);
}
