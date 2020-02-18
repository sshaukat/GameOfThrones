package ru.skillbranch.gameofthrones.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import ru.skillbranch.gameofthrones.AppConfig.START_DELAY
import ru.skillbranch.gameofthrones.MainActivity
import ru.skillbranch.gameofthrones.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, START_DELAY)
    }
}
