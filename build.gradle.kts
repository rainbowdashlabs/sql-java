import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    java
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.23")
    testImplementation("org.junit.jupiter:junit-jupiter:RELEASE")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
}

group = "de.chojo"
version = "1.0-SNAPSHOOOOOOT"
description = "mArIa-Db-BaSiCs"
java.sourceCompatibility = JavaVersion.VERSION_15

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("com.mysql", "de.chojo.simplecoins.mysql")
    minimize()
    mergeServiceFiles()
}