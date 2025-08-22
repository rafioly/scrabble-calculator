plugins {
    id("java")
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.zuehlke.gobusiness.scrabble.calculator"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")

    // All test dependencies are now in one scope, available to all tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

// Configure all test tasks to use JUnit Platform
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// Configure the default 'test' task to run only unit tests
tasks.test {
    description = "Runs unit tests."
    filter {
        // Exclude integration tests from the default test run
        excludeTestsMatching("*IntegrationTest")
    }
}

// Define and configure the new 'integrationTest' task
val integrationTest = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    filter {
        // Include only the integration tests
        includeTestsMatching("*IntegrationTest")
    }

    // Ensure integration tests run after unit tests
    shouldRunAfter(tasks.test)
}

// Make the 'check' task (part of the default build lifecycle) run integration tests
tasks.check {
    dependsOn(integrationTest)
}
