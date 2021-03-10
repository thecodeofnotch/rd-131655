plugins {
    id("java-library")
}

group = "net.minecraft"
version = "rd-131655"

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl", version = "2.9.3")
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl_util", version = "2.9.3")
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", ext = "pom")
}

task("run", JavaExec::class) {
    jvmArgs = listOf("-Dorg.lwjgl.librarypath=\"${project.projectDir.toPath()}\\run\\natives\"")
    main = "com.mojang.rubydung.RubyDung"
    classpath = sourceSets["main"].runtimeClasspath
}