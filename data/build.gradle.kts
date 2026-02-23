plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Supabase BOM (Bill of Materials) - use latest version
    api(platform(libs.bom.v300))

    // Supabase modules you'll need
    api(libs.supabase.postgrest.kt)  // Database
    api(libs.supabase.auth.kt)       // Authentication
    api(libs.supabase.realtime.kt)   // Real-time (for later)

    // Ktor client for Android
    api(libs.ktor.client.android)


    implementation(libs.kotlinx.coroutines.play.services)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)


    implementation(libs.play.services.location)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // For Kotlin coroutines support
    kapt(libs.androidx.room.compiler)
//    ksp(libs.androidx.room.compiler.v250)
}
