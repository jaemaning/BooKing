pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
        maven(url = "https://jitpack.io")
        maven(url = "https://naver.jfrog.io/artifactory/maven/")

    }
}

rootProject.name = "Booking"
include(":app")
include(":data")
include(":domain")
