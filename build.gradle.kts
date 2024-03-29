import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    jacoco
    `maven-publish`
}

group = "io.gabrielczar"
version = "0.1"

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit:junit:4.12")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    val codeCoverageReport by creating(JacocoReport::class) {
        executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))

        subprojects.onEach {
            sourceSets(it.sourceSets["main"])
        }

        classDirectories.setFrom(files(sourceSets["main"].output))
        sourceDirectories.setFrom(files(sourceSets["main"].allSource.srcDirs))

        reports {
            xml.destination = File("$buildDir/reports/jacoco/report.xml")
            html.isEnabled = false
            csv.isEnabled = false
            xml.isEnabled = true
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("KAESCrypt")
                url.set("https://github.com/GabrielCzar/KAESCrypt")
                description.set("String extension to encrypting & decrypting strings using AES")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/GabrielCzar/KAESCrypt/blob/master/LICENSE")
                    }
                }
            }
        }
    }
}

