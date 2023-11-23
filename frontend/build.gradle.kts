buildscript {
    repositories {
        // 기존에 있던 저장소들
        mavenCentral()


    }

    dependencies {
        // 기존에 있던 의존성들
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.5.31-1.0.0")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.1.2" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    
//    형 이거 FCM때문에 잠깐 주석처리할게
//    id("com.google.gms.google-services") version "4.4.0" apply false

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.3.15" apply false
}

