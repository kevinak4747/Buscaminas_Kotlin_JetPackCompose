plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // KSP genera automáticamente el código de Room (DAO, consultas, etc)
    // sin esto, Room no funciona
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
}

android {
    namespace = "com.example.buscaminas_2dapracticakotlinjeckpackcompose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.buscaminas_2dapracticakotlinjeckpackcompose"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Definimos la versión de Room en una variable para usarla en varias dependencias y mantenerla consistente
    val room_version = "2.6.1"
    // Dependencias para Retrofit, una librería que nos permite hacer peticiones HTTP .
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Esta dependencia es un convertidor que permite transformar las respuestas JSON en objetos de Kotlin.
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Librería principal de Room
    // Permite trabajar con base de datos usando objetos Kotlin
    implementation("androidx.room:room-runtime:$room_version")

    // Extensiones para usar corrutinas (suspend, Flow, etc)
    implementation("androidx.room:room-ktx:$room_version")

    // Procesador que genera el código automáticamente (obligatorio)
    ksp("androidx.room:room-compiler:$room_version")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Añadimos la librería de Navigation Compose.
    // Esta librería nos permite usar NavHost, NavController y composable()
    // para poder cambiar entre pantallas en la app.
    implementation("androidx.navigation:navigation-compose:2.7.7")



}