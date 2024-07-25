plugins {
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
    id("com.google.dagger.hilt.android") version "2.46.1" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.0" apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}