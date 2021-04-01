# Sharks
This is the web app directory. The wbb app is built using Django framework and Python programming language.

## Language and Libraries
- Python 3.8
- Django 3

## Installation
Clone this repository and open it in an editor.
```bash
git clone https://github.com/dodziraynard/sharks.git
```

```bash
cd sharks/web

pip install -r requirements.txt

python .\manage.py makemigrations
python .\manage.py migrate
```
#### Creating an account
- Run the command below to create an administrative account to access the dashboard.
```bash
python .\manage.py createsuperuser 
```

## Running
Run this command to spin up the development server.
```bash
python .\manage.py runserver 0.0.0.0:8000
```
- Finally, open [http://127.0.0.1:8000/](http://127.0.0.1:8000/) in the browser and enter the credentials to login.
- Create some contents and run the mobile app (android) to view them in the app as well.


## Live demo
View the dashboard currently hosted on google cloud [here](http://34.67.115.110)