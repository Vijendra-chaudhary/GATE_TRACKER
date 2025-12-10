# GATE Exam Tracker - Android App

A modern Android application built with Kotlin and Jetpack Compose to help students prepare for GATE (Graduate Aptitude Test in Engineering) exams.

## ✨ Features

- **6 Engineering Branches Supported:**
  - Computer Science & IT (CS)
  - Electronics & Communication (EC)
  - Electrical Engineering (EE)  
  - Mechanical Engineering (ME)
  - Civil Engineering (CE)
  - Data Science & AI (DA)

- **Complete Syllabus Tracking:** Track progress across 400+ chapters
- **Exam Countdown:** Motivational countdown to your exam date
- **Progress Visualization:** See your overall and subject-wise progress
- **Offline-First:** All data stored locally, no internet required
- **Modern UI:** Material Design 3 with branch-specific color themes

## 🛠️ Technology Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Database:** Room (SQLite)
- **Architecture:** Simplified MVVM
- **Async:** Kotlin Coroutines + Flow
- **Navigation:** Compose Navigation

## 📋 Requirements

- Android Studio Hedgehog | 2023.1.1 or newer
- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Kotlin 1.9.20+

## 🚀 Building the App

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Generate launcher icons (optional):
   - Right-click on `res` folder → New → Image Asset
   - Create launcher icons for `ic_launcher` and `ic_launcher_round`
4. Build the project:
   ```
   ./gradlew assembleDebug
   ```
5. Run on device/emulator or find APK in:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

## 📱 App Flow

1. **First Launch:** Select your GATE branch
2. **Dashboard:** View countdown, overall progress, and subjects
3. **Subject Detail:** Mark chapters as complete/incomplete
4. **Exam Date:** Change your target exam date

## 📦 Project Structure

```
app/src/main/java/com/gate/tracker/
├── data/
│   ├── local/
│   │   ├── entity/      # Room entities
│   │   ├── dao/         # Database access objects
│   │   ├── GateDatabase.kt
│   │   └── SyllabusData.kt  # Pre-loaded syllabus data
│   └── repository/      # Data repository
├── ui/
│   ├── branch/          # Branch selection screen
│   ├── dashboard/       # Main dashboard
│   ├── subject/         # Chapter list
│   ├── examdate/        # Date picker
│   ├── components/      # Reusable UI components
│   └── theme/           # Material Design theme
├── GateApp.kt           # Application class
├── MainActivity.kt      # Main activity
└── ViewModelFactories.kt # ViewModel factories
```

## 🎨 Branch Colors

- **CS:** Blue (#4A90E2)
- **EC:** Purple (#9B59B6)
- **EE:** Orange (#F39C12)
- **ME:** Dark Orange (#E67E22)
- **CE:** Green (#27AE60)
- **DA:** Red (#E74C3C)

## 📝 Default Exam Date

February 8, 2026 (GATE 2026)

##⚠️ Notes

- PNG launcher icons are placeholders. Generate proper icons in Android Studio for production.
- The app is optimized for devices with 2GB+ RAM running Android 7.0+

## 📄 License

This is a demo project created for educational purposes.

---

**Built with ❤️ using Kotlin and Jetpack Compose**
