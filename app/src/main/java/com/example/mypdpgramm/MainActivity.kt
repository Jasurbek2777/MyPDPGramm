package com.example.mypdpgramm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.Navigation
import com.example.mypdpgramm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.splashFragment).navigateUp()
    }


}