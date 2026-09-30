package com.example.sesilogin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GantiSandiActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganti_sandi)

        session = SessionManager(this)

        val etSandiLama = findViewById<EditText>(R.id.etSandiLama)
        val etSandiBaru = findViewById<EditText>(R.id.etSandiBaru)
        val btnSimpan = findViewById<Button>(R.id.btnSimpanSandi)

        btnSimpan.setOnClickListener {
            val lama = etSandiLama.text.toString()
            val baru = etSandiBaru.text.toString()

            if (!session.validasiSandi(lama, "123456")) {
                etSandiLama.error = "Sandi lama salah!"
                return@setOnClickListener
            }

            if (baru.length < 6) {
                etSandiBaru.error = "Sandi baru minimal 6 karakter!"
                return@setOnClickListener
            }

            // Simpan sandi baru ke SharedPreferences dalam bentuk Hash SHA-256
            session.simpanSandiBaru(baru)
            Toast.makeText(this, "Sandi berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}