package org.lichtspiele.EntityRemoverPlugin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.lichtspiele.EntityRemoverPlugin.exception.NoSuchPluginException;
import org.mcstats.Metrics;

import com.nisovin.shopkeepers.ShopkeepersPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class EntityRemoverPlugin extends JavaPlugin {
	
	public static EntityRemoverPlugin instance	= null;
	
	/*
	 * instance stuff
	 */
	private void setInstance(EntityRemoverPlugin instance) {
		EntityRemoverPlugin.instance = (EntityRemoverPlugin) instance;
	}
	
	public static EntityRemoverPlugin getInstance() {
		return instance;
	}
	
	/*
	 * actions that happen when the plugin starts up
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	public void onEnable() {
		// create default directory and config file
		this.getDataFolder().mkdirs();
		this.saveDefaultConfig();
		
		if (this.getConfig().getBoolean("use_metrics")) {
			try {
			    Metrics metrics = new Metrics(this);
			    metrics.start();
			} catch (IOException e) {
				log(Level.INFO, "Failed to submit metrics");
			}			
		}
			
		try {
			this.getWorldGuardPlugin();
			this.getWorldEditPlugin();
		} catch (NoSuchPluginException e) {
			this.disable(e);
		}
		
		this.setInstance(this);
	}
	
	
	/*
	 * external plugins
	 */
	public JavaPlugin getPlugin(String name) throws NoSuchPluginException {
		JavaPlugin p = (JavaPlugin) Bukkit.getServer().getPluginManager().getPlugin(name);
		if (p == null) throw new NoSuchPluginException(name);
		
		return p;
	}
	
	public WorldEditPlugin getWorldEditPlugin() throws NoSuchPluginException {
		return (WorldEditPlugin) getPlugin("WorldEdit");
	}
	
	public WorldGuardPlugin getWorldGuardPlugin() throws NoSuchPluginException {
		return (WorldGuardPlugin) getPlugin("WorldGuard");
	}
	
	public ShopkeepersPlugin getShopkeepersPlugin() throws NoSuchPluginException {
		return (ShopkeepersPlugin) getPlugin("Shopkeepers");
	}
	
	public boolean hasShopkeeperPlugin() {
		try {
			this.getShopkeepersPlugin();
		} catch (NoSuchPluginException e) {
			return false;
		}
		return true;
	}
	
	/*
	 * logging stuff
	 */
	public void log(Level level, String message) {
		Logger.getLogger("Minecraft").log(
			level,
			ChatColor.stripColor(String.format("[EntityRemoverPlugin] %s", message))
		);
	}
	
	public void log(Level level, String message, Throwable t) {
		log(
			level,
			ChatColor.stripColor(String.format("[EntityRemoverPlugin] %s", message)),
			t
		);		
	}
	
	
	/*
	 * disable plugin stuff
	 */
	public void disable(Exception e) {
		log(Level.SEVERE, "Unrecoverable error: " + e.getMessage());
		log(Level.SEVERE, "Disabling plugin");
        this.getPluginLoader().disablePlugin(this);	
	}

	public void disable(Exception e, CommandSender sender) {
		sender.sendMessage("[EntityRemoverPlugin] Unrecoverable error. Disabling plugin");
		this.disable(e);
	}	
	
}
