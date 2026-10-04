package io.github.dagonco.gsd.api

import io.github.dagonco.gsd.model.Device
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream

class CsvParserTest {

    @Test
    fun `Should find a device with quoted fields`() {
        val device = CsvParser.findDevice(aCsv(), codename = "oriole", model = "Pixel 6")

        assertEquals(Device("Google", "Pixel 6", "oriole", "Pixel 6"), device)
    }

    @Test
    fun `Should find a device with unquoted fields`() {
        val csv = aCsv(HEADER, "Google,Pixel 6,oriole,Pixel 6")

        val device = CsvParser.findDevice(csv, codename = "oriole", model = "Pixel 6")

        assertEquals(Device("Google", "Pixel 6", "oriole", "Pixel 6"), device)
    }

    @Test
    fun `Should prefer the row matching the model`() {
        val device = CsvParser.findDevice(aCsv(), codename = "capitolhill", model = "AXEN 2K Google TV")

        assertEquals(Device("Axen", "AXEN 2K Google TV", "capitolhill", "AXEN 2K Google TV"), device)
    }

    @Test
    fun `Should match the model ignoring case`() {
        val device = CsvParser.findDevice(aCsv(), codename = "beyond1", model = "sm-g973n")

        assertEquals(Device("Samsung", "Galaxy S10", "beyond1", "SM-G973N"), device)
    }

    @Test
    fun `Should fall back to the first codename match when the model is unknown`() {
        val device = CsvParser.findDevice(aCsv(), codename = "beyond1", model = "SM-G973X")

        assertEquals(Device("Samsung", "Galaxy S10", "beyond1", "SM-G973F"), device)
    }

    @Test
    fun `Should keep commas and escaped quotes inside fields`() {
        val device = CsvParser.findDevice(aCsv(), codename = "tiger_cheets", model = "AOpen Chromebase Mini")

        assertEquals(Device("AOpen", "RK3288 10\" Chromebase, Mini", "tiger_cheets", "AOpen Chromebase Mini"), device)
    }

    @Test
    fun `Should skip rows without a market name`() {
        val device = CsvParser.findDevice(aCsv(), codename = "FJL21", model = "FJL21")

        assertEquals(Device("Fujitsu", "ARROWS ef FJL21", "FJL21", "FJL21"), device)
    }

    @Test
    fun `Should return null when the device is not in the CSV`() {
        val device = CsvParser.findDevice(aCsv(), codename = "generic_arm64", model = "sdk_gphone64_arm64")

        assertNull(device)
    }

    @Test
    fun `Should not match the header`() {
        val device = CsvParser.findDevice(aCsv(), codename = "Device", model = "Model")

        assertNull(device)
    }

    private fun aCsv(vararg lines: String = ROWS): InputStream {
        val bom = byteArrayOf(0xFF.toByte(), 0xFE.toByte())
        val content = lines.joinToString(separator = "\n", postfix = "\n").toByteArray(Charsets.UTF_16LE)
        return ByteArrayInputStream(bom + content)
    }

    private companion object {
        private const val HEADER = "Retail Branding,Marketing Name,Device,Model"
        private val ROWS = arrayOf(
            HEADER,
            "\"\",\"\",\"FJL21\",\"FJL21\"",
            "\"2E (ERC)\",\"2E 2K Google TV\",\"capitolhill\",\"2E 2K Google TV\"",
            "\"AOpen\",\"RK3288 10\"\" Chromebase, Mini\",\"tiger_cheets\",\"AOpen Chromebase Mini\"",
            "\"Axen\",\"AXEN 2K Google TV\",\"capitolhill\",\"AXEN 2K Google TV\"",
            "\"Fujitsu\",\"ARROWS ef FJL21\",\"FJL21\",\"FJL21\"",
            "\"Google\",\"Pixel 6\",\"oriole\",\"Pixel 6\"",
            "\"Samsung\",\"Galaxy S10\",\"beyond1\",\"SM-G973F\"",
            "\"Samsung\",\"Galaxy S10\",\"beyond1\",\"SM-G973N\"",
        )
    }
}
