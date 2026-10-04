package io.github.dagonco.gsd.api

import io.github.dagonco.gsd.model.Device
import org.junit.Assert.assertEquals
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test
import java.net.URL

/**
 * Runs against the real CSV published by Google to detect format changes.
 * Enabled with the GSD_LIVE_CSV=true environment variable.
 */
class LiveCsvTest {

    @Before
    fun setUp() {
        assumeTrue(System.getenv("GSD_LIVE_CSV") == "true")
    }

    @Test
    fun `Should find a Pixel in the live CSV`() {
        val device = URL(CSV_URL).openStream().use { CsvParser.findDevice(it, codename = "oriole", model = "Pixel 6") }

        assertEquals(Device("Google", "Pixel 6", "oriole", "Pixel 6"), device)
    }

    @Test
    fun `Should find a Galaxy in the live CSV`() {
        val device = URL(CSV_URL).openStream().use { CsvParser.findDevice(it, codename = "beyond1", model = "SM-G973N") }

        assertEquals(Device("Samsung", "Galaxy S10", "beyond1", "SM-G973N"), device)
    }

    private companion object {
        private const val CSV_URL = "https://storage.googleapis.com/play_public/supported_devices.csv"
    }
}
