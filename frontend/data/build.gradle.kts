import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

// 보안키 설정을 위한 properties 세팅
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val naverClientId = localProperties.getProperty("naverClient_id") ?: ""
val naverClientSecret = localProperties.getProperty("naverClient_secret") ?: ""
val kakaoAK = localProperties.getProperty("kakaoAK") ?: ""

android {
    namespace = "com.ssafy.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "naverClient_id", "\"$naverClientId\"")
        buildConfigField("String", "naverClient_secret", "\"$naverClientSecret\"")
        buildConfigField("String", "kakaoAK", "\"$kakaoAK\"")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit
    implementation (Retrofit.RETROFIT)
    implementation (Retrofit.CONVERTER_GSON)
    implementation (Retrofit.CONVERTER_JAXB)

    //okHttp
//    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
    implementation (OkHttp.OKHTTP)
    implementation (OkHttp.LOGGING_INTERCEPTOR)

    //coroutines
    implementation (Coroutines.COROUTINES)

    // dager hilt
    implementation (DaggerHilt.DAGGER_HILT)
    kapt (DaggerHilt.DAGGER_HILT_COMPILER)
    implementation(DaggerHilt.DAGGER_HILT_VIEW_MODEL)

    // Room
    implementation(Room.ROOM_RUNTIME)
    annotationProcessor(Room.ROOM_COMPILER)
    kapt(Room.ROOM_COMPILER)
    implementation(Room.ROOM_KTX)

    // serialize
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.12.3") // 버전은 적절하게 조절
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")

}