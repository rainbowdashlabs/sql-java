import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    java
}

repositories {
    maven { url = uri("https://repo.maven.apache.org/maven2/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.4.0")
        compileOnly("org.jetbrains", "annotations", "24.1.0")
    implementation("org.xerial", "sqlite-jdbc", "3.46.0.0")
    implementation("org.postgresql", "postgresql", "42.7.3")
    implementation("com.zaxxer", "HikariCP", "5.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
}

group = "de.chojo"
version = "1.0"
description = "basicsqlplugin"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("com.mysql", "de.chojo.simplecoins.mysql")
    relocate("com.google", "de.chojo.simplecoins.google")
    relocate("org.mariadb", "de.chojo.simplecoins.mariadb")
    // Don't use minimize. This will remove classes from your database driver which you will need.
    minimize {
        exclude(dependency("mysql:.*:.*"))
    }
    mergeServiceFiles()
}

tasks.test {
    useJUnitPlatform()
    maxHeapSize = "1G"
}
