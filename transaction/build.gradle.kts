import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("io.quarkus") version "2.11.1.Final"
    id("info.solidsoft.pitest") version "1.7.4"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.7.10"
    application
}

group = "transaction"
version = "latest"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.lectra:koson:1.2.4")
    implementation("org.jdbi:jdbi3-kotlin:3.32.0")

    implementation("org.flywaydb:flyway-mysql")
    implementation("org.flywaydb:flyway-core:9.0.2")

    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-flyway")
    implementation("io.quarkus:quarkus-jdbc-mysql")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:2.11.1.Final"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation(kotlin("test"))
    testImplementation("org.testcontainers:mysql")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.strikt:strikt-arrow:0.34.1")
    testImplementation("com.jayway.jsonpath:json-path:2.7.0")
    testImplementation("com.tngtech.archunit:archunit-junit5:0.23.1")
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.Dependent")
    annotation("javax.enterprise.context.ApplicationScoped")
}

pitest {
    configure<PitestPluginExtension> {
        mutators.set(
            setOf(
                "VOID_METHOD_CALLS"
            )
        )
        avoidCallsTo.set(setOf("arrow.core.computations", "kotlin.jvm.internal"))
        targetClasses.set(setOf("domain.*"))
        mutationThreshold.set(100)
        timestampedReports.set(false)
        junit5PluginVersion.set("0.15")
    }
}

tasks.test { useJUnitPlatform() }
tasks.check { dependsOn(tasks.pitest) }
tasks.pitest { dependsOn(tasks.test) }

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}