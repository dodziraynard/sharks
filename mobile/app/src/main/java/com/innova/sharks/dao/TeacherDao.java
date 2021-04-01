package com.innova.sharks.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.sharks.models.Teacher;

import java.util.List;

import static com.innova.sharks.models.Teacher.TEACHER_TABLE;


@Dao
public interface TeacherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTeacher(Teacher teacher);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTeachers(List<Teacher> teachers);

    @Query("DELETE FROM " + TEACHER_TABLE)
    void deleteAll();

    @Delete
    void deleteTeacher(Teacher teacher);

    @Query("SELECT * from " + TEACHER_TABLE + " ORDER BY id ASC")
    LiveData<List<Teacher>> getTeachers();

    @Query("SELECT * from " + TEACHER_TABLE + " WHERE id >=:from ORDER BY id ASC LIMIT :to")
    List<Teacher> getPageTeachers(int from, int to);

    @Query("SELECT * FROM " + TEACHER_TABLE + " WHERE id = :id")
    LiveData<Teacher> getTeacher(long id);
}
