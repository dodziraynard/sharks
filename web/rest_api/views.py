from django.shortcuts import get_object_or_404
from rest_framework.viewsets import ModelViewSet     
from rest_framework import generics, permissions, status
from rest_framework.response import Response
from portal.models import *
from django.db.models import Q
from . serializers import *
from random import shuffle


class CoursesAPI(ModelViewSet):
    """
    Perform CRUD operation on course model.
    """
    serializer_class = CourseSerializer    
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]     

    def get_queryset(self):
        request = self.request
        query = request.GET.get("query")
        level = request.GET.get("level")
        courses  = Course.objects.filter(active=True).order_by("id")
        if level:
            courses = Course.objects.filter(active=True, level=(level)).order_by("id")
        elif query:
            courses = courses.filter(
                                Q(title__icontains=query) |
                                Q(short_description__icontains=query) |
                                Q(level__icontains=query) |
                                Q(long_description__icontains=query)
                            ) 
        
        return courses
    
    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


class LessonsAPI(ModelViewSet):
    """
    Perform CRUD operation on lesson model.
    """
    serializer_class = LessonSerializer       
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]     

    def get_queryset(self):
        request = self.request
        query = request.GET.get("query")
        course_id = request.GET.get("course_id")

        lessons  = Lesson.objects.all().order_by("title")
        if query:
            lessons = lessons.filter(
                                Q(title__icontains=query) |
                                Q(description__icontains=query)
                            )
        if course_id:
            course = Course.objects.filter(id=course_id).first()
            if course:
                lessons = lessons.filter(course=course)
            else:
                return []
        return lessons

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)



class QuizzesAPI(ModelViewSet):
    """
    Perform CRUD operation on quiz model.
    """
    serializer_class = QuizSerializer       
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]     

    def get_queryset(self):
        request = self.request
        lesson_id = request.GET.get("lesson_id")

        quizzes  = Quiz.objects.all().order_by('?')
        if lesson_id:
            lesson = Lesson.objects.filter(id=lesson_id).first()
            if lesson:
                quizzes = quizzes.filter(lesson=lesson)
            else:
                return []
        return quizzes

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


class ScholarshipsAPI(ModelViewSet):
    """
    Perform CRUD operation on scholarship model.
    """
    serializer_class = ScholarshipSerializer       
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]     

    def get_queryset(self):
        request = self.request
        query = request.GET.get("query")

        scholarships  = Scholarship.objects.filter(active=True)
        if query:
            scholarships = scholarships.filter(
                                Q(title__icontains=query) |
                                Q(description__icontains=query)
                            )
        return scholarships

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


class BooksAPI(ModelViewSet):
    """
    Perform CRUD operation on book model.
    """
    serializer_class = BookSerializer       
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]     

    def get_queryset(self):
        request = self.request
        query = request.GET.get("query")

        books  = Book.objects.all()
        if query:
            books = books.filter(
                                Q(title__icontains=query) |
                                Q(description__icontains=query)
                            )
        return books

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)
 

class TeacherProfilesAPI(ModelViewSet):
    """
    Perform CRUD operation on teacher profile model.
    """
    serializer_class = TeacherProfileSerializer       
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]     

    def get_queryset(self):
        request = self.request
        query = request.GET.get("query")

        profiles  = TeacherProfile.objects.all()
        if query:
            profiles = profiles.filter(
                                Q(full_name__icontains=query) |
                                Q(introduction__icontains=query) |
                                Q(header__icontains=query)
                            )
        return profiles

    # def list(self, request, *args, **kwargs):
    #     ret = super(TeacherProfilesAPI, self).list(request)
    #     return Response({'teacher_profiles': ret.data})

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


class TeacherReviewsAPI(ModelViewSet):
    """
    Perform CRUD operation on teacher review model.
    """
    serializer_class = TeacherReviewSerializer 
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]     

    def get_queryset(self):
        request = self.request
        query = request.GET.get("query")
        teacher_id = request.GET.get("teacher_id")

        profiles  = TeacherReview.objects.filter(teacher_id=teacher_id)
        if query:
            profiles = profiles.filter(message__icontains=query)
        return profiles

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)