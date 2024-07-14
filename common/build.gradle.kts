import net.fabricmc.loom.task.AbstractRemapJarTask

plugins {
    id("java")
    id("idea")
    id("fabric-loom") version "1.7.2"
}

repositories {
    maven("https://maven.parchmentmc.org/")
}

val MINECRAFT_VERSION: String by rootProject.extra
val PARCHMENT_VERSION: String? by rootProject.extra
val FABRIC_LOADER_VERSION: String by rootProject.extra
val FABRIC_API_VERSION: String by rootProject.extra

// This trick hides common tasks in the IDEA list.
tasks.configureEach {
    group = null
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = MINECRAFT_VERSION)
    mappings(loom.layered() {
        officialMojangMappings()
        if (PARCHMENT_VERSION != null) {
            parchment("org.parchmentmc.data:parchment-${MINECRAFT_VERSION}:${PARCHMENT_VERSION}@zip")
        }
    })
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")
    compileOnly("net.fabricmc:sponge-mixin:0.13.2+mixin.0.8.5")

    fun addDependentFabricModule(name: String) {
        val module = fabricApi.module(name, FABRIC_API_VERSION)
        modCompileOnly(module)
    }

    addDependentFabricModule("fabric-api-base")
    addDependentFabricModule("fabric-block-view-api-v2")
    addDependentFabricModule("fabric-renderer-api-v1")
    addDependentFabricModule("fabric-rendering-data-attachment-v1")

    modCompileOnly("net.fabricmc.fabric-api:fabric-renderer-api-v1:3.2.9+1172e897d7")
    implementation(group = "com.lodborg", name = "interval-tree", version = "1.0.0")

    modImplementation(files(rootDir.resolve("sodium-fabric.jar")))
}

tasks.withType<AbstractRemapJarTask>().forEach {
    it.targetNamespace = "named"
}

loom {
    mixin {
        defaultRefmapName = "sodium-extra.refmap.json"
    }

    accessWidenerPath = file("src/main/resources/sodium-extra.accesswidener")
}