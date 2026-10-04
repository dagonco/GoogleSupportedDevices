package io.github.dagonco.gsd.model

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val manufacturer: String,
    val marketName: String,
    val codename: String,
    val model: String,
)
