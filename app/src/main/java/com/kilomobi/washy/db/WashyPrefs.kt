package com.kilomobi.washy.db

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.firebase.auth.FirebaseUser

class WashyPrefs {
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    var user: FirebaseUser? = null

    companion object {
        private var washyPrefs: WashyPrefs? = null

        @Synchronized
        fun getInstance(ctx: Context?): WashyPrefs? {
            if (washyPrefs == null && ctx != null) {
                // Create or retrieve the Master Key for encryption/decryption
                val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                // Initialize/open an instance of EncryptedSharedPreferences
                val sharedSecurePreferences = EncryptedSharedPreferences.create(
                    "PreferencesFilename",
                    masterKeyAlias,
                    ctx,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )

                washyPrefs = WashyPrefs()
                washyPrefs?.sharedPreferences = sharedSecurePreferences
                washyPrefs?.editor = sharedSecurePreferences.edit()
            }
            return washyPrefs
        }
    }

    @Synchronized
    fun clear() {
        try {
            editor?.clear()
            editor?.commit()
            washyPrefs?.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getString(KEY: String?, v: String?): String? {
        return if (null != washyPrefs) washyPrefs?.getString(KEY, v) else v
    }

    @Synchronized
    fun getBoolean(KEY: String?, v: Boolean?): Boolean? {
        return washyPrefs?.getBoolean(KEY, v!!) ?: v
    }

    @Synchronized
    fun getInt(KEY: String?, v: Int): Int {
        return washyPrefs?.getInt(KEY, v) ?: v
    }

    @Synchronized
    fun getFloat(KEY: String?, v: Float?): Float? {
        return washyPrefs?.getFloat(KEY, v!!) ?: v
    }

    @Synchronized
    fun getLong(KEY: String?, v: Long): Long? {
        return washyPrefs?.getLong(KEY, v) ?: v
    }

    @Synchronized
    fun putString(KEY: String?, v: String?) {
        if (null != editor) {
            editor?.putString(KEY, v)
            editor?.commit()
        }
    }

    @Synchronized
    fun putBoolean(KEY: String?, v: Boolean?) {
        if (null != editor) {
            editor?.putBoolean(KEY, v!!)
            editor?.commit()
        }
    }

    @Synchronized
    fun putInt(KEY: String?, v: Int) {
        if (null != editor) {
            editor?.putInt(KEY, v)
            editor?.commit()
        }
    }

    @Synchronized
    fun putFloat(KEY: String?, v: Float?) {
        if (null != editor) {
            editor?.putFloat(KEY, v!!)
            editor?.commit()
        }
    }

    @Synchronized
    fun putLong(KEY: String?, v: Long) {
        if (null != editor) {
            editor?.putLong(KEY, v)
            editor?.commit()
        }
    }
}