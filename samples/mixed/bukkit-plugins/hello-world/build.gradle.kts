plugins {
  id("gay.pizza.foundation.concrete-plugin")
}

dependencies {
  implementation(project(":other-library"))
  implementation(project(":bukkit-plugins:common-library"))
}

concrete {
  dependency(project(":bukkit-plugins:goodbye-world"))
}
