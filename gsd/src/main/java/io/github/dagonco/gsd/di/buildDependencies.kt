package io.github.dagonco.gsd.di

import android.content.Context
import io.github.dagonco.gsd.api.Request
import io.github.dagonco.gsd.api.Storage
import io.github.dagonco.gsd.data.NetworkDataSource
import io.github.dagonco.gsd.data.Repository
import io.github.dagonco.gsd.data.StorageDataSource

internal fun buildRepository(context: Context): Repository {
    return Repository(
        networkDataSource = NetworkDataSource(request = Request(storage = buildStorage(context))),
        storageDataSource = StorageDataSource(storage = buildStorage(context)),
    )
}

private fun buildStorage(context: Context): Storage {
    return Storage(context = context)
}