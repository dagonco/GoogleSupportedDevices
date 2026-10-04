package io.github.dagonco.gsd.api

import android.os.Build
import android.util.Log
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.cancellation.CancellationException

internal class Request(private val storage: Storage) {

    suspend fun getDevice(): Device? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            connection = URL(CSV_URL).openConnection() as HttpURLConnection
            connection.connectTimeout = CONNECT_TIMEOUT_MILLIS
            connection.readTimeout = READ_TIMEOUT_MILLIS

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
                    CsvParser.findDevice(connection.inputStream, Build.DEVICE, Build.MODEL).also {
                        if (newETag != null) storage.storeEtag(newETag)
                    }
                }
                else -> {
                    Log.d(TAG, "Unexpected response code: $responseCode")
                    null
                }
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Log.d(TAG, "Exception fetching CSV: $exception")
            null
        } finally {
            connection?.disconnect()
        }
    }

    private companion object {
        private const val TAG = "GSD"
        private const val ETAG_HEADER = "etag"
        private const val IF_NONE_MATCH_HEADER = "If-None-Match"
        private const val CSV_URL = "https://storage.googleapis.com/play_public/supported_devices.csv"
        private const val CONNECT_TIMEOUT_MILLIS = 15_000
        private const val READ_TIMEOUT_MILLIS = 30_000
    }
}
