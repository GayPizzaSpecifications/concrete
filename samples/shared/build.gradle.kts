plugins {
  id("gay.pizza.foundation.concrete-root")
  id("gay.pizza.foundation.concrete-plugin")
}

concrete {
  minecraftServerPath.set("server")
  paperVersionGroup.set("1.19")
  paperApiVersion.set("1.19.3-R0.1-SNAPSHOT")
  acceptServerEula.set(true)
}
