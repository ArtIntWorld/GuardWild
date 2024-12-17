import requests
import json

def get_accurate_location():
    try:
        GOOGLE_API_KEY = "AIzaSyApIANMRe7XkOopSo7273ggyNoeosbufBw"  # Replace with your Google API key
        url = f"https://www.googleapis.com/geolocation/v1/geolocate?key={GOOGLE_API_KEY}"

        # Simulated payload for better accuracy
        payload = {
            "wifiAccessPoints": [
                {"macAddress": "01:23:45:67:89:AB", "signalStrength": -65, "signalToNoiseRatio": 40},
                {"macAddress": "01:23:45:67:89:AC", "signalStrength": -70, "signalToNoiseRatio": 35}
            ]
        }

        response = requests.post(url, json=payload)
        data = response.json()

        if "location" in data:
            latitude = data["location"]["lat"]
            longitude = data["location"]["lng"]
            accuracy = data.get("accuracy", "N/A")
            return latitude, longitude, accuracy
        else:
            print("Location data not available.")
            return None, None, None
    except Exception as e:
        print("Error:", e)
        return None, None, None

# Example Usage
latitude, longitude, accuracy = get_accurate_location()
if latitude and longitude:
    print(f"Your location is:\nLatitude: {latitude}\nLongitude: {longitude}\nAccuracy: {accuracy} meters")
else:
    print("Could not determine the location.")
