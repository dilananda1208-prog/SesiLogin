package com.example.sesilogin
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Tampilkan splash screen selama 1.5 detik (1500 ms)
        Handler(Looper.getMainLooper()).postDelayed({
            periksaSesiDanPindah()
        }, 1500)
    }
    private fun periksaSesiDanPindah() {
        val session = SessionManager(this)
        if (session.sudahLogin()) {
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}