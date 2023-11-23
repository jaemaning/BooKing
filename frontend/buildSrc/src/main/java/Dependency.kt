import Versions.NAV_VERSION
import Versions.ROOM_VERSION

object Versions {
    const val NAV_VERSION = "2.5.3"
    const val ROOM_VERSION = "2.5.0"
}

object Kotlin {
    const val SDK = "org.jetbrains.java:java-stdlib-jdk8:1.5.21"
}

object AndroidX {
    const val MATERIAL = "androidx.compose.material:material:1.0.0-rc02"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.1.0"
    const val APP_COMPAT = "androidx.appcompat:appcompat:1.3.1"
    const val LEGACY = "androidx.legacy:legacy-support-v4:1.0.0"
    const val LIFECYCLE_VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
    const val LIFECYCLE_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
    const val ACTIVITY = "androidx.activity:activity-ktx:1.3.1"
    const val FRAGMENT = "androidx.fragment:fragment-ktx:1.3.6"
    const val DATASTORE = "androidx.datastore:datastore-preferences:1.0.0"
}

object KTX {
    const val CORE = "androidx.core:core-ktx:1.12.0"
}

object Google {
    const val MATERIAL = "com.google.android.material:material:1.4.0"
    const val GOOGLE_AUTH = "com.google.android.gms:play-services-auth:20.7.0"
}

object Test {
    const val JUNIT = "junit:junit:4.+"
    const val ANDROID_JUNIT_RUNNER = "AndroidJUnitRunner"
}

object AndroidTest {
    const val EXT_JUNIT = "androidx.test.ext:junit:1.1.3"
    const val TEST_RUNNER = "androidx.test:runner:1.4.0"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.4.0"
}

object DaggerHilt {
    const val DAGGER_HILT = "com.google.dagger:hilt-android:2.48.1"
    const val DAGGER_HILT_COMPILER = "com.google.dagger:hilt-android-compiler:2.48.1"
//    const val DAGGER_HILT_VIEW_MODEL = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    const val DAGGER_HILT_VIEW_MODEL = "androidx.hilt:hilt-navigation-compose:1.0.0"
    const val DAGGER_HILT_ANDROIDX_COMPILER = "androidx.hilt:hilt-compiler:1.0.0"
}

object Retrofit {
    const val RETROFIT = "com.squareup.retrofit2:retrofit:2.9.0"
    const val CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val CONVERTER_JAXB = "com.squareup.retrofit2:converter-jaxb:2.9.0"
    const val CONVERTER_SCALARS = "com.squareup.retrofit2:converter-scalars:2.9.0"
}

object OkHttp {
    const val OKHTTP = "com.squareup.okhttp3:okhttp:4.12.0"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:4.12.0"
}

object Coroutines {
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2"
}

object CameraX {
    const val CAMERA_CORE = "androidx.camera:camera-core:1.0.2"
    const val CAMERA_CAMERA2 = "androidx.camera:camera-camera2:1.0.2"
    const val CAMERA_LIFECYCLE = "androidx.camera:camera-lifecycle:1.0.2"
    const val CAMERA_VIEW = "androidx.camera:camera-view:1.0.0-alpha29"
    const val CAMERA_EXTENSIONS = "androidx.camera:camera-extensions:1.0.0-alpha29"
}

object NavComponent {
    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:$NAV_VERSION"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:$NAV_VERSION"
    const val NAVIGATION_DYNAMIC_FEATURES_FRAGMENT = "androidx.navigation:navigation-dynamic-features-fragment:$NAV_VERSION"
    const val NAVIGATION_TESTING = "androidx.navigation:navigation-testing:$NAV_VERSION"
    const val NAVIGATION_COMPOSE = "androidx.navigation:navigation-compose:$NAV_VERSION"
}

object Coil {
    const val COIL = "io.coil-kt:coil:2.2.2"
    const val COIL_COMPOSE = "io.coil-kt:coil-compose:2.2.2"
}

object Room {
    const val ROOM_RUNTIME = "androidx.room:room-runtime:$ROOM_VERSION"
    const val ROOM_COMPILER = "androidx.room:room-compiler:$ROOM_VERSION"
    const val ROOM_KTX = "androidx.room:room-ktx:$ROOM_VERSION"
}
