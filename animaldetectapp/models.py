from django.db import models

# Create your models here.
class Login(models.Model):
    password=models.CharField(max_length=100)
    usertype=models.CharField(max_length=100)
    email=models.CharField(max_length=100)

class Country(models.Model):
    name=models.CharField(max_length=100)

class State(models.Model):
    name=models.CharField(max_length=100)
    country=models.ForeignKey(Country,on_delete=models.CASCADE)

class District(models.Model):
    name=models.CharField(max_length=100)
    state=models.ForeignKey(State,on_delete=models.CASCADE)

class ForestDivision(models.Model):
    name=models.CharField(max_length=100)
    established_year=models.CharField(max_length=100)
    description=models.CharField(max_length=500)
    area_covered=models.CharField(max_length=100)
    district=models.ForeignKey(District,on_delete=models.CASCADE)

class ForestStation(models.Model):
    login=models.ForeignKey(Login,on_delete=models.CASCADE)
    name=models.CharField(max_length=100)
    head=models.CharField(max_length=100)
    email=models.CharField(max_length=100)
    phone=models.CharField(max_length=100)
    lattitude=models.CharField(max_length=100)
    longitude=models.CharField(max_length=100)
    proof=models.CharField(max_length=100)
    division=models.ForeignKey(ForestDivision,on_delete=models.CASCADE)
    status=models.CharField(max_length=100)
    staff_count=models.CharField(max_length=100)
    password=models.CharField(max_length=100)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

class User(models.Model):
    login=models.ForeignKey(Login,on_delete=models.CASCADE)
    photo=models.CharField(max_length=100)
    gender=models.CharField(max_length=100)
    dob=models.CharField(max_length=100)
    name=models.CharField(max_length=100)
    email=models.CharField(max_length=100)
    phone=models.CharField(max_length=100)
    password=models.CharField(max_length=100)
    station=models.ForeignKey(ForestStation,on_delete=models.CASCADE)
    city=models.CharField(max_length=100)
    lattitude=models.CharField(max_length=100)
    longitude=models.CharField(max_length=100)

class Animals(models.Model):
    name=models.CharField(max_length=100)
    type=models.CharField(max_length=100)
    photo=models.CharField(max_length=200)
    description=models.CharField(max_length=500)
    endangered_status=models.CharField(max_length=100)
    risk=models.CharField(max_length=100)


class Station_Animal(models.Model):
    station=models.ForeignKey(ForestStation,on_delete=models.CASCADE)
    animal=models.ForeignKey(Animals,on_delete=models.CASCADE)
    population=models.CharField(max_length=100)

class Complaint(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    complaint=models.CharField(max_length=700)
    reply=models.CharField(max_length=700)
    date=models.CharField(max_length=100)
    status=models.CharField(max_length=100)
    
    