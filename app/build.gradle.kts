plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.sukhman_lab07"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sukhman_lab07"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.ext.junit)
    implementation(libs.espresso.core)
    testImplementation("junit:junit:4.13.2")
    testImplementation(" org.robolectric:robolectric:4.8")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    implementation("com.android.volley:volley:1.2.1")
}