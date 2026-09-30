package com.example.sesilogin

import android.content.Context
import java.security.MessageDigest

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences(NAMA_PREFS, Context.MODE_PRIVATE)

    companion object {
        private const val NAMA_PREFS = "sesi_login"

        private const val KEY_SUDAH_LOGIN = "sudah_login"
        private const val KEY_NAMA        = "nama"
        private const val KEY_EMAIL       = "email"
        private const val KEY_WAKTU       = "waktu_login"
        private const val KEY_AVATAR_INDEX = "avatar_index"
        private const val KEY_HASH_SANDI  = "hash_sandi"

        // Batas waktu sesi: 7 Hari dalam Milidetik (7 * 24 * 60 * 60 * 1000)
        private const val BATAS_SESI_MILLIS = 7L * 24 * 60 * 60 * 1000
    }

    // Fungsi utilitas SHA-256 Hash
    fun hashSandi(sandi: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(sandi.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    // ================= MENYIMPAN SESI =================
    fun simpanSesi(nama: String, email: String, avatarIndex: Int, ingatSaya: Boolean, rawSandi: String? = null) {
        val editor = prefs.edit()
            .putBoolean(KEY_SUDAH_LOGIN, ingatSaya)
            .putString(KEY_NAMA, nama)
            .putString(KEY_EMAIL, email)
            .putInt(KEY_AVATAR_INDEX, avatarIndex)
            .putLong(KEY_WAKTU, System.currentTimeMillis())

        if (!rawSandi.isNullOrEmpty()) {
            editor.putString(KEY_HASH_SANDI, hashSandi(rawSandi))
        }

        editor.apply()
    }

    fun simpanSandiBaru(sandiBaru: String) {
        prefs.edit().putString(KEY_HASH_SANDI, hashSandi(sandiBaru)).apply()
    }

    // ================= MEMBACA & VALIDASI SESI =================
    fun sudahLogin(): Boolean {
        val isLogin = prefs.getBoolean(KEY_SUDAH_LOGIN, false)
        if (!isLogin) return false

        // Pengecekan Kadaluarsa 7 Hari
        val waktuLogin = ambilWaktuLogin()
        val sekarang = System.currentTimeMillis()
        if (sekarang - waktuLogin > BATAS_SESI_MILLIS) {
            hapusSesiLoginSaja() // Paksa login ulang jika > 7 hari
            return false
        }
        return true
    }

    fun validasiSandi(sandiInput: String, defaultSandi: String): Boolean {
        val hashTersimpan = prefs.getString(KEY_HASH_SANDI, null)
        val hashInput = hashSandi(sandiInput)

        return if (hashTersimpan != null) {
            hashTersimpan == hashInput
        } else {
            sandiInput == defaultSandi
        }
    }

    fun adaDataProfil(): Boolean = ambilNama().isNotEmpty() && ambilEmail().isNotEmpty()

    fun ambilNama(): String = prefs.getString(KEY_NAMA, "") ?: ""
    fun ambilEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""
    fun ambilWaktuLogin(): Long = prefs.getLong(KEY_WAKTU, 0L)
    fun ambilAvatarIndex(): Int = prefs.getInt(KEY_AVATAR_INDEX, 0)

    // ================= LOGOUT (INGAT EMAIL SAJA) =================
    fun hapusSesiLoginSaja() {
        prefs.edit()
            .putBoolean(KEY_SUDAH_LOGIN, false)
            .remove(KEY_NAMA)
            .remove(KEY_WAKTU)
            .apply()
        // Email & Hash Sandi tetap disimpan agar email terisi otomatis di form login
    }
}