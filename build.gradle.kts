import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    application
    java
}

group = "com.db.twitter"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven { // Custom repo for fixed json deserializer
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/Sonicskater/twitter-bot-detection")
        credentials {
            username = "Sonicskater"
            password = "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"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1-fixed_streams")
    implementation("org.apache.commons:commons-text:1.9")
    implementation("org.apache.commons:commons-math3:3.6.1")

    implementation("com.github.haifengl:smile-kotlin:2.6.0") // Natural Language Processing
    implementation("com.github.pemistahl:lingua:1.1.1") // Text Language detection lib
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaExec>(){
    jvmArgs = listOf("-Xms12g","-Xmx12g")
}

application {
    mainClass.set("MainKt")
}