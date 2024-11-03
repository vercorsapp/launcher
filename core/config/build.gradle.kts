plugins {
    id("app.vercors.launcher.data")
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(projects.core.storage)
    api(libs.protobuf.kotlin.lite)
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                named("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}