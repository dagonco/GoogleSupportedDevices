package io.github.dagonco.gsd.data

import android.util.Log
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal open class Repository(
    private val networkDataSource: NetworkDataSource,
    private val storageDataSource: StorageDataSource,
) {

    open suspend fun getDevice(): Device = mutex.withLock {
        val cachedDevice = storageDataSource.getDevice().first()

        if (cachedDevice != null) {
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

        // Shared by all instances so concurrent calls don't download the CSV more than once.
        private val mutex = Mutex()
    }
}
