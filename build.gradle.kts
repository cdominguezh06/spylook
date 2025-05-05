// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("io.franzbecker.gradle-lombok") version "5.0.0"
    alias(libs.plugins.kotlin.android) apply false
id("com.google.devtools.ksp") version "2.1.20-2.0.1" apply false
}