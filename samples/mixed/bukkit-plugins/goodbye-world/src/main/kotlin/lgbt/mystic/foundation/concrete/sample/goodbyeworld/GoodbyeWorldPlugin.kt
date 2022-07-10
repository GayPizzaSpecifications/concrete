package lgbt.mystic.foundation.concrete.sample.goodbyeworld

import org.bukkit.plugin.java.JavaPlugin

class GoodbyeWorldPlugin : JavaPlugin() {
  override fun onEnable() {
    slF4JLogger.info("Enabled Goodbye World")
  }

  override fun onDisable() {
    slF4JLogger.info("Disabled Goodbye World")
  }
}
