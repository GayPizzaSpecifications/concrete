plugins {
  id("gay.pizza.foundation.concrete-plugin")
}

dependencies {
  implementation(project(":other-library"))
  implementation(project(":bukkit-plugins:common-library"))
}

concreteItem {
  dependency(project(":bukkit-plugins:goodbye-world"))
}
