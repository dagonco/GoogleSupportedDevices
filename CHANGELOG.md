# Changelog

## 2.1.0

- Fix device lookup: Google's CSV now quotes every field, so no device was being found and the
  `Build` fallback was always returned.
- Match devices by codename and model, so variants that share a codename get the right row.
- Look devices up again after upgrading, since previous versions could have cached a wrong result.
- `Device` is now a data class.
- Declare the `INTERNET` permission in the library manifest.
- Add connection timeouts, respect coroutine cancellation and avoid parallel downloads.
- Update Kotlin to 2.4, kotlinx.serialization to 1.11 and DataStore to 1.2.

## 2.0.0

- Migrate from Moshi to kotlinx.serialization.
- Fix ETag and HTTP 304 handling.
- Update AGP, Gradle, Kotlin and SDK versions.

## 1.1.1

- Handle requests without ETag or connection.

## 1.1.0

- Publish from GitHub Actions.
