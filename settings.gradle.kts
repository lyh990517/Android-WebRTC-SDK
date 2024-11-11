pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            url = uri("https://raw.githubusercontent.com/alexgreench/google-webrtc/master")
        }
        maven {
            // r8 maven
            url = uri("https://storage.googleapis.com/r8-releases/raw")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://raw.githubusercontent.com/alexgreench/google-webrtc/master")
        }
    }
}
rootProject.name = "WebRTC"
include(":app")
include(":presentaion")
include(":data")
include(":domain")
