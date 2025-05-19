// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.google.devtools.ksp") version "2.1.20-2.0.1" apply false
    kotlin("kapt") version "2.1.20"
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}