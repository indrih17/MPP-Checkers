import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.kubsu.checkers"

        minSdk = 24
        targetSdk = 32

        versionName = "0.2.2"
        versionCode = 8
    }

    val localProps = Properties()
    rootProject
        .file("local.properties")
        .takeIf { it.exists() }
        ?.also { it.inputStream().use(localProps::load) }

    signingConfigs {
        create("release") {
            storeFile = file("release-key.jks")
            storePassword = localProps.getProperty("storePassword")
            keyAlias = localProps.getProperty("keyAlias")
            keyPassword = localProps.getProperty("keyPassword")
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules-debug.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-beta02"
    }
}

val composeVersion = "1.2.0-beta02"

dependencies {
    implementation(project(":sharedCode"))
    implementation("androidx.appcompat:appcompat:1.4.1")

    // Compose
    val composeVersion = "1.2.0-beta02"
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.foundation:foundation:$composeVersion")

    // Material Design
    implementation("com.google.android.material:material:1.6.0")
    implementation("androidx.compose.material3:material3:1.0.0-alpha12")

    // TEA
    implementation("family.amma:keemun:1.2.3")
}