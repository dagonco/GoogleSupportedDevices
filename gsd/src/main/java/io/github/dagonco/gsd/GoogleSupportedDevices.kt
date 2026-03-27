package io.github.dagonco.gsd

import android.content.Context
import io.github.dagonco.gsd.di.buildRepository
import io.github.dagonco.gsd.model.Device

class GoogleSupportedDevices(context: Context) {

    private val repository = buildRepository(context)

    suspend fun getDevice(): Device = repository.getDevice()
}
