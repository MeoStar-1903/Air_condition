# Air Score

Air Score is an Android application designed to provide real-time air quality and weather information for cities worldwide. It offers a comprehensive breakdown of various environmental pollutants and uses an intuitive color-coded system to indicate the severity of air pollution, helping users stay informed about their local environment.

## Features

* **Real-time Air Quality & Temperature**: View the current Air Quality Index (AQI) and temperature for any searched city.
* **Detailed Pollutant Breakdown**: Monitors and displays specific metrics including:
  * PM2.5 & PM10 (Particulate Matter)
  * Ozone (O3)
  * Carbon Monoxide (CO)
  * Nitrogen Dioxide (NO2)
  * Sulfur Dioxide (SO2)
  * Humidity
* **Color-Coded Health Indicators**: The UI dynamically changes colors (ranging from green for "Good" to maroon for "Hazardous") based on the severity levels of the AQI and individual pollutants.
* **City Search**: A simple input field allows users to easily pull the latest environmental data for different cities globally.

## Tech Stack

* **Platform**: Android
* **Language**: Java
* **Minimum SDK**: 24
* **Target SDK**: 36
* **Networking**: OkHttp3 and Android Volley
* **API**: [World Air Quality Index (WAQI) API](https://aqicn.org/api/)

## Getting Started

### Prerequisites
* Android Studio (Electric Eel or newer recommended)
* Android SDK 36
* Java 11 (configured for source and target compatibility)

### Installation
1. **Clone the repository:**
   ```bash
   git clone <repository-url>
2. **Open the project in Android Studio.**
3. **Sync the project with Gradle files to download the required dependencies (Volley, OkHttp, Material Components, etc.).**
4. **Run the application on an emulator or a physical Android device.**

## API Configuration Note
The application uses the WAQI API to fetch real-time data. Currently, the API token is hardcoded in the MainActivity.java file. If you plan to deploy this application or make extensive requests, it is highly recommended to obtain your own free API token from the WAQI and replace the existing placeholder token.

## Project Structure
* app/src/main/java/vn/edu/usth/airscore/MainActivity.java: The core activity handling the UI updates, API calls, and color-coding logic.

* app/src/main/res/layout/activity_main.xml: The main XML layout containing the ScrollView, CardViews, and input fields.

* app/build.gradle.kts: Contains the app-level configuration and external library dependencies.
