from django.contrib.auth.models import User
from django.db import models
from django.utils import timezone


class Course(models.Model):
    title = models.CharField(max_length=200)
    short_description = models.CharField(max_length=200)
    long_description = models.TextField()
    website = models.URLField(max_length=200, null=True, blank=True)
    pdfs = models.TextField(null=True, blank=True)
    level = models.CharField(max_length=200)
    image = models.ImageField()
    active = models.BooleanField(default=True)
    time = models.DateTimeField(default=timezone.now)

    def __str__(self):
        return self.title
    
    def time_in_millis(self):
        return self.time.timestamp() * 1000

class Lesson(models.Model):
    course = models.ForeignKey(Course, related_name="lessons", on_delete=models.CASCADE)
    title = models.CharField(max_length=200)
    slide = models.FileField(null=True, blank=True)
    description = models.TextField()
    note = models.TextField(null=True, blank=True)
    video_file = models.FileField(null=True, blank=True)
    video_url = models.URLField(null=True, blank=True)
    image = models.ImageField()
    time = models.DateTimeField(default=timezone.now)

    def video(self):
        return self.video_url or self.video_file.url

    def has_quizzes(self):
        return 1 if self.quizzes.all().count() > 0 else 0

    def __str__(self):
        return self.title

class Quiz(models.Model):
    user = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True)
    duration = models.IntegerField()
    lesson = models.ForeignKey(Lesson, related_name="quizzes", on_delete=models.CASCADE)
    question = models.TextField()
    correct_option = models.OneToOneField("Option", related_name="correct_option_quiz", blank=True, null=True,  on_delete=models.CASCADE)

    def options(self):
        opts = Option.objects.filter(quiz=self)
        str_options = ""
        for item in opts:
            str_options +=item.text + "\n"
        str_options = str_options.strip("\n")
        return str_options
    
    def str_correct_option(self):
        return self.correct_option.text

    class Meta:
        verbose_name = "Quiz"
        verbose_name_plural = "Quizzes"

    def __str__(self):
            return self.question

class Option(models.Model):
    quiz = models.ForeignKey(Quiz, related_name="answer_options", on_delete=models.CASCADE)
    text = models.CharField(max_length=500)

    def __str__(self):
            return self.text


class Scholarship(models.Model):
    title = models.CharField(max_length=100)
    description = models.TextField()
    url = models.URLField()
    location = models.CharField(max_length=200)
    image = models.ImageField()
    active = models.BooleanField(default=True)
    opens = models.DateTimeField()
    closes = models.DateTimeField()

    def closes_in_millis(self):
        return self.closes.timestamp() * 1000

    def opens_in_millis(self):
        return self.opens.timestamp() * 1000

    def __str__(self):
            return self.title


class StudentProfile(models.Model):
    user = models.OneToOneField(User, related_name="student_profile", on_delete=models.CASCADE)
    full_name = models.CharField(max_length=200, blank=True, null=True)
    level = models.IntegerField(default=1)
    image = models.ImageField(upload_to="profiles")
    background_image = models.ImageField(upload_to="profiles", blank=True, null=True)
    education = models.CharField(blank=True, null=True, max_length=200)
    introduction = models.TextField(blank=True, null=True)
    contact = models.CharField(blank=True, null=True, max_length=10)
    location = models.CharField(max_length=100, blank=True, null=True)

    def username(self):
        return self.user.username

    def __str__(self):
        return self.full_name or self.user.username

class TeacherProfile(models.Model):
    user = models.OneToOneField(User, related_name="teacher_profile", on_delete=models.CASCADE)
    full_name = models.CharField(max_length=200, blank=True, null=True)
    image = models.ImageField(upload_to="profiles", blank=True, null=True)
    background_image = models.ImageField(upload_to="profiles", blank=True, null=True)
    contact = models.CharField(blank=True, null=True, max_length=10)
    email = models.EmailField(blank=True, null=True)
    education = models.CharField(blank=True, null=True, max_length=200)
    header = models.CharField(max_length=300, blank=True, null=True)
    location = models.CharField(max_length=100, blank=True, null=True)
    introduction = models.TextField(blank=True, null=True)
    rating = models.DecimalField(default=0, max_digits=2, decimal_places=1)

    def review_count(self):
        return TeacherReview.objects.filter(teacher=self).count()

    def __str__(self):
        return self.full_name or self.user.username


class Comment(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    lesson = models.ForeignKey(Lesson, related_name='comments', on_delete=models.CASCADE)
    time = models.DateTimeField(default=timezone.now)
    message = models.TextField()


class TeacherReview(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    teacher = models.ForeignKey(TeacherProfile, related_name='reviews', on_delete=models.CASCADE)
    time = models.DateTimeField(default=timezone.now)
    rating = models.DecimalField(max_digits=2, decimal_places=1)
    message = models.TextField()

    def time_in_millis(self):
        return self.time.timestamp() * 1000

    def __str__(self):
            return self.message

class Book(models.Model):
    file = models.FileField(upload_to="books")
    title = models.CharField(max_length=500)
    author = models.CharField(max_length=500)
    description = models.TextField()
    image = models.ImageField(upload_to="books")

    def __str__(self):
        return self.title

class APK(models.Model):
    file = models.FileField(upload_to="apks")
    version = models.CharField(max_length=10)

    def __str__(self):
        return self.version