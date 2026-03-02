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
