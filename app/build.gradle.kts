plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.sixthapplication"
    compileSdk = 33


    dexOptions {
        javaMaxHeapSize= "4G"
    }
    defaultConfig {
        applicationId = "com.example.sixthapplication"
        minSdk = 28
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        mlModelBinding = true
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.tensorflow:tensorflow-lite-support:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("org.tensorflow:tensorflow-lite-select-tf-ops:2.3.0")
    implementation ("org.tensorflow:tensorflow-lite:2.12.0")
//    implementation ("org.pytorch:pytorch_android_lite:1.12.2")
//    implementation ("org.pytorch:pytorch_android_torchvision_lite:1.12.2")
//    implementation 'org.pytorch:pytorch_android_lite:1.9.0'
//    implementation ("org.pytorch:pytorch_android_torchvision:1.12.2")
    implementation ("org.pytorch:pytorch_android_lite:1.12.2")
    implementation ("org.pytorch:pytorch_android_torchvision_lite:1.12.2")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation ("com.opencsv:opencsv:5.2")
}