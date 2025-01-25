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
    path('subscribe/', views.subscribe, name='subscribe'),
    path('',views.index),
    path('signup',views.signup),
    path('logout/',views.logout, name='logout'),
    path('login',views.login, name='login'),
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
    path('viewcomplaint', views.viewcomplaint, name='viewcomplaint'),
    path('submit-replies/', views.submit_replies, name='submit_replies'),

    #--------------------STATION PAGE--------------------#
    path('station',views.station),
    path('station_profile',views.stationprofile),
    path('stationedit/<id>',views.updateprofile),
    path('stationanimal',views.stationanimal),
    path('addstationanimal',views.addstationanimal),
    path('deletestationanimal/<id>',views.deletestationanimal),
    path('cctvlist',views.cctvlist,name='cctvlist'),
    path('addcamera',views.addcamera),
    path('deletecamera/<id>',views.deletecamera),
    path('toggle-status/<camera_id>/', views.toggle_status, name='toggle_status'),

    #--------------------ANDROID USER PAGE--------------------#
    path('and_user_register',views.and_user_register),
    path('and_login',views.and_login),
    path('and_user_complaint',views.and_user_complaint),
    path('and_user_view_complaint',views.and_user_view_complaint),
    path('and_user_profile',views.and_user_profile),
    path('and_user_animal_list',views.and_user_animal_list),
    path('and_user_view_station',views.and_user_view_station),
    path('and_get_districts',views.and_get_districts),
    path('and_get_divisions',views.and_get_divisions),
    path('and_get_stations',views.and_get_stations),
    path('and_get_profile',views.and_get_profile),
]