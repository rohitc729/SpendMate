package com.rohitchauhan.spendmate.data.pref

import android.content.SharedPreferences
import androidx.compose.ui.res.painterResource
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SharedPrefService @Inject constructor(
    private val prefs: SharedPreferences
) {
    companion object {
        private const val KEY_LAST_SYNC_TIMESTAMP = "key_last_sync_timestamp"
        private const val KEY_IS_ANYTHING_TO_UPLOAD = "key_is_anything_to_upload"
    }

    fun saveLastSyncTimeStamp(){
        prefs.edit { putLong(KEY_LAST_SYNC_TIMESTAMP, System.currentTimeMillis()) }
    }
    fun getLastSyncTimeStamp(): Long {
        return prefs.getLong(KEY_LAST_SYNC_TIMESTAMP,0L)
    }

    fun clear(){
        prefs.edit { clear() }
    }
}
