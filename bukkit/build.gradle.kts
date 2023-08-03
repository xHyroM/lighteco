plugins {
    id("lighteco.platform-logic")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":lighteco-common"))

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    compileOnly("org.checkerframework:checker-qual:3.8.0")
    compileOnly("org.jetbrains:annotations:20.1.0")
}