plugins {
  id("lgbt.mystic.foundation.concrete-plugin")
}

dependencies {
  implementation(project(":other-library"))
  implementation(project(":bukkit-plugins:common-library"))
}
