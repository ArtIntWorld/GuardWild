from django.shortcuts import render
from django.http import HttpResponse
from django.http import JsonResponse
from .models import*
import datetime
from django.core.files.storage import FileSystemStorage

# Create your views here.

def get_states(request):
    country_id = request.GET.get('country_id')
    if country_id:
        # Fetch states that belong to the selected country
        states = State.objects.filter(country_id=country_id).values('id', 'name')
        return JsonResponse(list(states), safe=False)
    return JsonResponse([], safe=False)

# View to fetch districts based on the selected state
def get_districts(request):
    state_id = request.GET.get('state_id')
    if state_id:
        # Fetch districts that belong to the selected state
        districts = District.objects.filter(state_id=state_id).values('id', 'name')
        return JsonResponse(list(districts), safe=False)
    return JsonResponse([], safe=False)

def index(request):
    if 'submit' in request.POST:
        email=request.POST['email']
        password=request.POST['password']
        role=request.POST['role']
        if Login.objects.filter(email=email,password=password).exists():
            res=Login.objects.get(email=email)

    return render(request,"public/index.html")

def signup(request):
    return render(request,"public/sign_up.html")

def admin(request):
    return render(request,"admin/admin.html")

def login(request):
    if 'submit' in request.POST:
        email = request.POST['email']
        password = request.POST['password']
        if Login.objects.filter(email=email,password=password).exists():
            res = Login.objects.get(email=email)
            request.session['login_id']=res.pk
            login_id=request.session['login_id']

            if res.usertype =='admin':
                request.session['log']="in"
                return HttpResponse(f"<script>alert('welcome Admin');window.location='admin'</script>")
        else:
            return HttpResponse(f"<script>alert('invalid username or password');window.location='login'</script>")
    return render(request,'public/login.html')

def animallist(request):
    data=Animals.objects.all()
    return render(request,"admin/animal.html",{'data':data})

def addanimal(request):
    if 'submit' in request.POST:
        name=request.POST['name']
        type=request.POST['type']
        description=request.POST['description']
        endangered_status=request.POST['endangered_status']
        risk=request.POST['risk']
        photo=request.FILES['photo']

        date=datetime.datetime.now().strftime("%Y%m%d-%H%M%S")+".jpg"
        fs = FileSystemStorage() 
        fa = fs.save(date, photo)

        if Animals.objects.filter(name=name).exists():
            return HttpResponse(f"<script>alert('{name} exists already');window.location='/addanimal'</script>")
        
        a=Animals(name=name,endangered_status=endangered_status,risk=risk,type=type,description=description,photo=fs.url(fa))
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
            date=datetime.datetime.now().strftime("%y%m%d-%H%M%S")+".jpg"
            fs = FileSystemStorage() 
            fp = fs.save(date, photo)
            data.photo=fs.url(fp)

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
        name = request.POST['name']
        established_year = request.POST['established_year']
        description = request.POST['description']
        area_covered = request.POST['area_covered']
        district_id = request.POST['district']
        district = District.objects.get(id=district_id)

        data.name = name
        data.established_year = established_year
        data.description = description
        data.area_covered = area_covered
        data.district = district

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









#----------------------------------------------------------------------------------------------------------------------------------------
#----------------------------------------------------------------------------------------------------------------------------------------
import googlemaps

# Initialize Google Maps client with your API key
gmaps = googlemaps.Client(key='AIzaSyApIANMRe7XkOopSo7273ggyNoeosbufBw')

def get_coordinates(location):
    geocode_result = gmaps.geocode(location)
    if geocode_result:
        # Extract latitude and longitude from the first result
        latitude = geocode_result[0]['geometry']['location']['lat']
        longitude = geocode_result[0]['geometry']['location']['lng']
        return latitude, longitude
    return None, None
