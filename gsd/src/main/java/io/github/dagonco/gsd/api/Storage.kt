package io.github.dagonco.gsd.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException

internal open class Storage(
    private val context: Context,
) {

    open fun getDevice(): Flow<Device?> {
        return preferences().map { preferences ->
            preferences[GSD_DEVICE_KEY]?.let { runCatching { json.decodeFromString<Device>(it) }.getOrNull() }
        }
    }

    open suspend fun storeDevice(deviceInfo: Device) {
        context.dataStore.edit { preferences ->
            preferences[GSD_DEVICE_KEY] = json.encodeToString(deviceInfo)
        }
    }

    open fun getEtag(): Flow<String?> {
        return preferences().map { preferences ->
            preferences[ETAG_KEY]
        }
    }

    open suspend fun storeEtag(etag: String) {
        context.dataStore.edit { preferences ->
            preferences[ETAG_KEY] = etag
        }
    }

    private fun preferences(): Flow<Preferences> {
        return context.dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gsd_prefs_data_store")
        // Versioned so that devices missed or mismatched by older parsers are looked up again.
        private val ETAG_KEY = stringPreferencesKey("gsd_etag_v2")
        private val GSD_DEVICE_KEY = stringPreferencesKey("gsd_device_v2")
    }
}
