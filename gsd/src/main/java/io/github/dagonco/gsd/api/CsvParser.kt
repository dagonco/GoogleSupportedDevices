package io.github.dagonco.gsd.api

import io.github.dagonco.gsd.model.Device
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

internal object CsvParser {

    /**
     * Looks up [codename] in Google's supported devices CSV. Several devices can share a codename,
     * so the row whose model also matches wins; otherwise the first row with that codename is used.
     */
    fun findDevice(inputStream: InputStream, codename: String, model: String): Device? {
        var codenameMatch: Device? = null
        BufferedReader(InputStreamReader(inputStream, Charsets.UTF_16)).use { reader ->
            for (line in reader.lineSequence().drop(1)) {
                if (!line.contains(codename)) continue
                val device = parseLine(line) ?: continue
                if (device.codename != codename || device.marketName.isBlank()) continue
                if (device.model.equals(model, ignoreCase = true)) return device
                if (codenameMatch == null) codenameMatch = device
            }
        }
        return codenameMatch
    }

    private fun parseLine(line: String): Device? {
        val fields = splitFields(line)
        if (fields.size != COLUMNS) return null
        val (manufacturer, marketName, codename, model) = fields
        return Device(manufacturer, marketName, codename, model)
    }

    private fun splitFields(line: String): List<String> {
        val fields = mutableListOf<String>()
        val field = StringBuilder()
        var inQuotes = false
        var index = 0
        while (index < line.length) {
            val char = line[index]
            when {
                char == '"' && inQuotes && line.getOrNull(index + 1) == '"' -> {
                    field.append('"')
                    index++
                }
                char == '"' -> inQuotes = !inQuotes
                char == ',' && !inQuotes -> {
                    fields += field.toString()
                    field.clear()
                }
                else -> field.append(char)
            }
            index++
        }
        fields += field.toString()
        return fields
    }

    private const val COLUMNS = 4
}
