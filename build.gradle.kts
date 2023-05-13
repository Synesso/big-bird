import org.jetbrains.compose.desktop.application.dsl.TargetFormat

group = "tk.satsophone"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jetbrains.compose") version "1.4.0"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    mavenLocal()
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(libs.nostrino)
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
