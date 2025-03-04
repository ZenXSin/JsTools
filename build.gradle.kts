plugins {
    kotlin("jvm") version "2.0.20"
}

group = "zxs"
version = "For-Mindustry-Studio"


repositories{
    mavenCentral()
    maven(url = "https://maven.aliyun.com/repository/public")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(files("./libs/host.jar"))
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}