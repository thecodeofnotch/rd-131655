plugins {
    id("java-library")
}

group = "net.minecraft"
version = "rd-131655"

repositories {
    mavenCentral()
}

val natives: Configuration by configurations.creating
natives.isTransitive = true

dependencies {
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl", version = "2.9.3")
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl_util", version = "2.9.3")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-windows")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-linux")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-osx")
}


task("run", JavaExec::class) {
    jvmArgs = listOf("-Dorg.lwjgl.librarypath=${project.projectDir.toPath()}\\run\\natives")
    main = "com.mojang.rubydung.RubyDung"
    classpath = sourceSets["main"].runtimeClasspath
    workingDir("${project.projectDir.toPath()}\\run")
    dependsOn("extractNatives")
}

task("extractNatives", Copy::class) {
    dependsOn(natives)
    from(natives.map { zipTree(it) })
    into("${project.projectDir.toPath()}\\run\\natives")
}