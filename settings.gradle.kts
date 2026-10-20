pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven {
            url = uri("https://mirrors.tencent.com/nexus/repository/maven-tencent/")
        }
    }
    plugins {
        id("org.jetbrains.compose") version "1.7.3"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://mirrors.tencent.com/nexus/repository/maven-tencent/")
        }
    }
}

rootProject.name = "ImagePicker"
include(":imagepicker")
include(":gearui-kit-local")
include(":sample")
project(":gearui-kit-local").projectDir = file("../gearui-kit/gearui-kit")

// Include GearUI-Kit as a composite build so that "com.gearui:gearui-kit" module dependency
// is resolved to the local source. When imagepicker-kotlin is itself included as a composite
// build (e.g. from live-chat), Gradle deduplicates by canonical path and the outer build's
// substitution rule takes precedence, preventing klib duplication.
includeBuild("../gearui-kit") {
    dependencySubstitution {
        substitute(module("com.gearui:gearui-kit")).using(project(":gearui-kit"))
    }
}
