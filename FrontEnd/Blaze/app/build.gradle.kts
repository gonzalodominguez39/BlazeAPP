import java.util.regex.Pattern.compile

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.emma.blaze"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.emma.blaze"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        viewBinding = true
    }


}

dependencies {
    /*firebase implementation*/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.messaging)
    /*google Implementation*/
    implementation(libs.play.services.auth)
    implementation (libs.play.services.maps)
    implementation (libs.play.services.location)
    /*cardStackView Implementation*/
    implementation("com.github.yuyakaido:CardStackView:v2.3.4")
    /*Room Implementation*/
    implementation(libs.room.runtime)
    implementation(libs.room.common)
    annotationProcessor(libs.room.compiler)

    /*websockets implementation*/
    implementation(libs.okhttp)
    implementation(libs.retrofit)

    implementation(libs.converter.gson)
    implementation(libs.picasso)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.material.v180)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}