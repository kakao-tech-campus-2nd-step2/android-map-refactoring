import java.util.Properties
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.room")
}

android {
    namespace = "campus.tech.kakao.map"
    compileSdk = 34

    room {
        schemaDirectory("$projectDir/schemas")
    }

    defaultConfig {
        applicationId = "campus.tech.kakao.map"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val kakaoApiKey = getApiKey("KAKAO_API_KEY")
        val kakaoRestApiKey = getApiKey("KAKAO_REST_API_KEY")

        buildConfigField("String", "KAKAO_API_KEY", kakaoApiKey)
        buildConfigField("String", "KAKAO_REST_API_KEY", kakaoRestApiKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    kapt {
        correctErrorTypes = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val lifecycle_version = "2.8.3"

    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.kakao.maps.open:android:2.9.7")
    implementation("com.kakao.sdk:v2-all:2.20.3")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.test:core-ktx:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.databinding:databinding-runtime:8.5.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.fragment:fragment-ktx:1.8.1")
    implementation("com.jakewharton.rxbinding4:rxbinding:4.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    kapt("androidx.room:room-compiler:2.6.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk-android:1.13.12")
    testImplementation("io.mockk:mockk-agent:1.13.12")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.13")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")
}

fun getApiKey(key: String): String {
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }
    return properties.getProperty(key, "")
}
