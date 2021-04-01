package com.innova.sharks.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.sharks.models.Quiz;

import java.util.List;

import static com.innova.sharks.models.Quiz.QUIZ_TABLE;


@Dao
public interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertQuiz(Quiz lesson);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertQuizzes(List<Quiz> lessons);

    @Query("DELETE FROM " + QUIZ_TABLE)
    void deleteAll();

    @Delete
    void deleteQuiz(Quiz lesson);

    @Query("SELECT * from " + QUIZ_TABLE + " WHERE lesson_id=:lesson_id ORDER BY RANDOM() LIMIT :number")
    LiveData<List<Quiz>> getQuizzes(int number, long lesson_id);


    @Query("SELECT * from " + QUIZ_TABLE + " ORDER BY RANDOM() LIMIT :number")
    LiveData<List<Quiz>> getQuizzes(int number);


    @Query("SELECT * from " + QUIZ_TABLE + " WHERE id >=:from AND lesson_id=:lesson_id ORDER BY RANDOM() LIMIT :to")
    List<Quiz> getPageQuizzes(long lesson_id, int from, int to);


    @Query("SELECT * from " + QUIZ_TABLE + " WHERE id >=:from  ORDER BY RANDOM() LIMIT :to")
    List<Quiz> getPageQuizzes(int from, int to);

    @Query("SELECT * FROM " + QUIZ_TABLE + " WHERE id = :id")
    LiveData<Quiz> getQuiz(long id);
}
