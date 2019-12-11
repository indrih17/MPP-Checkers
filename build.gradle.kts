buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    // workaround for https://youtrack.jetbrains.com/issue/KT-27170
    configurations.create("compileClasspath")
}