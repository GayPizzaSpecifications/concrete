package lgbt.mystic.foundation.concrete.sample.helloworld

import lgbt.mystic.foundation.concrete.other.OtherLibrary
import lgbt.mystic.foundation.concrete.sample.common.logOnDisable
import lgbt.mystic.foundation.concrete.sample.common.logOnEnable
import org.bukkit.plugin.java.JavaPlugin

class HelloWorldPlugin : JavaPlugin() {
  override fun onEnable() {
    logOnEnable(OtherLibrary.HELLO_WORLD)
  }

  override fun onDisable() {
    logOnDisable(OtherLibrary.HELLO_WORLD)
  }
}
