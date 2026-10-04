package io.github.dagonco.gsd.model

import kotlinx.serialization.Serializable

/**
 * @property manufacturer Retail brand, e.g. "Samsung".
 * @property marketName Name the device is sold under, e.g. "Galaxy S10".
 * @property codename Value of [android.os.Build.DEVICE], e.g. "beyond1".
 * @property model Value of [android.os.Build.MODEL], e.g. "SM-G973F".
 */
@Serializable
public data class Device(
    val manufacturer: String,
    val marketName: String,
    val codename: String,
    val model: String,
)
