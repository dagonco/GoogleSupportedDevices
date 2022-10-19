package io.github.dagonco.gsd.data

import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class RepositoryTest {

    private val networkDataSource: NetworkDataSource = mock()
    private val storageDataSource: StorageDataSource = mock()


    private val sut = Repository(
        networkDataSource,
        storageDataSource
    )

    @Test
    fun `Should return a cached device`() = runTest {
        givenThatThereIsACachedDevice()

        val device = sut.getDevice()

        assertEquals(device, A_CACHED_DEVICE)
    }

    @Test
    fun `Should return a network device`() = runTest {
        givenThatThereIsNotACachedDevice()
        givenThatThereIsANetworkDevice()

        val device = sut.getDevice()

        assertEquals(device, A_NETWORK_DEVICE)
    }


    @Test
    fun `Should cache a network device`() = runTest {
        givenThatThereIsNotACachedDevice()
        givenThatThereIsANetworkDevice()

        sut.getDevice()

        verify(storageDataSource).storeDevice(eq(A_NETWORK_DEVICE))
    }

    @Test
    fun `Should return the default device`() = runTest {
        givenThatThereIsNotACachedDevice()
        givenThatThereIsNotANetworkDevice()
        givenThatThereIsADefaultDevice()

        val device = sut.getDevice()

        assertEquals(device, A_DEFAULT_DEVICE)
    }

    private fun givenThatThereIsACachedDevice() {
        whenever(storageDataSource.getDevice()).thenReturn(flowOf(A_CACHED_DEVICE))
    }

    private fun givenThatThereIsNotACachedDevice() {
        whenever(storageDataSource.getDevice()).thenReturn(flowOf(null))
    }

    private fun givenThatThereIsANetworkDevice() = runTest {
        whenever(networkDataSource.getDevice()).thenReturn(A_NETWORK_DEVICE)
    }

    private fun givenThatThereIsNotANetworkDevice() = runTest {
        whenever(networkDataSource.getDevice()).thenReturn(null)
    }

    private fun givenThatThereIsADefaultDevice() {
        whenever(storageDataSource.getDefaultDeviceInfo()).thenReturn(A_DEFAULT_DEVICE)
    }

    private companion object {
        private val A_CACHED_DEVICE: Device = mock()
        private val A_NETWORK_DEVICE: Device = mock()
        private val A_DEFAULT_DEVICE: Device = mock()
    }
}