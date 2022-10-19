package io.github.dagonco.gsd.data

import android.util.Log
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.flow.first

internal open class Repository(
    private val networkDataSource: NetworkDataSource,
    private val storageDataSource: StorageDataSource,
) {

    open suspend fun getDevice(): Device {
        val cachedDevice: Device? = storageDataSource.getDevice().first()

        return if (cachedDevice != null) {
            Log.d("GSD", "There is an existing cached device, it will be returned")
            cachedDevice
        } else {
            Log.d("GSD", "There is no cached device. Getting one from Google's CSV.")
            val networkDevice = networkDataSource.getDevice()
            if (networkDevice != null) {
                Log.d("GSD", "Device found in Google's CSV. Caching and returning it.")
                storageDataSource.storeDevice(networkDevice)
                networkDevice
            } else {
                Log.d("GSD", "This device has not been found in the Google CSV. Returning default values.")
                storageDataSource.getDefaultDeviceInfo()
            }
        }
    }
}