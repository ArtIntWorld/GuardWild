# GuardWild: Real-Time Animal Species Identification

## Overview
The **Wildlife Detection System** aims to mitigate wildlife-human conflicts by utilizing an AI-based detection system that processes both audio and visual inputs from CCTV cameras to identify wildlife intrusions. This system helps protect villages and forested areas from wildlife damage by providing timely alerts to users. The detection system uses **ResNet50** for image recognition and **MFCC (Mel Frequency Cepstral Coefficients)** with a **Random Forest model** for audio-based detection.

## Features
- **CCTV-based Image Detection**: Uses **ResNet50 (Residual Neural Network)** to identify animal species from images captured by CCTV cameras.
- **Audio-based Detection**: Audio input is processed into **MFCC features** and analyzed using a **Random Forest model** for species detection.
- **Real-time Alerts**: The system sends notifications and alerts to users based on the detection of wildlife, including the affected region (via GPS).
- **Mitigates Human-Wildlife Conflict**: Reduces the risk of damage to property and harm to villagers by providing real-time alerts.

## Problem Statement
With the increasing frequency of **wildlife-human conflict (WHC)**, particularly in areas near forests, there is a need for a solution to monitor and prevent wildlife intrusions. The lack of effective technological solutions has led to significant challenges. This project aims to address these issues by accurately detecting wildlife intrusions and sending alerts to users, thus preventing property damage and potential harm to people.

## How It Works
1. **CCTV Footage Input**: The system processes video footage captured by CCTV cameras to extract frames and analyze them using **ResNet50** for animal species recognition.
2. **Audio Processing**: Audio input from the environment is processed into **MFCC features**, which are then passed through a **Random Forest model** for classification and species detection.
3. **Alert System**: Once an animal intrusion is detected, alerts are sent to users via an app. These alerts include GPS data to help target specific regions.
4. **Data and GPS Integration**: The system can use GPS coordinates from the CCTV cameras to target specific geographic locations, sending relevant alerts to users in the affected area.

### Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/ArtIntWorld/GuardWild-Real-Time-Animal-Species-Identification.git
   cd GuardWild-Real-Time-Animal-Species-Identification
