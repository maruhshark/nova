plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
}

buildscript {
    dependencies {
        // Navigation Safe Args Plugin
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.0")
        // Google Maps Secrets Plugin
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}