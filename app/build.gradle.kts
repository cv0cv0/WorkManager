import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "me.gr.workmanager"
        renderscriptTargetApi = 21
        renderscriptSupportModeEnabled = true
        vectorDrawables { useSupportLibrary = true }
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

val ankoVersion="0.10.7"
val supportVersion = "1.0.0"
val workVersion = "1.0.0-alpha09"
val retrofitVersion = "2.3.0"
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.anko:anko:$ankoVersion")
    implementation("org.jetbrains.anko:anko-design:$ankoVersion")
    implementation("androidx.appcompat:appcompat:$supportVersion")
    implementation("androidx.cardview:cardview:$supportVersion")
    implementation("com.google.android.material:material:$supportVersion")
    implementation("androidx.legacy:legacy-support-v4:$supportVersion")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha2")
    implementation("android.arch.work:work-runtime:$workVersion")
    implementation("android.arch.work:work-runtime-ktx:$workVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.0")
    implementation("com.github.bumptech.glide:glide:4.8.0")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}
