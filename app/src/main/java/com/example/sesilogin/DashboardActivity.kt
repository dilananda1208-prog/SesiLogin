package com.example.sesilogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        session = SessionManager(this)

        if (!session.adaDataProfil()) {
            kembaliKeLogin()
            return
        }

        // Tampilkan Sapaan Waktu
        findViewById<TextView>(R.id.tvSapaan).text = dapatkanSapaanWaktu()
        findViewById<TextView>(R.id.tvNama).text = session.ambilNama()
        findViewById<TextView>(R.id.tvEmail).text = session.ambilEmail()
        findViewById<TextView>(R.id.tvWaktu).text = formatWaktu(session.ambilWaktuLogin())

        // Tampilkan Avatar Sesuai Pilihan Sesi
        val ivAvatar = findViewById<ImageView>(R.id.ivAvatar)
        when (session.ambilAvatarIndex()) {
            0 -> ivAvatar.setImageResource(R.drawable.avatar_1) // Hijau
            1 -> ivAvatar.setImageResource(R.drawable.avatar_2) // Biru
            2 -> ivAvatar.setImageResource(R.drawable.avatar_3) // Oranye
            else -> ivAvatar.setImageResource(R.drawable.avatar_1)
        }

        // Menu Ganti Sandi
        findViewById<TextView>(R.id.btnMenuGantiSandi).setOnClickListener {
            startActivity(Intent(this, GantiSandiActivity::class.java))
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener { keluar() }
    }

    private fun dapatkanSapaanWaktu(): String {
        val jam = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (jam) {
            in 4..10 -> "Selamat pagi,"
            in 11..14 -> "Selamat siang,"
            in 15..18 -> "Selamat sore,"
            else -> "Selamat malam,"
        }
    }

    private fun formatWaktu(millis: Long): String {
        if (millis == 0L) return "-"
        val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        return format.format(Date(millis))
    }

    private fun keluar() {
        session.hapusSesiLoginSaja() // Hapus status login tetapi SIMPAN email
        kembaliKeLogin()
    }

    private fun kembaliKeLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}