plugins {
    `java-library`                                  // define este proyecto como librería
    id("org.openjfx.javafxplugin") version "0.1.0"  // javafx
}

group = "com.ingeniusapps"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.release.set(24) // genera bytecode 24 (Versión actual de SceneBuilder para que los custom controls sean reconocidos).
}

javafx {
    version = "26-ea+14"
    modules = listOf(
        "javafx.base",
        "javafx.controls",
        "javafx.fxml",
        "javafx.graphics",
        "javafx.media",
        "javafx.swing",
        "javafx.web"
    )
}

// -----------------------------------------------------------------
// Dependencies
dependencies {
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("net.lingala.zip4j:zip4j:2.11.5")
    implementation("com.auth0:java-jwt:4.5.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.5")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:4.0.0")
    implementation("org.jsoup:jsoup:1.21.2")
}