plugins {
  id("gay.pizza.foundation.concrete-root")
}

concreteRoot {
  minecraftServerPath.set("server")
  paperServerVersionGroup.set("1.19")
  paperApiVersion.set("1.19.3-R0.1-SNAPSHOT")
  acceptServerEula.set(true)
}
