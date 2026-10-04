# Google Supported Devices

[![Maven Central][mavenbadge-svg]][mavencentral]
[![CI][cibadge-svg]][ci]
[![API][apibadge-svg]][api]
[![License][licensebadge-svg]][license]

Get the **market name** of an Android device — *Galaxy S10* instead of *SM-G973F* — straight from
Google Play's official list of supported devices.

| `Build.MODEL` | `Build.DEVICE` | Market name              |
|---------------|----------------|--------------------------|
| `SM-G973F`    | `beyond1`      | **Galaxy S10**           |
| `2312DRA50G`  | `garnet`       | **Redmi Note 13 Pro 5G** |
| `CPH2581`     | `OP595DL1`     | **OnePlus 12**           |
| `A065`        | `Pong`         | **Nothing Phone (2)**    |

## Why

`Build.MODEL` returns model codes that mean nothing to your users. Libraries that ship a bundled
device database go stale every time a new phone is launched. This library reads the
[list Google Play publishes](https://support.google.com/googleplay/answer/1727131) at runtime, so new
devices are recognized as soon as Google adds them.

- **Always up to date**: uses Google Play's supported devices list, updated by Google.
- **Downloads once**: the result is cached, so later calls are instant and work offline.
- **Accurate**: matches both codename and model, so variants that share a codename resolve correctly.
- **Never fails**: falls back to `Build` values when the device isn't listed or there is no connection.
- **Lightweight**: no networking library, no API keys, no tracking. Just one request to Google's storage.

## Installation

The library is available on Maven Central.

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.dagonco:gsd:2.1.0")
}
```

<details>
<summary>Groovy / version catalog</summary>

```groovy
// build.gradle
dependencies {
    implementation 'io.github.dagonco:gsd:2.1.0'
}
```

```toml
# gradle/libs.versions.toml
[libraries]
gsd = { module = "io.github.dagonco:gsd", version = "2.1.0" }
```

</details>

The `INTERNET` permission is declared by the library and merged into your app automatically.

## Usage

Create an instance of `GoogleSupportedDevices` and call `getDevice()` from a coroutine.

```kotlin
val gsd = GoogleSupportedDevices(context)

lifecycleScope.launch {
    val device = gsd.getDevice()
    deviceName.text = device.marketName // "Galaxy S10"
}
```

`getDevice()` returns a `Device`:

| Property       | Example      | Source                           |
|----------------|--------------|----------------------------------|
| `manufacturer` | `Samsung`    | Retail branding in Google's list |
| `marketName`   | `Galaxy S10` | Marketing name in Google's list  |
| `codename`     | `beyond1`    | `Build.DEVICE`                   |
| `model`        | `SM-G973F`   | `Build.MODEL`                    |

If the device is not in Google's list, or the list can't be downloaded, the values come from `Build`:

```kotlin
manufacturer = Build.MANUFACTURER
marketName = Build.MODEL
codename = Build.DEVICE
model = Build.MODEL
```

## How it works

1. If the device was found before, it is returned from a local cache (DataStore).
2. Otherwise, the library streams Google's
   [`supported_devices.csv`](https://storage.googleapis.com/play_public/supported_devices.csv)
   and looks for the row matching `Build.DEVICE` and `Build.MODEL`.
3. When the device is found it is cached for good. When it isn't, the list's ETag is stored and later
   calls only download it again when Google publishes a new version.

The first lookup downloads about 5 MB. Call `getDevice()` once, for example at startup, and reuse the
result.

## Sample app

The [`app`](app) module shows the library in a Jetpack Compose screen. Open the project in Android
Studio and run the `app` configuration.

## Contributing

Issues and pull requests are welcome. Run the checks locally with:

```shell
./gradlew :gsd:testDebugUnitTest :gsd:lintDebug
```

Set `GSD_LIVE_CSV=true` to also run the tests against the live CSV published by Google.

## License

```
Copyright 2022 David González

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[mavenbadge-svg]: https://img.shields.io/maven-central/v/io.github.dagonco/gsd.svg?label=Maven%20Central
[mavencentral]: https://central.sonatype.com/artifact/io.github.dagonco/gsd
[cibadge-svg]: https://github.com/dagonco/GoogleSupportedDevices/actions/workflows/ci.yaml/badge.svg
[ci]: https://github.com/dagonco/GoogleSupportedDevices/actions/workflows/ci.yaml
[apibadge-svg]: https://img.shields.io/badge/API-21%2B-brightgreen.svg
[api]: https://apilevels.com/
[licensebadge-svg]: https://img.shields.io/badge/License-Apache%202.0-blue.svg
[license]: LICENSE
