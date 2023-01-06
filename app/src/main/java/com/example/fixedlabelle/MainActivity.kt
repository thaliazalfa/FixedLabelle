package com.example.fixedlabelle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View


class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

        setFullScreen(window)
        lightStatusBar(window)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    }
}
