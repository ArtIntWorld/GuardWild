from django.db import models

# Create your models here.
class Login(models.Model):
    username=models.CharField(max_length=100)
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

class User(models.Model):
    name=models.CharField(max_length=100)
    email=models.CharField(max_length=100)
    phone=models.CharField(max_length=100)
    password=models.CharField(max_length=100)
    country=models.ForeignKey(Country,on_delete=models.CASCADE)
    state=models.ForeignKey(State,on_delete=models.CASCADE)
    district=models.ForeignKey(District,on_delete=models.CASCADE)
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

class ForestDivision(models.Model):
    name=models.CharField(max_length=100)
    established_year=models.CharField(max_length=100)
    description=models.CharField(max_length=500)
    area_covered=models.CharField(max_length=100)
    district=models.ForeignKey(District,on_delete=models.CASCADE)

class ForestStation(models.Model):
    name=models.CharField(max_length=100)
    head=models.CharField(max_length=100)
    email=models.CharField(max_length=100)
    phone=models.CharField(max_length=100)
    lattitude=models.CharField(max_length=100)
    longitude=models.CharField(max_length=100)
    proof=models.CharField(max_length=100)
    division=models.CharField(max_length=100)
    status=models.CharField(max_length=100)
    staff_count=models.CharField(max_length=100)
    password=models.CharField(max_length=100)

    