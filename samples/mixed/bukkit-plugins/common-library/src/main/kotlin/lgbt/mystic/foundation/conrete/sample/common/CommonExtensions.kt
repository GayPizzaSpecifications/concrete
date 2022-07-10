package lgbt.mystic.foundation.conrete.sample.common

import org.bukkit.plugin.java.JavaPlugin

fun JavaPlugin.logOnEnable(name: String) = slF4JLogger.info("Enabled $name")
fun JavaPlugin.logOnDisable(name: String) = slF4JLogger.info("Disabled $name")
