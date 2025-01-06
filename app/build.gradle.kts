plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.example.recipe_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recipe_app"
        minSdk = 24
        targetSdk = 34
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
            buildConfigField("String", "API_KEY", "\"9b31a6de51e34ce89699ac0e7ac03199\"")
        }
        debug {
            buildConfigField("String", "API_KEY", "\"9b31a6de51e34ce89699ac0e7ac03199\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.adapters)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v261)
    implementation(libs.androidx.recyclerview)
    implementation(libs.glide)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    kapt(libs.compiler)

    //Firebase BoM (Bill of Materials) to manage Firebase library versions
    implementation(platform(libs.firebase.bom))
    //Add Firebase dependencies without specifying versions (managed by BoM)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.jsoup.jsoup)
//    implementation(libs.androidx.navigation.fragment.ktx.v273)
//    implementation(libs.androidx.navigation.ui.ktx.v273)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Firebase BoM (Bill of Materials) to manage Firebase library versions
    implementation(libs.firebase.bom.v3211) // Use the latest BoM version
    implementation (libs.retrofit.v290) //for edamam reftrofit
    implementation (libs.converter.gson.v290)

    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

}

apply(plugin = "com.google.gms.google-services")
