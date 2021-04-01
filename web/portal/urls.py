from django.urls import path
from . import views


app_name = "portal"
urlpatterns = [
    path('', views.courses, name='courses'),
    path('courses', views.courses, name='courses'),
    path('course/create-update', views.course_create_update, name='course_create_update'),
    path('course/delete', views.course_delete, name='course_delete'),

    path('<int:course_id>/course-lessons',  views.course_lessons, name='course_lessons'),
    path('<int:course_id>/lesson/create-update', views.lesson_create_update, name='lesson_create_update'),
    path('lessons',  views.lessons, name='lessons'),
    path('lesson/delete', views.lesson_delete, name='lesson_delete'),

    path('<int:lesson_id>/quizzes',  views.lesson_quizzes, name='lesson_quizzes'),
    path('<int:lesson_id>/quiz/create-update', views.quiz_create_update, name='quiz_create_update'),
    path('quiz/delete', views.quiz_delete, name='quiz_delete'),

    path('quiz/option/delete/<int:id>',  views.delete_option, name='delete_option'),
    path('quiz/<int:quiz_id>/add_option',  views.add_option, name='add_option'),

    path('quizzes', views.quizzes, name='quizzes'),

    path('scholarships', views.scholarships, name='scholarships'),
    path('scholarship/create-update', views.scholarship_create_update, name='scholarship_create_update'),
    path('scholarship/delete', views.scholarship_delete, name='scholarship_delete'),

    path('books', views.books, name='books'),
    path('book/create-update', views.book_create_update, name='book_create_update'),
    path('book/delete', views.book_delete, name='book_delete'),

]