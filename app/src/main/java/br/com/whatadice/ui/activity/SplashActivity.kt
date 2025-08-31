package br.com.whatadice.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.whatadice.data.SessionManager
import br.com.whatadice.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity: AppCompatActivity() {
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        session = SessionManager(this)

        lifecycleScope.launch {
            delay(1500)
            if (session.isLoggedIn()){
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            } else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }
    }
}