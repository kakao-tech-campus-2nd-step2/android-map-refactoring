pluginManagement {
    repositories {
<<<<<<< HEAD
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
=======
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
>>>>>>> origin/step0
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
<<<<<<< HEAD
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

rootProject.name = "android-map-location"
=======
    }
}

rootProject.name = "android-map-refactoring"
>>>>>>> origin/step0
include(":app")
