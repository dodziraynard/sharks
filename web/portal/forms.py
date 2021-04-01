from django import forms

from .models import Course, Lesson, Quiz, Scholarship, Book


class LessonForm(forms.ModelForm):
    class Meta:
        model = Lesson
        exclude = ["time"]
        extra_kwargs = {"id": {"write_only": True}}


class CourseForm(forms.ModelForm):
    class Meta:
        model = Course
        exclude = ["time"]
        extra_kwargs = {"id": {"write_only": True}}


class QuizForm(forms.ModelForm):
    class Meta:
        model = Quiz
        fields = '__all__'
        extra_kwargs = {"id": {"write_only": True}}


class ScholarshipForm(forms.ModelForm):
    class Meta:
        model = Scholarship
        fields = '__all__'
        extra_kwargs = {"id": {"write_only": True}}


class BookForm(forms.ModelForm):
    class Meta:
        model = Book
        fields = '__all__'
        extra_kwargs = {"id": {"write_only": True}}