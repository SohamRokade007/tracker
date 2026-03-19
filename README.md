# 🚀 Device Tracker System

![Status](https://img.shields.io/badge/status-active-success)
![Backend](https://img.shields.io/badge/backend-FastAPI-blue)
![Android](https://img.shields.io/badge/android-Java-green)
![License](https://img.shields.io/badge/license-MIT-yellow)

---

## 📍 Overview

A **real-time tracking system** using:

- 📱 Android (background tracking)
- 🌐 FastAPI (backend server)
- 🗺️ Web dashboard (admin map)

Track multiple devices live and visualize them on a map.

---

## ✨ Features

- 🔄 Real-time location tracking  
- 📡 Automatic background updates  
- 🌍 Live map visualization  
- ⚡ FastAPI REST backend  
- ☁️ Cloud deployed (Render)  

---

## 🏗️ Project Structure

```
tracker/
│
├── backend/
├── android/
├── admin/
│   └── map.html
└── README.md
```

---

## ⚙️ Backend Setup

```bash
cd backend
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8000
```

---

## 🌐 Live API

```
https://tracker-24co.onrender.com
```

---

## 📱 Android Setup

Update API URL in:

```java
LocationService.java
```

```java
URL url = new URL("https://tracker-24co.onrender.com/update_location");
```

---

## 🗺️ Admin Dashboard

Run locally:

```bash
python -m http.server 5500
```

Open:
```
https://SohamRokade007.github.io/tracker/map.html
```

---

## 🔄 Workflow

1. Device sends location 📍  
2. Backend stores data 💾  
3. Dashboard fetches data 🔄  
4. Map displays markers 🗺️  

---

## ⚠️ Limitations

- No offline retry  
- SQLite not persistent on free hosting  

---

## 🔮 Future Plans

- 📊 Location history  
- 🔐 Authentication  
- 📱 Admin app  
- 🚨 Alerts  

---

## 👨‍💻 Author

**Soham Rokade**

---

⭐ Star this repo if you like it!
