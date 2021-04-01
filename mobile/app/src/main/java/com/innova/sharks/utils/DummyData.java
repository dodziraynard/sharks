package com.innova.sharks.utils;

import com.innova.sharks.models.Course;
import com.innova.sharks.models.Lesson;
import com.innova.sharks.models.Quiz;
import com.innova.sharks.models.Scholarship;
import com.innova.sharks.models.Teacher;

import java.util.ArrayList;
import java.util.List;

public class DummyData {
    private static final String TAG = "DummyData";

    public static List<Course> getDummyCourses() {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Course lesson = new Course(i,
                    "Course " + i,
                    "short description" + i,
                    "Some really really long description",
                    "",
                    "",
                    1,
                    "",
                    "April 21, 2021"
            );
            courses.add(lesson);
        }
        return courses;
    }

    public static List<Lesson> getDummyLessons() {
        List<Lesson> lessons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Lesson lesson = new Lesson(i,
                    "Lesson " + i,
                    "Note goes here",
                    "short description" + i,
                    "",
                    "",
                    "https://firebasestorage.googleapis.com/v0/b/sharks-307902.appspot.com/o/When%20Mathematics%20Meets%20Engineering.mp4?alt=media&token=ffd626fd-f942-4053-bc76-4a2ae4bec52e",
                    1,
                    1
            );
            lessons.add(lesson);
        }
        return lessons;
    }

    public static List<Scholarship> getDummyScholarships() {
        List<Scholarship> scholarships = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Scholarship scholarship = new Scholarship(i,
                    "Scholarship " + i,
                    "short description" + i,
                    "",
                    "https://google.com",
                    "University of Ghana",
                    1993939393L,
                    19939393393L
            );
            scholarships.add(scholarship);
        }
        return scholarships;
    }

    public static List<Quiz> getDummyQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Quiz scholarship = new Quiz(
                    i,
                    "Really really long question " + i,
                    "Really\n really\n long \nquestion",
                    "Really",
                    5 + i
            );
            quizzes.add(scholarship);
        }
        return quizzes;
    }

    public static List<Teacher> getDummyTeachers() {
        List<Teacher> quizzes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Teacher scholarship = new Teacher(
                    i,
                    "Raynard Dodzi " + i,
                    "",
                    "Really",
                    "This is about me",
                    1,
                    "0249626071",
                    "University of Ghana",
                    4.5f,
                    5,
                    "Professional Mathematics Teacher",
                    "Akatsi",
                    "I've been teaching Mathematics for 5 years now.",
                    "dodzireynard@gmail.com"
            );
            quizzes.add(scholarship);
        }
        return quizzes;
    }
}