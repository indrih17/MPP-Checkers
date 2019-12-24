import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        val applicationName = "checkers"
        applicationId = "com.kubsu.$applicationName"

        minSdkVersion(21)
        targetSdkVersion(29)

        versionName = "0.1.2"
        versionCode = 3

        base.archivesBaseName = "${applicationName}_$versionName"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    val local = Properties()
    rootProject.file("local.properties").also { localProperties ->
        if (localProperties.exists()) {
            localProperties.inputStream().use { local.load(it) }
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("release-key.jks")
            storePassword = local.getProperty("storePassword")
            keyAlias = local.getProperty("keyAlias")
            keyPassword = local.getProperty("keyPassword")
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

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }

    sourceSets {
        val main by getting {
            java.srcDirs("src/main/kotlin")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    androidExtensions {
        isExperimental = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":sharedCode"))
    implementation(fileTree("include" to "*.jar", "dir" to "libs"))
    implementation("androidx.appcompat:appcompat:1.1.0")
}