package com.innova.sharks.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.sharks.models.Student;

import java.util.List;

import static com.innova.sharks.models.Student.STUDENT_TABLE;


@Dao
public interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertStudent(Student student);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertStudents(List<Student> students);

    @Query("DELETE FROM " + STUDENT_TABLE)
    void deleteAll();

    @Delete
    void deleteStudent(Student student);

    @Query("SELECT * from " + STUDENT_TABLE + " ORDER BY id ASC")
    LiveData<List<Student>> getStudents();

    @Query("SELECT * from " + STUDENT_TABLE + " WHERE id >=:from ORDER BY id ASC LIMIT :to")
    List<Student> getPageStudents(int from, int to);

    @Query("SELECT * FROM " + STUDENT_TABLE + " WHERE id = :id")
    LiveData<Student> getStudent(long id);
}
