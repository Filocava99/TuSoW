import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.8.18"
    id("java-library")
    idea
}
//apply(plugin= "com.lightbend.akka.grpc.gradle")

group = "it.unibo.coordaas.tusow.grpc.presentation"
version = "0.7.2-dev03+fb9b118"

repositories {
    mavenCentral()
    mavenLocal()
}

val grpcVersion = "1.43.2"
val protobufVersion = "3.19.3"
val protocVersion = protobufVersion
val grpcKotlinVersion = "1.2.1"

dependencies {
    api(project(":linda-core"))
    api(project(":linda-logic"))
    api(project(":linda-logic-presentation"))
    api(project(":linda-text"))
    api(project(":linda-text-presentation"))
    api(project(":tusow-service"))
    api("io.grpc:grpc-protobuf:${grpcVersion}")
    api("com.google.protobuf:protobuf-java-util:${protobufVersion}")
    api("com.google.protobuf:protobuf-kotlin:${protobufVersion}")
    implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    implementation("com.google.protobuf:protobuf-java-util:${protobufVersion}")
    implementation("ch.qos.logback:logback-classic:_")
    runtimeOnly("io.grpc:grpc-netty-shaded:${grpcVersion}")

    testImplementation("io.grpc:grpc-testing:${grpcVersion}")
    testImplementation("junit:junit:4.12")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

tasks.withType<Test> {
    testLogging.showStandardStreams = true
}

tasks {
    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

task("prepareKotlinBuildScriptModel") {}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}