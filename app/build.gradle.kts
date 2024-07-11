plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.daccvo.radioapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.daccvo.radioapp"
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
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation ("com.google.android.exoplayer:exoplayer-core:2.15.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.15.1")

}