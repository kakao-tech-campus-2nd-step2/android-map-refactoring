// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.gradle.ktlint) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.protobuf) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
