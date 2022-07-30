import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"

    id("io.quarkus") version "2.11.1.Final"
    id("info.solidsoft.pitest") version "1.7.4"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
}

group = "transaction"
version = "latest"

repositories {
    mavenCentral()
    maven(url = "https://packages.confluent.io/maven")
}

dependencies {
    implementation("com.lectra:koson:1.2.4")
    implementation("org.flywaydb:flyway-mysql")
    implementation("org.jdbi:jdbi3-kotlin:3.32.0")
    implementation("org.flywaydb:flyway-core:9.0.2")
    implementation("org.apache.kafka:kafka-streams:7.2.1-ccs")
    implementation("io.confluent:kafka-streams-avro-serde:7.2.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-flyway")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-jdbc-mysql")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-resteasy-jackson")
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:2.11.1.Final"))

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation("org.testcontainers:mysql")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.strikt:strikt-arrow:0.34.1")
    testImplementation("com.jayway.jsonpath:json-path:2.7.0")
    testImplementation("com.tngtech.archunit:archunit-junit5:0.23.1")
    testImplementation("org.apache.kafka:kafka-streams-test-utils:7.2.1-ccs")
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
tasks.runKtlintCheckOverMainSourceSet { dependsOn(tasks.quarkusGenerateCode) }
tasks.runKtlintCheckOverTestSourceSet { dependsOn(tasks.quarkusGenerateCodeTests) }

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}
