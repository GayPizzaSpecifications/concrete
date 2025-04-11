plugins {
  id("gay.pizza.foundation.concrete-root")
}

concreteRoot {
  minecraftServerPath.set("server")
  paperServerVersionGroup.set("1.21")
  paperApiVersion.set("1.21.4-R0.1-SNAPSHOT")
  acceptServerEula.set(true)
}
