plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":networking"))
    implementation(project(":shared"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("dev.nightowl.alghortimroyale.core.Bootstrap")
}
