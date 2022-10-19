package io.github.dagonco.gsd.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class Device(
    val manufacturer: String,
    val marketName: String,
    val codename: String,
    val model: String,
)