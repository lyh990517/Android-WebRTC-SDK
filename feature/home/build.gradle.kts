import com.example.build_logic.setNamespace

plugins {
    id("feature")
}

android {
    setNamespace("feature.home")
}

dependencies {
    implementation(projects.webrtcSdk)
    implementation(project(":webrtc-sdk"))
}
