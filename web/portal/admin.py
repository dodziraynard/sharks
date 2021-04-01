from django.contrib import admin
from . models import *
from django.contrib.auth.models import User


admin.site.register(Course)
admin.site.register(Lesson)
admin.site.register(Quiz)
admin.site.register(Option)
admin.site.register(Scholarship)
admin.site.register(StudentProfile)
admin.site.register(TeacherProfile)
admin.site.register(Comment)
admin.site.register(TeacherReview)
admin.site.register(APK)
admin.site.register(Book)
