import kotlin.io.path.name

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    application
}

val ktorVersion = libs.versions.ktor.get()

group = "by.funduk"
version = "1.0.0"
application {
    mainClass.set("by.funduk.web.MainKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

val browserDist by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.html.builder)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation("org.mindrot:jbcrypt:0.4")
    implementation(libs.ktor.serialization.json)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.h2)
    testImplementation(libs.h2)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
    browserDist(project(":web:archive", "browserDist"))
}

tasks.withType<Copy>().named("processResources") {
    from(browserDist)
    eachFile {
        val pathElements = file.toPath().toString().split(File.separator)
        val moduleName = pathElements.getOrNull(pathElements.indexOf("web") + 1) ?: throw GradleException("UNKNOWN MODULE")
        relativePath = relativePath.prepend("static/$moduleName")
    }
}