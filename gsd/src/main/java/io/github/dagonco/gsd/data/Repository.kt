package io.github.dagonco.gsd.data

import android.util.Log
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.flow.first

internal open class Repository(
    private val networkDataSource: NetworkDataSource,
    private val storageDataSource: StorageDataSource,
) {

    open suspend fun getDevice(): Device {
        val cachedDevice = storageDataSource.getDevice().first()

        return if (cachedDevice != null) {
            Log.d(TAG, "Returning cached device.")
            cachedDevice
        } else {
            Log.d(TAG, "No cached device. Fetching from network.")
            val networkDevice = networkDataSource.getDevice()
            if (networkDevice != null) {
                Log.d(TAG, "Device found in CSV. Caching.")
                storageDataSource.storeDevice(networkDevice)
                networkDevice
            } else {
                Log.d(TAG, "Device not found in CSV. Using defaults.")
                storageDataSource.getDefaultDeviceInfo()
            }
        }
    }

    private companion object {
        private const val TAG = "GSD"
    }
}
