package org.example.mytask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalConfiguration
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(
            applicationContext,
            FirebaseOptions.Builder().setApiKey("AIzaSyCyi4hh0XzqwOYzdej67H8WLlYSYx3Kr5g")
                .setApplicationId("1:786980952096:android:0a2be1073609e1ec918445")
                .setProjectId("mytask-7d917")
                .build(),
        )

        /*  Firebase.initialize(
              applicationContext,
              options = FirebaseOptions(
                  applicationId = "1:786980952096:android:0a2be1073609e1ec918445",
                  apiKey = "AIzaSyCyi4hh0XzqwOYzdej67H8WLlYSYx3Kr5g",
                  projectId = "mytask-7d917"
              )
          )*/
        setContent {
            App(getCalculateWindowSizeClass(LocalConfiguration.current))
        }
    }
}