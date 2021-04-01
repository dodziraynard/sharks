from django.urls import path, include   
from django.conf.urls import url
from rest_framework import routers                  
from knox import views as knox_views
from . import views
from . import auth_views


router = routers.DefaultRouter()  
router.register("courses", views.CoursesAPI, 'courses')
router.register("lessons", views.LessonsAPI, 'lessons')
router.register("quizzes", views.QuizzesAPI, 'quizzes')
router.register("scholarships", views.ScholarshipsAPI, 'scholarships')
router.register("books", views.BooksAPI, 'books')
router.register("teacher_profiles", views.TeacherProfilesAPI, 'teacher_profiles')
router.register("teacher_reviews", views.TeacherReviewsAPI, 'teacher_reviews')


urlpatterns = [    
    path('', include(router.urls)),

    # Authentication
    path('auth', include("knox.urls")),
    path('auth/register', auth_views.RegisterAPI.as_view()),
    path('auth/login', auth_views.LoginAPI.as_view()),
    path('auth/logout', knox_views.LogoutView.as_view()),
    path('auth/update_profile', auth_views.UpdateProfile.as_view()),
    path('auth/profile', auth_views.GetProfile.as_view()),
]