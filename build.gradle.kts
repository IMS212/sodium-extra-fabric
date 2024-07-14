plugins {
    id("java")
    id("fabric-loom") version ("1.7.2") apply (false)
}

val MINECRAFT_VERSION by extra { "1.21" }
val NEOFORGE_VERSION by extra { "21.0.76-beta" }
val FABRIC_LOADER_VERSION by extra { "0.15.11" }
val FABRIC_API_VERSION by extra { "0.100.4+1.21" }

// This value can be set to null to disable Parchment.
val PARCHMENT_VERSION by extra { "2024.06.23" }

// https://semver.org/
val MOD_VERSION by extra { "0.5.7" }

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

subprojects {
    apply(plugin = "maven-publish")

    java.toolchain.languageVersion = JavaLanguageVersion.of(21)

    tasks.processResources {
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(mapOf("version" to MOD_VERSION))
        }
    }

    version = MOD_VERSION
    group = "me.flashyreese.mods"

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }
}
