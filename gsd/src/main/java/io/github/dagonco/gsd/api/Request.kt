package io.github.dagonco.gsd.api

import android.util.Log
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

internal class Request(private val storage: Storage) {

    suspend fun getDevice(): Device? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(CSV_URL).openConnection() as HttpURLConnection

            val storedETag = storage.getEtag().first()
            if (storedETag != null) {
                connection.setRequestProperty(IF_NONE_MATCH_HEADER, storedETag)
            }

            return@withContext when (val responseCode = connection.responseCode) {
                HttpURLConnection.HTTP_NOT_MODIFIED -> {
                    Log.d(TAG, "ETag matches — cached data is up to date.")
                    null
                }
                HttpURLConnection.HTTP_OK -> {
                    Log.d(TAG, "New CSV available. Parsing.")
                    val newETag = connection.getHeaderField(ETAG_HEADER)
                    parseCsv(connection.inputStream).also {
                        if (newETag != null) storage.storeEtag(newETag)
                    }
                }
                else -> {
                    Log.d(TAG, "Unexpected response code: $responseCode")
                    null
                }
            }
        } catch (exception: Exception) {
            Log.d(TAG, "Exception fetching CSV: $exception")
            null
        }
    }

    private fun parseCsv(inputStream: InputStream): Device? {
        BufferedReader(InputStreamReader(inputStream, "UTF-16")).use { reader ->
            for (line in reader.lineSequence()) {
                val data = line.split(",").dropLastWhile(String::isEmpty)
                if (data.size == 4 && data.getOrNull(2) == android.os.Build.DEVICE) {
                    val (manufacturer, marketName, codename, model) = data
                    return Device(manufacturer, marketName, codename, model)
                }
            }
        }
        return null
    }

    private companion object {
        private const val TAG = "GSD"
        private const val ETAG_HEADER = "etag"
        private const val IF_NONE_MATCH_HEADER = "If-None-Match"
        private const val CSV_URL = "https://storage.googleapis.com/play_public/supported_devices.csv"
    }
}
