plugins {
    kotlin("multiplatform") version "1.4.21"
    id("maven-publish")
}

group = "org.org.choottd"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.30")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.4.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
            }
        }

        publishing {
            repositories {
                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/choottd/librcon")
                    credentials {
                        username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                        password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                    }
                }
            }
        }
    }
}
