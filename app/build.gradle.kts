plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("io.freefair.lombok") version "6.5.0-rc1"
    kotlin("kapt") version "2.1.20"
}

android {
    namespace = "com.cogu.spylook"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cogu.spylook"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
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
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    compileOnly(libs.lombok)
    ksp(libs.room.compiler)
    kapt(libs.lombok.annotation.processor)
    kapt(libs.mapstruct.processor)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.recyclerview)
    implementation(libs.room.runtime)
    implementation(libs.mapstruct)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}