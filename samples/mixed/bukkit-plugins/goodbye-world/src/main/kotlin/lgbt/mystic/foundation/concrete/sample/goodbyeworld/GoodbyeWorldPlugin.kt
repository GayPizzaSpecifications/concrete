package lgbt.mystic.foundation.concrete.sample.goodbyeworld

import lgbt.mystic.foundation.conrete.sample.common.logOnDisable
import lgbt.mystic.foundation.conrete.sample.common.logOnEnable
import org.bukkit.plugin.java.JavaPlugin

class GoodbyeWorldPlugin : JavaPlugin() {
  override fun onEnable() {
    logOnEnable("Goodbye World")
  }

  override fun onDisable() {
    logOnDisable("Goodbye World")
  }
}
