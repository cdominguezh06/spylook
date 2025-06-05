plugins {
    id("com.google.devtools.ksp")
    kotlin("kapt") version "2.1.20"
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.cogu.spylook"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cogu.spylook"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.5.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
dependencies {
    kapt(libs.mapstruct.processor)
    ksp(libs.room.compiler)
    ksp(libs.hilt.android.compiler)
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(libs.room.runtime)
    implementation(libs.legacy.support.v4)
    implementation(libs.hilt.android)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android.v181)
    implementation(libs.recyclerview)
    implementation(libs.mapstruct)
    implementation (libs.converter.gson)
    implementation (libs.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}