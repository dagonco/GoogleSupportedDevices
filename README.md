# GoogleSupportedDevices

[![Maven Central][mavenbadge-svg]][mavencentral]

A library for Android to get the market name of the device.

<!-- GETTING STARTED -->
## Getting Started

Include the library in your `build.gradle`.

```groovy
implementation 'io.github.dagonco:gsd:{version}'
```

Add the INTERNET permission to your `AndroidManifest.xml`.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Usage

To use this library you will need to build an instance of _GoogleSupportedDevices_ and invoke _getDevice()_.

This method is **suspended**. Remember to call it inside a coroutine scope.

```kotlin
val gsd = GoogleSupportedDevices(context)
gsd.getDevice()
```

If the device is not yet registered by Google, a model with the following default values will be returned.
```kotlin
codename = Build.DEVICE
manufacturer = Build.MANUFACTURER
marketName = Build.MODEL
model = Build.MODEL
```

Otherwise, the values that Google has registered will be taken.


[mavenbadge-svg]: https://img.shields.io/maven-central/v/io.github.dagonco/gsd.svg?label=Maven%20Central
[mavencentral]: https://search.maven.org/artifact/io.github.dagonco/gsd
