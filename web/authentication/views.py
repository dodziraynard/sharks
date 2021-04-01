from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login, logout


def user_login(request):
    template_name = "authentication/user_login.html"
    context = {}

    if request.method == "POST":
        username = request.POST.get("username")
        password = request.POST.get("password")
        user = authenticate(username=username, password=password)

        if user:
            login(request, user)
            redirect_url = request.GET.get("next") or "portal:courses"
            return redirect(redirect_url)
        else:
            context.update({"message": "Invalid credentials"})
    return render(request, template_name, context)


def user_logout(request):
    logout(request)
    return redirect("authentication:user_login")