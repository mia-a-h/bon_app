# Recipe & Meal Planner App

## Overview
The Recipe & Meal Planner App is an Android application designed to make cooking and meal preparation easier. Users can search for recipes, save favorites, generate shopping lists, and plan meals based on their preferences. The app aims to be a practical and enjoyable tool for food enthusiasts and meal planners alike.

---

## Features
- **Search & Filter Recipes**: Find recipes based on ingredients, meal type, and dietary preferences.
- **Save Favorites**: Bookmark your favorite recipes for quick access.
- **Detailed Recipe View**: View step-by-step instructions, nutritional information, and ingredient alternatives.
- **Shopping List Generator**: Automatically create shopping lists based on selected recipes.
- **Meal Planning**: Organize recipes into a weekly meal plan.

---

## Tech Stack
- **Language**: Kotlin
- **Architecture**: MVVM
- **Database**: Firestore
- **Network**: Retrofit for API calls

---

## Setup Instructions

### Clone the Repository:
```bash
git clone https://github.com/your-username/recipe-meal-planner.git
```
### Open Android Studio
- Open Android Studio and select "Open an Existing Project."
- Navigate to the cloned repository folder and open it.

### Configure API Key:
- Obtain an API key from the recipe data provider: Spoonacular.
- Add the API key to the Constants file in the following format:
```bash
const val API_KEY = “your_api_key_here”
```
### Build the Project:
Sync the Gradle files and build the project.

### Run the App:
Connect an Android device or use an emulator to run the app.

---

## Contributors
- Mia Abou Haidar
- Sahar Chreif
