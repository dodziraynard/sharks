from pathlib import Path

SECRET_KEY = 'tdmavu&t#o632z(si4s&^9hiw$0v1a7i%(hm6hdjf^@jb&g1d!q'
DEBUG = True
ALLOWED_HOSTS = ["*"]


# Database
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': 'db.sqlite3',
    }
}
