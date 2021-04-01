package com.innova.sharks;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.innova.sharks.dao.BookDao;
import com.innova.sharks.dao.CourseDao;
import com.innova.sharks.dao.LessonDao;
import com.innova.sharks.dao.ScholarshipDao;
import com.innova.sharks.dao.StudentDao;
import com.innova.sharks.dao.TeacherDao;
import com.innova.sharks.models.Course;
import com.innova.sharks.models.Lesson;
import com.innova.sharks.models.Scholarship;
import com.innova.sharks.models.Teacher;

import java.util.List;

public class DataRepository {
    public static final String TAG = "DataRepository";

    private final LiveData<List<Course>> mCourses;
    private final CourseDao mCourseDao;
    private final LessonDao mLessonDao;
    private final ScholarshipDao mScholarshipDao;
    private final TeacherDao mTeacherDao;
    private final StudentDao mStudentDao;
    private final BookDao mBookDao;

    public DataRepository(Context context) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(context);
        mCourseDao = db.CourseDao();
        mLessonDao = db.LessonDao();
        mScholarshipDao = db.ScholarshipDao();
        mCourses = mCourseDao.getCourses();
        mTeacherDao = db.TeacherDao();
        mStudentDao = db.StudentDao();
        mBookDao = db.BookDao();
    }

    // COURSES
    public LiveData<List<Course>> getAllCourses() {
        return mCourses;
    }

    public List<Course> getPageCourses(int from, int to) {
        return mCourseDao.getPageCourses(from, to);
    }

    public long insertCourse(Course course) {
        return mCourseDao.insertCourse(course);
    }

    // LESSONS
    public LiveData<List<Lesson>> getLessons(long courseId) {
        return mLessonDao.getLessons(courseId);
    }

    // SCHOLARSHIPS
    public LiveData<List<Scholarship>> getScholarships() {
        return mScholarshipDao.getScholarships();
    }

    // TEACHERS
    public List<Teacher> getPageTeachers(int from, int to) {
        return mTeacherDao.getPageTeachers(from, to);
    }

    //BOOKS

}
