package com.example.sesilogin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val EMAIL_DEMO = "rani@example.com"
        private const val SANDI_DEMO = "123456"
    }

    private lateinit var session: SessionManager
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbIngatSaya: CheckBox
    private lateinit var tvPesanMasuk: TextView
    private lateinit var rgAvatar: RadioGroup
    private lateinit var ivLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)

        ivLogo = findViewById(R.id.ivLogo)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        cbIngatSaya = findViewById(R.id.cbIngatSaya)
        tvPesanMasuk = findViewById(R.id.tvPesanMasuk)
        rgAvatar = findViewById(R.id.rgAvatar)

        // Set preview gambar saat RadioButton dipilih
        rgAvatar.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbAvatar1 -> ivLogo.setImageResource(R.drawable.avatar_1)
                R.id.rbAvatar2 -> ivLogo.setImageResource(R.drawable.avatar_2)
                R.id.rbAvatar3 -> ivLogo.setImageResource(R.drawable.avatar_3)
            }
        }

        // Otomatis terisi email tersimpan (ingat email saja)
        etEmail.setText(session.ambilEmail())

        findViewById<Button>(R.id.btnMasuk).setOnClickListener { prosesLogin() }
    }

    private fun prosesLogin() {
        val email = etEmail.text.toString().trim()
        val sandi = etPassword.text.toString()

        tvPesanMasuk.text = ""

        if (email.isEmpty()) {
            etEmail.error = getString(R.string.pesan_email_kosong)
            return
        }
        if (sandi.length < 6) {
            etPassword.error = getString(R.string.pesan_sandi_pendek)
            return
        }

        // Cek login via sandi Hash / Demo
        if (email != EMAIL_DEMO || !session.validasiSandi(sandi, SANDI_DEMO)) {
            tvPesanMasuk.text = getString(R.string.pesan_login_gagal)
            return
        }

        val avatarIndex = when (rgAvatar.checkedRadioButtonId) {
            R.id.rbAvatar2 -> 1
            R.id.rbAvatar3 -> 2
            else -> 0
        }

        val nama = email.substringBefore("@").replaceFirstChar { it.uppercase() }

        // Simpan sesi beserta index avatar dan Hash Sandi
        session.simpanSesi(nama, email, avatarIndex, cbIngatSaya.isChecked, sandi)

        bukaDashboard()
    }

    private fun bukaDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}