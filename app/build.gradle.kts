plugins {
    application
    id("com.vanniktech.maven.publish") version "0.35.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.assertj.core)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

application {
    mainClass = "com.mammb.code.jpostal.App"
}

tasks.withType<JavaCompile> {
    options.encoding = Charsets.UTF_8.name()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

version = "0.5.1"
group = "com.mammb"
base.archivesName.set("jpostal")

tasks.jar {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to base.archivesName,
            "Implementation-Version" to project.version,
            "Main-Class" to "com.mammb.code.jpostal.App",
        ))
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(group.toString(), base.archivesName.get(), version.toString())
    configure(com.vanniktech.maven.publish.JavaLibrary(
        javadocJar = com.vanniktech.maven.publish.JavadocJar.Javadoc(),
        sourcesJar = true,
    ))
    pom {
        name.set("JPostal")
        description.set("Japan postal code dictionary utility.")
        inceptionYear.set("2020")
        url.set("https://github.com/naotsugu/jpostal")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("naotsugu")
                name.set("Naotsugu Kobayashi")
                url.set("https://github.com/naotsugu/")
            }
        }
        scm {
            url.set("https://github.com/naotsugu/jpostal/")
            connection.set("scm:git:git://github.com/naotsugu/jpostal.git")
            developerConnection.set("scm:git:ssh://git@github.com/naotsugu/jpostal.git")
        }
    }
}
