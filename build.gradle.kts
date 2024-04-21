buildscript {
    val agp_version by extra("8.2.0")
    val agp_version1 by extra("8.3.0")
    val agp_version2 by extra("8.2.1")
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
    repositories {
        google()
        mavenCentral()

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}
