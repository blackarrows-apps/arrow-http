pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    val arrowHttpRepository = providers.gradleProperty("arrowHttpRepository")
        .orElse("https://repo.maven.apache.org/maven2")

    repositories {
        maven {
            name = "arrowHttpUnderTest"
            url = uri(arrowHttpRepository.get())
            content {
                includeGroup("io.github.blackarrows-apps")
            }
        }
        mavenCentral {
            content {
                excludeGroup("io.github.blackarrows-apps")
            }
        }
    }
}

rootProject.name = "arrow-http-publication-consumer"
