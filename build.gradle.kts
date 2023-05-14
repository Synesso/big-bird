import org.jetbrains.compose.desktop.application.dsl.TargetFormat

group = "tk.satsophone"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenLocal()
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.decompose.core)
                implementation(libs.decompose.extensions)
                implementation(libs.nostrino)
                implementation(libs.okHttp)
                implementation(libs.slf4jSimple)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "BigBird"
            packageVersion = "1.0.0"
        }
    }
}
