import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath(kotlin("gradle-plugin", version = "1.3.50"))
  }
}

plugins {
  kotlin("jvm") version "1.3.50"
  kotlin("kapt") version "1.3.50"
}

repositories {
  google()
  jcenter()
  mavenCentral()
}

// FIX KAPT incapacity to find the right variant
// https://youtrack.jetbrains.com/issue/KT-31641
val usage = Attribute.of("org.gradle.usage", Usage::class.java)

dependencies {
  attributesSchema {
    attribute(usage)
  }
}

configurations.all {
    afterEvaluate {
        if (isCanBeResolved) {
            attributes {
                attribute(usage, project.objects.named(Usage::class.java, "java-runtime"))
            }
        }
    }
}
// END FIX KAPT incapacity to find the right variant

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.kodein.di:kodein-di-generic-jvm:6.4.1")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}