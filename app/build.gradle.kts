plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.java.arapp"
    compileSdk = 36  // Updated to latest stable version

    defaultConfig {
        applicationId = "com.java.arapp"
        minSdk = 26   // Lowered to support more devices (ARCore requires min 24)
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Required when setting minSdk to 24+
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Enable view binding (optional but recommended)
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core)

    // ARCore and Sceneform
    implementation("com.google.ar:core:1.50.0")  // Latest stable ARCore
    implementation("com.gorisse.thomas.sceneform:sceneform:1.23.0")  // Maintained Sceneform fork

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}