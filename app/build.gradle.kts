plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
<<<<<<< HEAD
}
android {
    compileSdk = 34
    namespace = "campus.tech.kakao.map"
=======
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "campus.tech.kakao.map"
    compileSdk = 34
>>>>>>> origin/step0

    defaultConfig {
        applicationId = "campus.tech.kakao.map"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
<<<<<<< HEAD
        ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }
        dataBinding {
            enable = true
        }
=======
>>>>>>> origin/step0

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
<<<<<<< HEAD
                "proguard-rules.pro"
            )
        }
    }

=======
                "proguard-rules.pro",
            )
        }
    }
>>>>>>> origin/step0
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
<<<<<<< HEAD

=======
>>>>>>> origin/step0
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
<<<<<<< HEAD
        viewBinding = true
=======
        dataBinding = true
        buildConfig = true
>>>>>>> origin/step0
    }
}

dependencies {
<<<<<<< HEAD
    implementation("androidx.test.ext:junit-ktx:1.2.1@arr")
    androidTestImplementation ("org.mockito:mockito-android:3.11.2")
    testImplementation ("org.mockito:mockito-core:3.11.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("androidx.multidex:multidex:2.0.1")
    // Kakao Map SDK - 최신 버전 사용
    implementation("com.kakao.maps.open:android:2.9.5")
    // Kakao SDK
    implementation("com.kakao.sdk:v2-all:2.20.3")
    // 네트워크 라이브러리
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
    // AndroidX 및 기타 라이브러리
=======

>>>>>>> origin/step0
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
<<<<<<< HEAD
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.activity:activity:1.9.0")
    // Retrofit 라이브러리
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // AR 라이브러리
    implementation("com.google.ar.sceneform:core:1.17.1") // ARCore 관련 라이브러리 수정
    // Google Maps 서비스 라이브러리
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.libraries.places:places:3.5.0")
    implementation("androidx.test:core-ktx:1.6.1")
    // 테스트 라이브러리
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

=======
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.kakao.maps.open:android:2.9.5")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.test:core-ktx:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.room:room-ktx:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk-android:1.13.11")
    testImplementation("io.mockk:mockk-agent:1.13.11")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")
}
>>>>>>> origin/step0
