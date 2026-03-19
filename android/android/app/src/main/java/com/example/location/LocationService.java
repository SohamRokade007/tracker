package com.example.location;

import android.app.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.*;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationService extends Service {

    private FusedLocationProviderClient fusedLocationClient;
    private String employeeName = "Unknown";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getStringExtra("employee_name") != null) {
            employeeName = intent.getStringExtra("employee_name");
        }
        return START_STICKY; // ✅ FIXED
    }

    @Override
    public void onCreate() {
        super.onCreate();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        startForeground(1, getNotification());
        startLocationUpdates();
    }

    private Notification getNotification() {

        String channelId = "tracker_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Tracking Service",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Tracking Active")
                .setContentText("Tracking: " + employeeName)
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void startLocationUpdates() {

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest request = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000
        ).build();

        fusedLocationClient.requestLocationUpdates(
                request,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult result) {

                        for (Location location : result.getLocations()) {
                            sendToServer(location);
                        }
                    }
                },
                Looper.getMainLooper()
        );
    }

    private void sendToServer(Location location) {
        new Thread(() -> {
            try {
                URL url = new URL("https://tracker-24co.onrender.com/update_location");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String json = "{"
                        + "\"employee_id\":\"" + employeeName + "\","
                        + "\"lat\":" + location.getLatitude() + ","
                        + "\"lng\":" + location.getLongitude()
                        + "}";

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("API", "Response: " + responseCode);

            } catch (Exception e) {
                Log.e("API", "Error", e);
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}