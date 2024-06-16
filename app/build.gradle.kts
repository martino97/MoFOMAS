plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.mofomas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mofomas"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation (libs.ccp)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database.v2005)
    implementation(libs.firebase.storage)
    implementation (platform(libs.firebase.bom))
    //noinspection UseTomlInstead
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    implementation (libs.chaosleung.pinview)
    implementation (libs.navigation.fragment.ktx)
    implementation (libs.circleimageview)
    implementation (libs.navigation.ui.ktx)
    implementation (libs.glide)
    annotationProcessor(libs.compiler)
    implementation (libs.fab)
    implementation (libs.gson)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

      //  implementation (libs.autoimageslider.v111)
      //mplementation (libs.appcompat.v7)
       //mplementation (libs.design)
       //mplementation (libs.android.support.vector.drawable)


}