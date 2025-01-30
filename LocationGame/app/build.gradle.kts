plugins {
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "jp.ac.ritsumei.ise.phy.exp2.is0691ve.locationgame"
    compileSdk = 34

    defaultConfig {
        applicationId = "jp.ac.ritsumei.ise.phy.exp2.is0691ve.locationgame"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Google Play Services Maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    // Google Play Services Location
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Google Maps Utils
    implementation("com.google.maps.android:android-maps-utils:2.4.0")

    // USBシリアル通信ライブラリを追加
    implementation("com.github.martinleopold:android-serialport-api:1.0.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")
}