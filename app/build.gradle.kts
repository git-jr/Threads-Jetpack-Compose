import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleServices)
}

val properties = Properties().apply {
    FileInputStream(rootProject.file("local.properties")).use { load(it) }
}

android {
    namespace = "com.paradoxo.threadscompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.paradoxo.threadscompose"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // To generate a new facebook app id, go to https://developers.facebook.com/apps/
        // and create a new app. Then, go to Settings > Basic and copy the App ID.
        // Finally, paste the App ID in the facebookAppId on file local.properties
        val facebookAppId = properties.getProperty("facebookAppId") ?: "INSIRA O ID DO SEU APP AQUI"
        resValue("string", "facebook_app_id", facebookAppId)

        val facebookClientToken = properties.getProperty("facebookClientToken") ?: "INSIRA O TOKEN DO SEU APP AQUI"
        resValue("string", "facebook_client_token", facebookClientToken)

        val fbLoginProtocolScheme = "fb${facebookAppId}"
        resValue("string", "fb_login_protocol_scheme", fbLoginProtocolScheme)

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
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation (libs.lottie.compose)
    implementation(libs.androidx.foundation)

    implementation(platform(libs.firebase.bom))
    implementation(libs.coil.compose)
    implementation(libs.google.services)
    implementation (libs.facebook.android.sdk)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.storage.ktx)


    implementation(libs.lifecycle.viewmodel)
    implementation(libs.navigation.compose)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.auth)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}