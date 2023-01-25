package gay.pizza.foundation.concrete.sample.helloworld

import org.bukkit.plugin.java.JavaPlugin

class HelloWorldPlugin : JavaPlugin() {
  override fun onEnable() {
    slF4JLogger.info("Enabled Hello World")
  }

  override fun onDisable() {
    slF4JLogger.info("Disabled Hello World")
  }
}
