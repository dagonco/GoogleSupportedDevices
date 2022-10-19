package io.github.dagonco.gsd.data

import android.os.Build
import io.github.dagonco.gsd.api.Storage
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.flow.Flow

internal open class StorageDataSource(
    private val storage: Storage,
) {

    open fun getDevice(): Flow<Device?> {
        return storage.getDevice()
    }

    open suspend fun storeDevice(deviceInfo: Device) {
        storage.storeDevice(deviceInfo)
    }

    open fun getDefaultDeviceInfo(): Device {
        return Device(
            codename = Build.DEVICE,
            manufacturer = Build.MANUFACTURER,
            marketName = Build.MODEL,
            model = Build.MODEL,
        )
    }
}