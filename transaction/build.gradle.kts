import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("io.quarkus") version "2.11.1.Final"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.7.10"
    application
}

group = "transaction"
version = "latest"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:2.11.1.Final"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation(kotlin("test"))
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.0.0-rc1")
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.Dependent")
    annotation("javax.enterprise.context.ApplicationScoped")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}