"""
URL configuration for AnimalDetection project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from .import views

urlpatterns = [

    #--------------------PLACE SELECTION DROP-DOWN--------------------#
    path('get_states/', views.get_states, name='get_states'),
    path('get_districts/', views.get_districts, name='get_districts'),
    path('get_divisions/', views.get_divisions, name='get_divisions'),

    #--------------------COMMON PAGE--------------------#
    path('',views.index),
    path('signup',views.signup),
    path('login',views.login),
    path('admin',views.admin),
    path('register_station',views.register_station),

    #--------------------ADMIN PAGE--------------------#
    path('animallist',views.animallist),
    path('addanimal',views.addanimal),
    path('updateanimal/<id>',views.updateanimal),
    path('deleteanimal/<id>',views.deleteanimal),
    path('divisionlist',views.divisionlist),
    path('updatedivision/<id>',views.updatedivision),
    path('deletedivision/<id>',views.deletedivision),
    path('adddivision',views.adddivision),
    path('pending_station',views.acceptorreject_station),
    path('approvestation/<id>',views.approvestation),
    path('deletestation/<id>',views.deletestation),
    path('stationlist',views.stationlist),

    #--------------------STATION PAGE--------------------#
    path('station',views.station),
    path('station_profile',views.stationprofile),
]