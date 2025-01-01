from django.shortcuts import render, redirect
from django.http import HttpResponse
from django.core.files.storage import FileSystemStorage
from django.http import HttpResponse
from django.shortcuts import redirect
from django.http import HttpResponse
from django.http import JsonResponse
from .models import*

import base64
from datetime import datetime
from django.core.files.storage import FileSystemStorage
from django.core.files.base import ContentFile

# Create your views here.

#----------------------------DROPDOWN FNC----------------------------#

def get_states(request):
    # Get the country_id from GET parameters, defaulting to 1 if not provided
    country_id = request.GET.get('country_id', 1)  # Default to 1 if country_id is not provided
    states = State.objects.filter(country_id=country_id).values('id', 'name')
    return JsonResponse(list(states), safe=False)

def get_districts(request):
    # Get the state_id from GET parameters, defaulting to 12 if not provided
    state_id = request.GET.get('state_id', 12)  # Default to 12 if state_id is not provided
    districts = District.objects.filter(state_id=state_id).values('id', 'name')
    return JsonResponse(list(districts), safe=False)

def get_divisions(request):
    district_id=request.GET.get('district_id')
    if district_id:
        divisions=ForestDivision.objects.filter(district_id=district_id).values('id', 'name')
        return JsonResponse(list(divisions), safe=False)
    return JsonResponse([], safe=False)

#----------------------------PUBLIC PAGE----------------------------#

def public(request):
    return render(request, "index1.html")

def login(request):
    if 'submit' in request.POST:
        email=request.POST['email']
        password=request.POST['password']
        usertype=request.POST['role']
        if Login.objects.filter(email=email,password=password,usertype=usertype).exists():
            res=Login.objects.get(email=email,usertype=usertype)
            request.session['login_id']=res.pk
            login_id=request.session['login_id']

            if res.usertype =='admin':
                request.session['log']="in"
                return HttpResponse(f"<script>alert('welcome Admin');window.location='admin'</script>")
            elif res.usertype == 'station':
                if ForestStation.objects.filter(login_id=login_id).exists():
                    res_station=ForestStation.objects.get(login_id=login_id)
                    if res_station:
                        request.session['log']="in"
                        request.session['foreststation_id']=res_station.pk
                        print(f"{res_station.name}")
                    return HttpResponse(f"<script>alert('welcome Officer');window.location='station'</script>")
        else:        
            return HttpResponse(f"<script>alert('Invalid username or password');window.location='login'</script>")
    return render(request,'public/login.html')

def index(request):
    return render(request,"public/index.html")

def signup(request):
    return render(request,"public/sign_up.html")

def register_station(request):
    countries = Country.objects.get(id=1)
    if 'submit' in request.POST:
        name=request.POST['name']
        email=request.POST['email']

        if ForestStation.objects.filter(email=email).exists():
            return HttpResponse(f"<script>alert('{name} exists already');window.location='/register_station'</script>")

        head=request.POST['head']
        phone=request.POST['phone']
        division_id=request.POST['division']
        division=ForestDivision.objects.get(id=division_id)
        password=request.POST['password']
        staff_count=request.POST['staff_count']
        proof=request.FILES['proof']

        file_extension = proof.name.split('.')[-1].lower()

        date = name + '.' + file_extension
        
        fs = FileSystemStorage(location='media/station/')
        fa = fs.save(date, proof)

        l=Login(email=email,password=password,usertype='pending')
        l.save()

        f=ForestStation(name=name,head=head,email=email,phone=phone,division=division,password=password,proof=f"media/station/{fa}",staff_count=staff_count,status='pending',login=l)
        f.save()
        return HttpResponse(f"<script>alert('Division added successfully');window.location='/login'</script>")

    country_id = 1  # Predefined country_id
    state_id = 12   # Predefined state_id

    # Fetch the country and state data
    country = Country.objects.get(id=country_id)  # Fetch the country
    state = State.objects.get(id=state_id)  # Fetch the state

    # Fetch districts based on the predefined state_id
    districts = District.objects.filter(state_id=state_id)  # Fetch districts by state_id
    
    # Optionally, fetch divisions based on the predefined district_id if needed
    divisions = ForestDivision.objects.filter(district_id=districts.first().id) if districts else []

    return render(request, "public/station_register.html", {
        'country': country,
        'state': state,
        'districts': districts,
        'divisions': divisions,})

def logout(request):
    if 'login_id' in request.session:
        del request.session['login_id']
    if 'log' in request.session:
        del request.session['log']
    if 'foreststation_id' in request.session:
        del request.session['foreststation_id']
    return redirect('login')

#----------------------------ADMIN PAGE----------------------------#

def admin(request):
    return render(request,"admin/admin.html")

def animallist(request):
    data=Animals.objects.all().order_by('name')
    return render(request,"admin/animal.html",{'data':data})

def addanimal(request):
    if 'submit' in request.POST:
        name=request.POST['name']
        type=request.POST['type']
        description=request.POST['description']
        endangered_status=request.POST['endangered_status']
        risk=request.POST['risk']
        photo=request.FILES['photo']

        file_extension = photo.name.split('.')[-1].lower()
        date = name + '.' + file_extension

        fs = FileSystemStorage(location='media/animals/')
        fa = fs.save(date, photo)

        if Animals.objects.filter(name=name).exists():
            return HttpResponse(f"<script>alert('{name} exists already');window.location='/addanimal'</script>")
        
        a=Animals(name=name,endangered_status=endangered_status,risk=risk,type=type,description=description,photo=f"media/animals/{fa}")
        a.save()
        return HttpResponse(f"<script>alert('Animal added successfully');window.location='/animallist'</script>")
    return render(request,"admin/addanimal.html")

def updateanimal(request,id):
    data=Animals.objects.get(id=id)
    if 'submit' in request.POST:
        name=request.POST['name']
        type=request.POST['type']
        description=request.POST['description']
        endangered_status=request.POST['endangered_status']
        risk=request.POST['risk']

        data.name=name
        data.type=type
        data.description=description
        data.endangered_status=endangered_status
        data.risk=risk
        
        if 'photo' in request.FILES:
            photo=request.FILES['photo']
            file_extension = photo.name.split('.')[-1].lower()
            date = name + '.' + file_extension
            fs=FileSystemStorage(location='media/animals/') 
            fa=fs.save(date, photo)
            data.photo=f"media/animals/{fa}"

        data.save()
        return HttpResponse(f"<script>alert('Animal updated successfully');window.location='/animallist'</script>")
    return render(request,"admin/updateanimal.html",{'data':data})

def deleteanimal(request,id):
    data=Animals.objects.get(id=id)
    name=data.name
    data.delete()
    return HttpResponse(f"<script>alert('{name} deleted successfully');window.location='/animallist'</script>")

def divisionlist(request):
    data=ForestDivision.objects.all()
    return render(request,"admin/divisionlist.html",{'data':data})

def updatedivision(request, id):
    data=ForestDivision.objects.get(id=id)

    countries = Country.objects.all()

    if 'submit' in request.POST:
        name=request.POST['name']
        established_year=request.POST['established_year']
        description=request.POST['description']
        area_covered=request.POST['area_covered']
        district_id=request.POST['district']
        district=District.objects.get(id=district_id)

        data.name=name
        data.established_year=established_year
        data.description=description
        data.area_covered=area_covered
        data.district=district

        data.save()

        return HttpResponse(f"<script>alert('Division updated successfully');window.location='/divisionlist'</script>")

    states = State.objects.filter(country=data.district.state.country)
    districts = District.objects.filter(state=data.district.state)

    return render(request, "admin/updatedivision.html", {
        'data': data,
        'countries': countries,
        'states': states,
        'districts': districts,
    })

def deletedivision(request,id):
    data=ForestDivision.objects.get(id=id)
    name=data.name
    data.delete()
    return HttpResponse(f"<script>alert('{name} deleted successfully');window.location='/divisionlist'</script>")

def adddivision(request):
    countries = Country.objects.all()
    if 'submit' in request.POST:
        name=request.POST['name']
        established_year=request.POST['established_year']
        description=request.POST['description']
        area_covered=request.POST['area_covered']
        district_id = request.POST['district']
        district = District.objects.get(id=district_id)

        if ForestDivision.objects.filter(name=name).exists():
            return HttpResponse(f"<script>alert('{name} exists already');window.location='/adddivision'</script>")
        
        f=ForestDivision(name=name,established_year=established_year,description=description,area_covered=area_covered,district=district)
        f.save()
        return HttpResponse(f"<script>alert('Division added successfully');window.location='/divisionlist'</script>")
    return render(request,"admin/adddivision.html",{'countries':countries})

def acceptorreject_station(request):
    data=ForestStation.objects.filter(status='pending')
    return render(request,"admin/pending_station.html",{'data':data})

def approvestation(request,id):
    data=ForestStation.objects.get(id=id)
    name=data.name
    data.status='active'
    login=data.login
    l=Login.objects.get(id=login.pk)
    l.usertype='station'

    l.save()
    data.save()

    return HttpResponse(f"<script>alert('{name} accepted successfully');window.location='/pending_station'</script>")

def deletestation(request,id):
    data=ForestStation.objects.get(id=id)
    name=data.name
    status=data.status
    data.delete()
    if status == 'pending':
        return HttpResponse(f"<script>alert('{name} rejected successfully');window.location='/pending_station'</script>")
    elif status == 'active':
        return HttpResponse(f"<script>alert('{name} deleted successfully');window.location='/stationlist'</script>")
    
def stationlist(request):
    data=ForestStation.objects.filter(status='active')
    return render(request,"admin/stationlist.html",{'data':data})

#----------------------------STATION PAGE----------------------------#

def station(request):
    return render(request,"forest_station/station.html")

def stationprofile(request):
    station_id=request.session['foreststation_id']
    data=ForestStation.objects.get(id=station_id)
    return render(request,"forest_station/profile.html",{'data':data})

def updateprofile(request,id):
    data=ForestStation.objects.get(id=id)

    countries = Country.objects.all()

    if 'submit' in request.POST:
        name=request.POST['name']
        head=request.POST['head']
        email=request.POST['email']
        phone=request.POST['phone']
        
        if 'proof' in request.FILES:
            proof=request.FILES['proof']
            file_extension = proof.name.split('.')[-1].lower()
            date = name + '.' + file_extension
            fs=FileSystemStorage(location='media/station/') 
            fa=fs.save(date, proof)
            data.photo=f"media/station/{fa}"

        division_id=request.POST['division']
        division=ForestDivision.objects.get(id=division_id)

        staff_count=request.POST['staff_count']

        data.name=name
        data.head=head
        data.email=email
        data.phone=phone
        data.staff_count=staff_count
        data.division=division

        data.save()

        return HttpResponse(f"<script>alert('Station updated successfully');window.location='/station_profile'</script>")

    states = State.objects.filter(country=data.division.district.state.country)
    districts = District.objects.filter(state=data.division.district.state)
    divisions = ForestDivision.objects.filter(district=data.division.district)

    return render(request,"forest_station/update.html",{
        'data': data,
        'countries': countries,
        'states': states,
        'districts': districts,
        'divisions': divisions,
    })

def stationanimal(request):
    station_id=request.session['foreststation_id']
    station=ForestStation.objects.get(id=station_id)
    animals=Station_Animal.objects.filter(station=station)
    return render(request,"forest_station/station_animal.html",{'animals':animals})

def addstationanimal(request):
    animals=Animals.objects.all()
    if 'submit' in request.POST:
        station_id=request.session['foreststation_id']
        station=ForestStation.objects.get(id=station_id)

        animal_names = request.POST.getlist('animal_name[]')
        population_counts = request.POST.getlist('population_count[]')

        animal_data = list(zip(animal_names, population_counts))

        for animal_id, population in animal_data:
            animal = Animals.objects.get(id=animal_id)
            
            sa=Station_Animal(station=station,animal=animal,population=population)
            sa.save()
        return HttpResponse(f"<script>alert('Animals of {station.name} added successfully');window.location='/stationanimal'</script>")
    return render(request,"forest_station/add_stationanimal.html",{'animals':animals})

def deletestationanimal(request,id):
    data=Station_Animal.objects.get(id=id)
    name=data.animal.name
    data.delete()
    return HttpResponse(f"<script>alert('{name} deleted successfully');window.location='/stationanimal'</script>")


#----------------------------ANDROID USER PAGE----------------------------#

def and_user_register(request):
    name=request.POST['name']
    email=request.POST['email']
    phone=request.POST['phone']
    gender=request.POST['gender']
    city=request.POST['city']
    password=request.POST['password']
    dob=request.POST['dob']
    photo=request.POST['photo']
    lattitude=request.POST['lattitude']
    longitude=request.POST['longitude']
    district_name=request.POST['district']
    district=District.objects.get(name=district_name)

    profile=base64.b64decode(photo)
 
    timestr = datetime.now().strftime("%Y%m%d%H%M%S") 
    file_name = f"{name}_{timestr}.jpg"

    fs = FileSystemStorage(location='media/station/')
    fa = fs.save(file_name, ContentFile(profile))

    l = Login(email=email,password=password,usertype = 'user')
    l.save()

    f=User(name=name,email=email,phone=phone,longitude=longitude,lattitude=lattitude,district=district,gender=gender,city=city,password=password,dob=dob,photo=f"media/station/{fa}",login=l)
    f.save()

    return JsonResponse({'status':'ok'})

def and_login(request):
    email=request.POST['email']
    password=request.POST['password']
    if Login.objects.filter(email=email,password=password,usertype='user').exists():
        qa=Login.objects.get(email=email,password=password,usertype='user')
        lid=qa.pk
        if qa.usertype=='user':
            try:
                qd=User.objects.get(login_id=lid)
                uid=qd.pk
                return JsonResponse({'status':'ok','lid':lid,'uid':uid,'user_type':'user'})
            except User.DoesNotExist:
                print('Login Failed.')
                return JsonResponse({'status':'no'})
        else:
            print('Login Failed.')
            return JsonResponse({'status':'no'})
    else:
        print('Login Failed.')
        return JsonResponse({'status':'no'})
    
def and_user_complaint(request):
    user_id=request.POST['uid']
    complaint=request.POST['complaint']
    date=datetime.now().strftime("%d-%m-%Y")
    status=request.POST['status']
    user=User.objects.get(id=user_id)
    data=Complaint(user=user,complaint=complaint,status=status,date=date,reply='pending')
    data.save()
    return JsonResponse({'status':'ok'})

def and_user_view_complaint(request):
    user_id=request.POST['uid']
    complaints=Complaint.objects.filter(user_id=user_id)
    data=[]
    for i in complaints:
        data.append({'complaint':i.complaint,'reply':i.reply})
    return JsonResponse({'status':'ok','data':data})

def and_user_profile(request):
    user_id=request.POST['uid']
    user=User.objects.get(id=user_id)
    data=[]
    data.append({'name':user.name,'email':user.email,'phone':user.phone,'dob':user.dob,'password':user.password,'district':user.district.name,'city':user.city,'gender':user.gender,'photo':user.photo})
    return JsonResponse({'status':'ok','data':data})

from django.http import JsonResponse

def and_user_animal_list(request):
    user_id = request.POST['uid']
    user = User.objects.get(id=user_id)
    district_id = user.district.pk

    # Retrieve animals in the district
    animals = Animals.objects.filter(station_animal__station__division__district__id=district_id)
        
    data = []
    for animal in animals:  # Assuming there's a ForeignKey to Animals in Station_Animal
        data.append({
            'name': animal.name,
            'type': animal.type,
            'description': animal.description,
            'endangered': animal.endangered_status,
            'risk': animal.risk,
            'photo': animal.photo,
        })
    
    # Return the response after the loop
    return JsonResponse({'status': 'ok', 'data': data})

def and_user_view_station(request):
    user_id = request.POST['uid']
    user = User.objects.get(id=user_id)
    district_id = user.district.pk
    stations = ForestStation.objects.filter(division__district__id=district_id)
    data=[]
    for i in stations:
        data.append({'name':i.name,'email':i.email,'phone':i.phone,})
    return JsonResponse({'status': 'ok', 'data': data})