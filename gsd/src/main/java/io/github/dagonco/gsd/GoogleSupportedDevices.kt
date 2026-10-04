package io.github.dagonco.gsd

import android.content.Context
import io.github.dagonco.gsd.di.buildRepository
import io.github.dagonco.gsd.model.Device

/**
 * Resolves the market name of the current device (e.g. "Galaxy S10" instead of "SM-G973F")
 * using the list of devices supported by Google Play.
 */
public class GoogleSupportedDevices(context: Context) {

    private val repository = buildRepository(context)

    /**
     * Returns the current device. The first call downloads Google's list and caches the result,
     * so later calls are answered locally. If the device is not in the list or the list can't be
     * downloaded, the values come from [android.os.Build].
     */
    public suspend fun getDevice(): Device = repository.getDevice()
}
