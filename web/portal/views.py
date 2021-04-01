from django.contrib.auth.models import User
from django.shortcuts import render, redirect, get_object_or_404
from rest_framework import viewsets
from rest_framework.permissions import IsAuthenticated
from .forms import CourseForm, LessonForm, QuizForm, ScholarshipForm, BookForm
from .models import Course, Lesson, Quiz, Option, Scholarship, Book
from django.utils.html import strip_tags
from django.core.paginator import Paginator
from django.db.models import Q
from django.contrib.auth.decorators import login_required


PAGE_SIZE  = 30

@login_required(login_url="authentication:user_login")
def courses(request):
    template_name = "portal/courses/courses.html"
    query = request.GET.get("query", '')
    page = request.GET.get("page", 1)
    course_list = Course.objects.all().order_by("-id")
    if query:
        course_list = course_list.filter(
                                Q(title__icontains=query) |
                                Q(short_description__icontains=query) |
                                Q(level__icontains=query) |
                                Q(long_description__icontains=query)
                            )    

    paginator = Paginator(course_list, PAGE_SIZE)
    paged_courses = paginator.page(page)

    context = {
        "courses":paged_courses,
        "query":query
    }
    return render(request, template_name, context)


@login_required(login_url="authentication:user_login")
def course_create_update(request):
    template_name = "portal/courses/course_create_update.html"
    form = CourseForm
    if request.method == "GET":
        context = {}
        id = request.GET.get("id")
        if id:
            course = Course.objects.values().get(id=id)
            context.update({k:v for k,v in course.items() if v != None})
        return render(request, template_name, context)

    elif request.method == "POST":
        id = request.POST.get("id")
        if id:
            course  = Course.objects.get(id=id)
            form = form(request.POST, request.FILES, instance=course)
        else:
            form = form(request.POST, request.FILES)
        if form.is_valid():
            course = form.save()
            return redirect("portal:courses")
        else:
            field, er = list(form.errors.items())[0]
            error_message = f"{field.title()}: {strip_tags(er)}"
            context = {
                "error_message":error_message,
                **{k:v for k,v in request.POST.items()}
            }
        return render(request, template_name, context)
        

@login_required(login_url="authentication:user_login")
def course_delete(request):
    if request.method == "POST":
        course_id = request.POST.get("course_id")
        Course.objects.filter(id=course_id).delete()
        request.session["message"] = "Course deleted successfully."
        referer = request.META.get("HTTP_REFERER")
        return redirect(referer)
    return redirect("portal:courses")


@login_required(login_url="authentication:user_login")
def lessons(request):
    template_name = "portal/lessons/lessons.html"
    query = request.GET.get("query", '')
    page = request.GET.get("page", 1)
    lesson_list = Lesson.objects.all().order_by("-id")
    if query:
        lesson_list = lesson_list.filter(
                                Q(title__icontains=query) |
                                Q(description__icontains=query) |
                                Q(note__icontains=query)                            
                    )        
    paginator = Paginator(lesson_list, PAGE_SIZE)
    paged_lessons = paginator.page(page)

    context = {
        "lessons":paged_lessons,
        "query":query,
    }
    return render(request, template_name, context)


def course_lessons(request, course_id):
    template_name = "portal/lessons/lessons.html"
    course = get_object_or_404(Course, id=course_id)
    query = request.GET.get("query", '')
    page = request.GET.get("page", 1)
    lesson_list = Lesson.objects.filter(course=course).order_by("-id")
    if query:
        lesson_list = lesson_list.filter(
                                Q(title__icontains=query) |
                                Q(description__icontains=query) |
                                Q(note__icontains=query)                            
                    )    
    paginator = Paginator(lesson_list, PAGE_SIZE)
    paged_lessons = paginator.page(page)

    context = {
        "lessons":paged_lessons,
        "course":course,
        "query":query,
    }
    return render(request, template_name, context)


def lesson_create_update(request, course_id):
    template_name = "portal/lessons/lesson_create_update.html"
    form = LessonForm
    course = get_object_or_404(Course, id=course_id)
    context = {
        "course":course
    }
    if request.method == "GET":
        id = request.GET.get("id")
        if id:
            lessons = Lesson.objects.values().get(id=id)
            context.update({k:v for k,v in lessons.items() if v != None})
        return render(request, template_name, context)

    elif request.method == "POST":
        id = request.POST.get("id")
        if id:
            lesson  = Lesson.objects.get(id=id)
            form = form(request.POST, request.FILES, instance=lesson)
        else:
            form = form(request.POST, request.FILES)
        if form.is_valid():
            form.save()
            return redirect("portal:course_lessons", course.id)
        else:
            field, er = list(form.errors.items())[0]
            error_message = f"{field.title()}: {strip_tags(er)}"
            context.update({
                "error_message":error_message,
                **{k:v for k,v in request.POST.items()}
            })
        return render(request, template_name, context)

@login_required(login_url="authentication:user_login")
def lesson_delete(request):
    if request.method == "POST":
        lesson_id = request.POST.get("lesson_id")
        Lesson.objects.filter(id=lesson_id).delete()
        request.session["message"] = "Lesson deleted successfully."
        referer = request.META.get("HTTP_REFERER")
        return redirect(referer)
    return redirect("portal:courses")


def lesson_quizzes(request, lesson_id):
    template_name = "portal/quizzes/quizzes.html"
    lesson = get_object_or_404(Lesson, id=lesson_id)
    query = request.GET.get("query", '')
    page = request.GET.get("page", 1)
    quiz_list = Quiz.objects.filter(lesson=lesson).order_by("-id")
    if query:
        quiz_list = quiz_list.filter(question__icontains=query)    
    paginator = Paginator(quiz_list, PAGE_SIZE)
    paged_quizzes = paginator.page(page)

    context = {
        "quizzes":paged_quizzes,
        "lesson":lesson,
        "query":query,
    }
    return render(request, template_name, context)


def quiz_create_update(request, lesson_id):
    template_name = "portal/quizzes/quiz_create_update.html"
    form = QuizForm
    lesson = get_object_or_404(Lesson, id=lesson_id)
    context = {
        "lesson":lesson
    }
    if request.method == "GET":
        id = request.GET.get("id")
        if id:
            quiz = Quiz.objects.values().get(id=id)
            context.update({k:v for k,v in quiz.items() if v != None})
            context.update({"quiz": Quiz.objects.get(id=id)})
        return render(request, template_name, context)

    elif request.method == "POST":
        id = request.POST.get("id")
        if id:
            # Is being edited.
            quiz  = Quiz.objects.get(id=id)
            form = form(request.POST, request.FILES, instance=quiz)
        else:
            form = form(request.POST, request.FILES)
        if form.is_valid():
            quiz = form.save()
            if not id:
                return redirect(request.META.get("HTTP_REFERER")+"?id="+str(quiz.id))
            return redirect("portal:lesson_quizzes", lesson.id)
        else:
            field, er = list(form.errors.items())[0]
            error_message = f"{field.title()}: {strip_tags(er)}"
            context.update({
                "error_message":error_message,
                **{k:v for k,v in request.POST.items()},
            })
            context.update({"lesson":lesson})
        return render(request, template_name, context)


@login_required(login_url="authentication:user_login")
def quizzes(request):
    template_name = "portal/quizzes/quizzes.html"
    query = request.GET.get("query", '')
    page = request.GET.get("page", 1)
    quiz_list = Quiz.objects.all().order_by("-id")
    if query:
        quiz_list = quiz_list.filter(question__icontains=query)     
    paginator = Paginator(quiz_list, PAGE_SIZE)
    paged_lessons = paginator.page(page)

    context = {
        "quizzes":paged_lessons,
        "query":query,
    }
    return render(request, template_name, context)


@login_required(login_url="authentication:user_login")
def quiz_delete(request):
    if request.method == "POST":
        quiz_id = request.POST.get("quiz_id")
        Quiz.objects.filter(id=quiz_id).delete()
        request.session["message"] = "Quiz deleted successfully."
        referer = request.META.get("HTTP_REFERER")
        return redirect(referer)
    return redirect("portal:courses")

def add_option(request, quiz_id):
    text = request.POST.get("text")
    quiz = get_object_or_404(Quiz, id=quiz_id)
    Option.objects.create(text=text, quiz=quiz)
    return redirect(request.META.get("HTTP_REFERER"))

def delete_option(request, id):
    Option.objects.filter(id=id).delete()
    return redirect(request.META.get("HTTP_REFERER"))


@login_required(login_url="authentication:user_login")
def scholarships(request):
    template_name = "portal/scholarships/scholarships.html"
    query = request.GET.get("query", '')
    page = request.GET.get("page", 1)
    scholarship_list = Scholarship.objects.all().order_by("-id")
    if query:
        scholarship_list = scholarship_list.filter(
                                Q(title__icontains=query) |
                                Q(description__icontains=query))

    paginator = Paginator(scholarship_list, PAGE_SIZE)
    paged_scholarships = paginator.page(page)

    context = {
        "scholarships":paged_scholarships,
        "query":query
    }
    return render(request, template_name, context)


@login_required(login_url="authentication:user_login")
def scholarship_create_update(request):
    template_name = "portal/scholarships/scholarship_create_update.html"
    form = ScholarshipForm
    if request.method == "GET":
        context = {}
        id = request.GET.get("id")
        if id:
            scholarship = Scholarship.objects.values().get(id=id)
            context.update({k:v for k,v in scholarship.items() if v != None})
        return render(request, template_name, context)

    elif request.method == "POST":
        id = request.POST.get("id")
        if id:
            scholarship  = Scholarship.objects.get(id=id)
            form = form(request.POST, request.FILES, instance=scholarship)
        else:
            form = form(request.POST, request.FILES)
        if form.is_valid():
            scholarship = form.save()
            return redirect("portal:scholarships")
        else:
            field, er = list(form.errors.items())[0]
            error_message = f"{field.title()}: {strip_tags(er)}"
            context = {
                "error_message":error_message,
                **{k:v for k,v in request.POST.items()}
            }
        return render(request, template_name, context)
        

@login_required(login_url="authentication:user_login")
def scholarship_delete(request):
    if request.method == "POST":
        scholarship_id = request.POST.get("scholarship_id")
        Scholarship.objects.filter(id=scholarship_id).delete()
        request.session["message"] = "Scholarship deleted successfully."
        referer = request.META.get("HTTP_REFERER")
        return redirect(referer)
    return redirect("portal:scholarships")



@login_required(login_url="authentication:user_login")
def books(request):
    template_name = "portal/books/books.html"
    query = request.GET.get("query", '')
    page = request.GET.get("page", 1)
    book_list = Book.objects.all().order_by("-id")
    if query:
        book_list = book_list.filter(
                                Q(title__icontains=query) |
                                Q(description__icontains=query))

    paginator = Paginator(book_list, PAGE_SIZE)
    paged_books = paginator.page(page)

    context = {
        "books":paged_books,
        "query":query
    }
    return render(request, template_name, context)


@login_required(login_url="authentication:user_login")
def book_create_update(request):
    template_name = "portal/books/book_create_update.html"
    form = BookForm
    if request.method == "GET":
        context = {}
        id = request.GET.get("id")
        if id:
            book = Book.objects.values().get(id=id)
            context.update({k:v for k,v in book.items() if v != None})
        return render(request, template_name, context)

    elif request.method == "POST":
        id = request.POST.get("id")
        if id:
            book  = Book.objects.get(id=id)
            form = form(request.POST, request.FILES, instance=book)
        else:
            form = form(request.POST, request.FILES)
        if form.is_valid():
            book = form.save()
            return redirect("portal:books")
        else:
            field, er = list(form.errors.items())[0]
            error_message = f"{field.title()}: {strip_tags(er)}"
            context = {
                "error_message":error_message,
                **{k:v for k,v in request.POST.items()}
            }
        return render(request, template_name, context)
        

@login_required(login_url="authentication:user_login")
def book_delete(request):
    if request.method == "POST":
        book_id = request.POST.get("book_id")
        Book.objects.filter(id=book_id).delete()
        request.session["message"] = "Book deleted successfully."
        referer = request.META.get("HTTP_REFERER")
        return redirect(referer)
    return redirect("portal:books")