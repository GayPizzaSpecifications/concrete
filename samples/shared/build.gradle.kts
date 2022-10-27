plugins {
  id("lgbt.mystic.foundation.concrete-root")
  id("lgbt.mystic.foundation.concrete-plugin")
}

concrete {
  minecraftServerPath.set("server")
  paperVersionGroup.set("1.19")
  paperApiVersion.set("1.19.2-R0.1-SNAPSHOT")
  acceptServerEula.set(true)
}
