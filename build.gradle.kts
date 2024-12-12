import java.net.URI

plugins {
    kotlin("jvm") version "2.1.0"
    id("maven-publish")
    java
}

group = property("maven_group")!!
version = property("project_version")!!
val archiveBaseName: String = property("archive_base_name") as String

base {
    archivesName = archiveBaseName
}

repositories {
    mavenCentral()
    maven {
        name = "minecraft"
        url = URI("https://libraries.minecraft.net")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.electronwill.night-config:json:${property("night_config_version")}")
    implementation("com.mojang:brigadier:${property("brigadier_version")}")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

// configure the maven publication
publishing {
	repositories {
		maven("https://maven.enjarai.dev/releases") {
			name = "enjaraiMaven"
			credentials(PasswordCredentials::class.java)
			authentication {
				create<BasicAuthentication>("basic")
			}
		}
	}

	publications {
		create<MavenPublication>("mavenJava") {
			groupId = project.group.toString()
			artifactId = base.archivesName.get()
			version = project.version.toString()
			from(components["java"])
		}
	}
}
