plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.citabella.citabella_app"

    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    defaultConfig {
        applicationId = "com.citabella.citabella_app"

        // Android 7+
        minSdk = 24

        // Android 16 / SDK 36
        targetSdk = 36

        versionCode = flutter.versionCode
        versionName = flutter.versionName

        // Evita futuros problemas con plugins/dependencias
        multiDexEnabled = true
    }

    buildTypes {
        release {
            // Temporal para desarrollo y pruebas
            signingConfig = signingConfigs.getByName("debug")

            // Mantener desactivado durante el desarrollo
            isMinifyEnabled = false
            isShrinkResources = false
        }

        debug {
            isDebuggable = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

flutter {
    source = "../.."
}