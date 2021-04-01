from knox.models import AuthToken
from rest_framework.viewsets import ModelViewSet     
from rest_framework import generics, permissions, status
from rest_framework.views import APIView      
from rest_framework.response import Response
from . serializers import (RegisterSerializer, 
                            TeacherProfileSerializer,
                            StudentProfileSerializer,
                                LoginSerializer)
from portal.models import TeacherProfile, StudentProfile
from rest_framework.exceptions import ValidationError
from django.contrib.auth.models import User
from django.db.models import Q
import json


class RegisterAPI(generics.GenericAPIView):
    """
    User registration endpoint
    """
    permission_classes = [permissions.AllowAny]
    serializer_class = RegisterSerializer

    def post(self, request, *args, **kwargs):
        error_message = ""
        request_data = request.data.copy()
        is_teacher = request_data.pop('is_teacher', None)[0]
        is_teacher = True if "true" in is_teacher else False

        serializer = self.get_serializer(data=request_data)
        try:
            serializer.is_valid(raise_exception=True)
        except ValidationError as e:
            for field in list(e.detail):
                error_message = e.detail.get(field)[0]
                return Response({"error_message": error_message})
        
        user = serializer.save()
        if is_teacher:
            teacher = TeacherProfile.objects.create(user=user, full_name=request_data.get("username"))
        else:
            student = StudentProfile.objects.create(user=user, full_name=request_data.get("username"))

        if is_teacher:
            response_data = {
                "teacher": TeacherProfileSerializer(teacher).data,
                "token": AuthToken.objects.create(user)[1],
            }
        else:
            response_data = {
                "student": StudentProfileSerializer(student).data,
                "token": AuthToken.objects.create(user)[1],
            }
        return Response(response_data)


class LoginAPI(generics.GenericAPIView):
    """
    User login endpoint
    """
    permission_classes = [permissions.AllowAny]
    serializer_class = LoginSerializer

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)

        try:
            serializer.is_valid(raise_exception=True)
        except ValidationError as e:
            for field in list(e.detail):
                error_message = e.detail.get(field)[0]
                return Response({"error_message": error_message})
        user = serializer.validated_data  
        response_data = {
            "token": AuthToken.objects.create(user)[1]
        }

        if hasattr(user, "student_profile"):
            student = user.student_profile
            response_data.update({"student":StudentProfileSerializer(student).data})        
        elif hasattr(user, "teacher_profile"):
            teacher = user.teacher_profile
            response_data.update({"teacher":TeacherProfileSerializer(teacher).data})   
        return Response(response_data)


class UpdateProfile(generics.GenericAPIView):
    """
    Update teacher or student profile.
    """
    permission_classes = [permissions.IsAuthenticated]

    def post(self, request, *args, **kwargs):
        if hasattr(request.user, "teacher_profile"):
            profile = request.user.teacher_profile
        elif hasattr(request.user, "student_profile"):
            profile = request.user.student_profile

        try:
            post_data = json.loads(request.body.decode())
            # post_data = {k:v for k,v in post_data.items() if v != ""}
            profile.__class__.objects.filter(id=profile.id).update(**post_data)
            profile = profile.__class__.objects.get(id=profile.id)
        except UnicodeDecodeError as e:
            # Post data is probably a file.
            pass

        full_name = request.POST.get("full_name")
        background_image = request.FILES.get("background_image")
        profile_image = request.FILES.get("profile_image")

        if background_image:
            profile.background_image = background_image
        if profile_image:
            profile.image = profile_image
        profile.save()
        return Response({"updated":True})

class GetProfile(generics.GenericAPIView):
    """
    Retrieve profile.
    """
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, *args, **kwargs):
        base_url = "https://" if request.is_secure() else "http://"
        base_url +=  request.META.get("HTTP_HOST")

        response_data = {}
        user_id = request.GET.get("user_id")
        if hasattr(request.user, "teacher_profile"):
            teacher = request.user.teacher_profile
            response_data.update({"teacher":TeacherProfileSerializer(teacher).data})       
        
        elif hasattr(request.user, "student_profile"):
            student = request.user.student_profile
            response_data.update({"student":StudentProfileSerializer(student).data})  

        image = response_data.get(list(response_data.keys())[0]).get("image")
        if image:
            image = base_url + image
        background_image = response_data.get(list(response_data.keys())[0]).get("background_image")
        if background_image:
            background_image = base_url + background_image
        
        response_data.get(list(response_data.keys())[0]).update({
            "background_image":background_image,
            "image":image
        })        
        return Response(response_data)