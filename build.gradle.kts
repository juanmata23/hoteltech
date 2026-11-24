plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.tech"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

ext["jetty.version"] = "11.0.24"

repositories {
    mavenCentral()
}

dependencies {
    // 1. Adaptador Web HTML (Spring MVC con Undertow + Thymeleaf + Seguridad por roles)
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.5") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow:3.4.5")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.4.5")
    implementation("org.springframework.boot:spring-boot-starter-security:3.4.5")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.3.RELEASE")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.4.5")

    // 2. Adaptador API REST (Javalin 6.1.3 con su Jetty 11 completo)
    implementation("io.javalin:javalin:6.1.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")

    // 3. Adaptador Persistencia (JDBI 3 + SQLite)
    implementation("org.jdbi:jdbi3-core:3.47.0")
    implementation("org.jdbi:jdbi3-sqlobject:3.47.0")
    implementation("org.xerial:sqlite-jdbc:3.47.1.0")

    // 4. Adaptador Reportes PDF (OpenPDF)
    implementation("com.github.librepdf:openpdf:2.0.3")

    // 5. Pruebas Unitarias (JUnit 5)
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
}

tasks.test {
    useJUnitPlatform()
}