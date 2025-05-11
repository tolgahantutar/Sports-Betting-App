# Sports-Betting-App

A modern Android sports betting demo application built with clean architecture principles, Firebase Authentication, and real-time analytics.

---

## ✨ Features

- 🔐 **Authentication**
    - Login & Sign-Up using **Firebase Authentication**
    - Fully reactive using **Kotlin Flow**, **Coroutines**, and **MVVM**

- 📈 **Live Betting Odds**
    - Fetches upcoming sports events and odds via the **Odds API**
    - Retrofit for HTTP calls, Gson for parsing

- 🗂️ **Modular UI Screens**
    - **Bulletin Screen**: View and search upcoming events & odds
    - **Bet Basket**: Add/remove bets, calculate total odds & max win
    - **Profile**: View profile and logout

- 📊 **Analytics**
    - Firebase Analytics events are triggered on:
        - Selecting odds
        - Removing selections
        - Viewing details

---

## 🛠 Tech Stack

| Layer        | Libraries |
|--------------|-----------|
| Dependency Injection | Hilt |
| Networking   | Retrofit + Gson |
| Architecture | MVVM + Clean Architecture |
| Async        | Kotlin Coroutines + Flow |
| Auth & Analytics | Firebase Auth & Firebase Analytics |
| UI           | Material Design + ViewBinding |

---

## 🚀 Getting Started

### 🔧 Requirements

- Android Studio Hedgehog or later
- Min SDK: 24
- Firebase account
- [The Odds API](https://the-odds-api.com/) key

### 🧩 Setup Instructions

1. **Clone the repo**

   ```bash
   git clone https://github.com/yourusername/SportsBetApp.git

## 2. Firebase Configuration

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new Firebase project.
3. Add a new Android app with your application's package name.
4. Download the `google-services.json` file.
5. Place the `google-services.json` file in the following directory:

## 3. Set Odds API Key

Replace `"YOUR_API_KEY"` inside `OddsRemoteDataSource.kt` with your actual key from [The Odds API](https://the-odds-api.com/):

```kotlin
apiService.getOddsForSport(sport, apiKey = "YOUR_API_KEY")
```



## 📸 Screenshots

<img width="405" alt="image" src="https://github.com/user-attachments/assets/190b03dc-9b1d-45fa-99c7-343bf82f3e91" /> 
<img width="405" alt="image" src="https://github.com/user-attachments/assets/afdaaf18-5bc0-41e2-a425-ef6408e3ffdf" />
<img width="405" alt="image" src="https://github.com/user-attachments/assets/59fffaef-9c35-48ce-9d37-b17abab3b000" />
<img width="405" alt="image" src="https://github.com/user-attachments/assets/945aaccb-7a7b-4ede-b1c8-bee2c89f79d4" />
<img width="405" alt="image" src="https://github.com/user-attachments/assets/4017dbf9-bf46-4d8b-9ee2-cd412f6d844a" />





---

## ✅ License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


