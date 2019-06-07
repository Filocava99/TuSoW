plugins {
    `java-library`
}

group = rootProject.group
version = rootProject.version

val javaVersion: String by project
val tuprologVersion: String by project
val apacheCommonsVersion: String by project
val junitVersion: String by project

dependencies {
    api("it.unibo.alice.tuprolog", "tuprolog", tuprologVersion)
    api("org.apache.commons", "commons-collections4", apacheCommonsVersion)

    testImplementation("junit", "junit", junitVersion)
}

configure<JavaPluginConvention> {
    targetCompatibility = JavaVersion.valueOf("VERSION_1_$javaVersion")
    sourceCompatibility = JavaVersion.valueOf("VERSION_1_$javaVersion")
}
