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

        versionName = "0.3.0"
        versionCode = 9
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
        kotlinCompilerExtensionVersion = "1.2.0-rc01"
    }
}

dependencies {
    implementation(project(":sharedCode"))
    implementation("androidx.appcompat:appcompat:1.4.2")

    // Data store
    implementation("androidx.datastore:datastore:1.0.0")

    // Compose
    val composeVersion = "1.2.0-rc01"
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha13")

    // Material Design
    implementation("com.google.android.material:material:1.6.1")
}