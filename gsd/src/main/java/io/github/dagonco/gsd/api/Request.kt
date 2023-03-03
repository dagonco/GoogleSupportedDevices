package io.github.dagonco.gsd.api

import android.util.Log
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

internal class Request(private val storage: Storage) {

    suspend fun getDevice(): Device? = withContext(Dispatchers.IO) {

        val openConnection = URL(URL).openConnection()
        val eTag = openConnection.getHeaderField(ETAG_HEADER)

        return@withContext try {
            when {
                eTag == null -> {
                    Log.d("GSD", "eTag header was not found. Parsing the file anyways.")
                    parseCsv(openConnection.getInputStream())
                }

                isETagEquals(eTag) -> {
                    Log.d("GSD", "This CSV info was parsed previously.")
                    null
                }
                else -> {
                    Log.d("GSD", "A new CSV was found. Parsing the file.")
                    parseCsv(openConnection.getInputStream()).also {
                        storeEtag(eTag)
                    }
                }
            }
        } catch (exception: Exception) {
            Log.d("GSD", "An exception has been thrown. Exception: $exception")
            null
        }
    }

    private fun parseCsv(inputStream: InputStream): Device? {

        var currentDevice: Device? = null
        val inputStreamReader = InputStreamReader(inputStream, "UTF-16")
        val bufferedReader = BufferedReader(inputStreamReader)

        bufferedReader.use { reader ->
            reader.forEachLine { line ->
                val data = line.split(",").dropLastWhile(String::isEmpty)
                if (data.size == 4 && data.getOrNull(2) == android.os.Build.DEVICE) {
                    val (manufacturer, marketName, codename, model) = data
                    currentDevice = Device(manufacturer, marketName, codename, model)
                    return@forEachLine
                }
            }
        }

        inputStream.close()
        inputStreamReader.close()
        bufferedReader.close()

        return currentDevice
    }

    private suspend fun isETagEquals(eTag: String): Boolean {
        val storedETag = storage.getEtag().first()
        return eTag == storedETag
    }

    private suspend fun storeEtag(etag: String) {
        storage.storeEtag(etag)
    }

    private companion object {
        private const val ETAG_HEADER = "etag"
        private const val URL = "https://storage.googleapis.com/play_public/supported_devices.csv"
    }
}