import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val hikaricp_version: String by project
val ehcache_version: String by project

plugins {
    application
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "online.k0ras1k.pulse"
version = "0.0.1"
application {
    mainClass.set("online.k0ras1k.pulse.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/") // JitPack repository
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("com.github.lamba92:kotlingram-core:1.2.7")
    implementation("com.github.lamba92:kotlingram-bot-builder:1.2.7")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("mysql:mysql-connector-java:8.0.15")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation(kotlin("stdlib-jdk8"))
    implementation("de.nycode:bcrypt:2.2.0")
    implementation("com.github.zihadmahiuddin:cloudflare-ktor:master-SNAPSHOT")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.2")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.aspectj:aspectjweaver:1.9.19")

    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.ehcache:ehcache:$ehcache_version")

    implementation("com.github.pengrad:java-telegram-bot-api:6.8.0")
    implementation("org.telegram:telegrambots:5.3.0")
    implementation("org.greenrobot:eventbus-java:3.3.1")

    implementation("net.coobird:thumbnailator:0.4.20")
    implementation("com.twelvemonkeys.imageio:imageio-jpeg:3.9.4")

    implementation("org.postgresql:postgresql:42.7.0")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

// Создание задачи для копирования зависимостей
tasks.register("copyDependencies") {
    doLast {
        val libsDir = File("$buildDir/libs")
        libsDir.mkdirs()

        configurations.getByName("runtimeClasspath").files.forEach {
            if (it.name.endsWith(".jar")) {
                it.copyTo(File(libsDir, it.name))
            }
        }
    }
}

tasks.named<Jar>("jar") {
    // Настраиваем имя JAR-файла
    archiveFileName.set("main.jar")

    // Настраиваем директорию для сохранения JAR-файла
    destinationDirectory.set(file("compiled"))
}
