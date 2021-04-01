# Sharks
Sharks is an educational app that helps pre-tertiary Ghanaian students to take tailored lessons based on the new curriculum. 
This repository is where I build the backend (web app) of the sharks app. The web app provides a dashboard through which contents can be added, updated, or deleted. The mobile client version can be found at [Sharks Android App](https://github.com/dodziraynard/sharks-android) as well.

![sharks icon](screenshots/sharks-architecture.png)

## Language and Libraries
- Python 3
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