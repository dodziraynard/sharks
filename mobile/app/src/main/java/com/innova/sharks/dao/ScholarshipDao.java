package com.innova.sharks.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.sharks.models.Scholarship;

import java.util.List;

import static com.innova.sharks.models.Scholarship.SCHOLARSHIP_TABLE;


@Dao
public interface ScholarshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertScholarship(Scholarship lesson);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertScholarships(List<Scholarship> lessons);

    @Query("DELETE FROM " + SCHOLARSHIP_TABLE)
    void deleteAll();

    @Delete
    void deleteScholarship(Scholarship lesson);

    @Query("SELECT * from " + SCHOLARSHIP_TABLE + " ORDER BY id ASC")
    LiveData<List<Scholarship>> getScholarships();

    @Query("SELECT * from " + SCHOLARSHIP_TABLE + " WHERE id >=:from ORDER BY id ASC LIMIT :to")
    List<Scholarship> getPageScholarships(int from, int to);

    @Query("SELECT * FROM " + SCHOLARSHIP_TABLE + " WHERE id = :id")
    LiveData<Scholarship> getScholarship(long id);
}
