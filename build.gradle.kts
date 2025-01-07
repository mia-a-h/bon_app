// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
buildscript {
    repositories {
        google()  // Ensure this repository is included
        mavenCentral()
    }
    dependencies {
        // Add the Google services classpath
        classpath(libs.google.services) // or latest version
        classpath(libs.gradle)
        //classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}