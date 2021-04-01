from rest_framework import serializers
from portal.models import *
from django.contrib.auth.models import User
from django.contrib.auth import authenticate


class CourseSerializer(serializers.ModelSerializer):
    class Meta:
        model = Course
        fields  = "__all__"

class LessonSerializer(serializers.ModelSerializer):
    class Meta:
        model = Lesson
        fields  = [
            "id",
            "course",
            "title",
            "slide",
            "description",
            "note",
            "video",
            "video_url",
            "image",
            "time",
            "has_quizzes"
        ]

class QuizSerializer(serializers.ModelSerializer):
    class Meta:
        model = Quiz
        fields  = [
            'id',
            "duration",
            "lesson",
            "question",
            "options",
            "str_correct_option",
        ]


class ScholarshipSerializer(serializers.ModelSerializer):
    class Meta:
        model = Scholarship
        fields  = [
            'id',
            'title',
            'description',
            'url',
            'location',
            'image',
            'active',
            'closes_in_millis',
            'opens_in_millis',
        ]


class BookSerializer(serializers.ModelSerializer):
    class Meta:
        model = Book
        fields  = "__all__"

class TeacherProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = TeacherProfile
        fields  = [
            'id',
            "user",
            "full_name",
            "image",
            "background_image",
            "contact",
            "email",
            "location",
            "education",
            'review_count',
            "header",
            "introduction",
            "rating",
        ]
    
    def to_representation(self, instance):
        ret = super(TeacherProfileSerializer, self).to_representation(instance)
        # check the request is list view or detail view
        is_list_view = isinstance(instance, list)
        # extra_ret = {'key': 'list value'} if is_list_view else {'key': 'single value'}
        # ret.update(extra_ret)
        return ret


class TeacherReviewSerializer(serializers.ModelSerializer):
    class Meta:
        model = TeacherReview
        fields  = [
            'id',
            "teacher",
            "time",
            "message",
            'rating',
            "time_in_millis",
        ]


class StudentProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = StudentProfile
        fields  = [
            'id',
            "contact",
            "user",
            "full_name",
            "level",
            'username',
            "location",
            'image',
            "background_image",
            "education",
            "introduction",
        ]


class RegisterSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ["id", "username", "password"]
        extra_kwargs = {"password": {"write_only": True}}

    def create(self, validated_data):
        validated_data.pop('id', None)
        password = validated_data.pop('password', None)
        user = self.Meta.model(**validated_data)
        if password is not None:
            user.set_password(password)
            user.save()
        return user

# Login Serializer
class LoginSerializer(serializers.Serializer):
    username = serializers.CharField()
    password = serializers.CharField()

    def validate(self, data):
        user = authenticate(**data)
        if user and user.is_active:
            return user
        raise serializers.ValidationError("Incorrect Credentials")