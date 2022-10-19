package io.github.dagonco.gsd.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal open class Storage(
    private val context: Context,
) {

    private val moshi: Moshi = Moshi.Builder().build()
    private val jsonAdapter: JsonAdapter<Device> = moshi.adapter(Device::class.java)

    open fun getDevice(): Flow<Device?> {
        return context.dataStore.data.map { preferences ->
            preferences[GSD_DEVICE_KEY]?.let { device -> jsonAdapter.fromJson(device) }
        }
    }

    open suspend fun storeDevice(deviceInfo: Device) {
        val toJson = jsonAdapter.toJson(deviceInfo)
        context.dataStore.edit { preferences ->
            preferences[GSD_DEVICE_KEY] = toJson
        }
    }

    open fun getEtag(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ETAG_KEY]
        }
    }

    open suspend fun storeEtag(etag: String) {
        context.dataStore.edit { preferences ->
            preferences[ETAG_KEY] = etag
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gsd_prefs_data_store")
        private val ETAG_KEY = stringPreferencesKey("gsd_etag")
        private val GSD_DEVICE_KEY = stringPreferencesKey("gsd_device")
    }
}