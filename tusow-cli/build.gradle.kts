plugins {
    application
    id("com.github.johnrengelman.shadow")
}

dependencies {
    api(project(":linda-logic-client"))
    api(project(":linda-text-client"))
    api(kotlin("stdlib-jdk8"))
    api(Libs.clikt)

    implementation(Libs.logback_classic)

    testImplementation(Libs.junit)
    testImplementation(project(":tusow-service"))
    testImplementation(project(":linda-test"))
    testImplementation(project(":test-utils"))
}

val mainClass = "it.unibo.coordination.linda.cli.Cli"

application {
    mainClassName = mainClass
}

tasks.getByName<Jar>("shadowJar") {
    manifest {
        attributes("Main-Class" to mainClass)
    }
    archiveBaseName.set(project.name)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("redist")
    from(files("${rootProject.projectDir}/LICENSE"))
}