rootProject.name = "core"

include("shared", "networking")
project(":shared").projectDir = file("../shared")
project(":networking").projectDir = file("../networking")

