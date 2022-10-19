package io.github.dagonco.gsd.data

import io.github.dagonco.gsd.api.Request
import io.github.dagonco.gsd.model.Device

internal open class NetworkDataSource(
    private val request: Request,
) {
    open suspend fun getDevice(): Device? {
        return request.getDevice()
    }
}