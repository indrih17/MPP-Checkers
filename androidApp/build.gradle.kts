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
        viewBinding = true
    }
}

dependencies {
    implementation(project(":sharedCode"))
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
}