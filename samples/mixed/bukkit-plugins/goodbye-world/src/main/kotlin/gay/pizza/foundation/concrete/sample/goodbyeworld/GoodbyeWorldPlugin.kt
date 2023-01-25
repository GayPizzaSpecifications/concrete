package gay.pizza.foundation.concrete.sample.goodbyeworld

import gay.pizza.foundation.concrete.sample.common.logOnDisable
import gay.pizza.foundation.concrete.sample.common.logOnEnable
import org.bukkit.plugin.java.JavaPlugin

class GoodbyeWorldPlugin : JavaPlugin() {
  override fun onEnable() {
    logOnEnable("Goodbye World")
  }

  override fun onDisable() {
    logOnDisable("Goodbye World")
  }
}
