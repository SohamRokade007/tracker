from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import sqlite3
import time

app = FastAPI()

# ✅ CORS FIX
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # allow all (for now)
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

conn = sqlite3.connect("locations.db", check_same_thread=False)
cursor = conn.cursor()

cursor.execute("""
CREATE TABLE IF NOT EXISTS locations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    employee_id TEXT,
    lat REAL,
    lng REAL,
    timestamp INTEGER
)
""")
conn.commit()


class Location(BaseModel):
    employee_id: str
    lat: float
    lng: float


@app.post("/update_location")
def update_location(loc: Location):
    cursor.execute(
        "INSERT INTO locations (employee_id, lat, lng, timestamp) VALUES (?, ?, ?, ?)",
        (loc.employee_id, loc.lat, loc.lng, int(time.time()))
    )
    conn.commit()
    return {"status": "success"}


@app.get("/devices")
def get_devices():

    query = """
    SELECT l1.employee_id, l1.lat, l1.lng
    FROM locations l1
    INNER JOIN (
        SELECT employee_id, MAX(timestamp) AS max_time
        FROM locations
        GROUP BY employee_id
    ) l2
    ON l1.employee_id = l2.employee_id AND l1.timestamp = l2.max_time
    """

    cursor.execute(query)
    rows = cursor.fetchall()

    devices = []
    for row in rows:
        devices.append({
            "employee_id": row[0],
            "lat": row[1],
            "lng": row[2]
        })

    print("Devices API:", devices)

    return devices