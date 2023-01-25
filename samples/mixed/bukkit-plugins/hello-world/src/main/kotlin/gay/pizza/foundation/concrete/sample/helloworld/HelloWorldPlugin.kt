package gay.pizza.foundation.concrete.sample.helloworld

import gay.pizza.foundation.concrete.other.OtherLibrary
import gay.pizza.foundation.concrete.sample.common.logOnDisable
import gay.pizza.foundation.concrete.sample.common.logOnEnable
import org.bukkit.plugin.java.JavaPlugin

class HelloWorldPlugin : JavaPlugin() {
  override fun onEnable() {
    logOnEnable(OtherLibrary.HELLO_WORLD)
  }

  override fun onDisable() {
    logOnDisable(OtherLibrary.HELLO_WORLD)
  }
}
