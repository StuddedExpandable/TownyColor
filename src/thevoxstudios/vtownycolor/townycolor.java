package thevoxstudios.vtownycolor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class townycolor extends JavaPlugin implements Listener, CommandExecutor {

	private File configf, colorsf = null;
	private YamlConfiguration config, colors = null;
	
	@Override
	public void onEnable() {
		createColors();
		createConfig();
		Bukkit.getLogger().info("[TownyColor] is now enabled!");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginCommand("color").setExecutor(this);
	}
	
	@Override
	public void onDisable() {
		saveConfig();
		Bukkit.getLogger().info("[TownyColor] is now diabled....cya!");
		
	}
	
	private void createConfig() {
		configf = new File(getDataFolder() + File.separator + "config.yml");
		config = YamlConfiguration.loadConfiguration(configf);
		
		boolean save = false;
		if (!configf.exists()) {
			config.set("Messages.NoPermission", "&cYou do not have permission to do this command!");
			config.set("Messages.BannedColor", "&cYou are not allowed to use <bannedcolor> as it is being used by staff ranks!");
			config.set("Messages.SetColorTo", "&aYou have just set your chat color to <color>!");
			config.set("Messages.IllegalArg", "&cThat is a invalid color arguement! Do /color list to view the list of valid avalible colours!");
			config.set("Messages.ErrorOccured", "&cAn error as occured! Please notify a staff member to get this problem resolved");
			config.set("Messages.ColorReset", "&aYour color has been reset to white!");
			config.set("Messages.FordataGoHere", "&aTo view the list of avalible color please do /color list");
			ArrayList<String> help = new ArrayList<String>();
			help.add("&8&m---------------------------&8[&aColor Help&8]&8&m---------------------------");
			help.add(" ");
			help.add("&8- &6/color set <color> &8- &7 changes your chat to the request color.");
			help.add("&8- &6/color reset &8- &7Resets your chat to the default color (white)");
			help.add("&8- &6/color list &8- &7Shows the list of allowed data");
			help.add(" ");
			help.add("&8&m------------------------------------------------------------------");
			config.set("Messages.Help", help);
			ArrayList<String> colorlist = new ArrayList<String>();
			colorlist.add("&8&m--------------------------&8[&aAvalible Colors&8]&8&m--------------------------");
			colorlist.add(" ");
			colorlist.add("&8- &1DARK_BLUE");
			colorlist.add("&8- &2DARK_GREEN");
			colorlist.add("&8- &5DARK_PURPLE");
			colorlist.add("&8- &aLIGHT_GREEN");
			colorlist.add("&8- &7LIGHT_GREY");
			colorlist.add("&8- &8DARK_GREY");
			colorlist.add("&8- &eYELLOW");
			colorlist.add("&8- &6GOLD");
			colorlist.add("&8- &9BLUE");
			colorlist.add(" ");
			colorlist.add("&8&m---------------------------------------------------------------------");
			config.set("Messages.AvalibleColors", colorlist);
			ArrayList<String> allowedcolors = new ArrayList<String>();
			allowedcolors.add("&1");
			allowedcolors.add("&2");
			allowedcolors.add("&5");
			allowedcolors.add("&a");
			allowedcolors.add("&7");
			allowedcolors.add("&8");
			allowedcolors.add("&e");
			allowedcolors.add("&6");
			allowedcolors.add("&9");
			config.set("Allowed-Colors", allowedcolors);
			save = true;
		}
		if (save) {
			try {
				config.save(configf);
				Bukkit.getLogger().info("[TownyColor] Creating config.yml....");
				Bukkit.getLogger().info("[TownyColor] Created config.yml!");
			} catch (IOException e) {
				Bukkit.getLogger().info("[TownyColor] Failed to create/save config.yml!");
				Bukkit.getLogger().info("[TownyColor] Caused by: " + e.getMessage());
			}
		}
	}
	
	private void createColors() {
		colorsf = new File(getDataFolder() + File.separator + "colors.yml");
		colors = YamlConfiguration.loadConfiguration(colorsf);
		
		boolean save = false;
		if (!colorsf.exists()) {
			save = true;
		}
		
		if (save) {
			try {
				colors.save(colorsf);
				Bukkit.getLogger().info("[TownyColor] Creating colors.yml....");
				Bukkit.getLogger().info("[TownyColor] Created colors.yml!");
			} catch (IOException e) {
				Bukkit.getLogger().info("[TownyColor] Failed to save/create colors.yml!");
				Bukkit.getLogger().info("[TownyColor] Caused by" + e.getMessage());
			}
		}
	}
	
	public void saveColors() {
		try {
			colors.save(colorsf);
		} catch (Exception e) {
			Bukkit.getLogger().info("[TownyColor] Failed to save colors.yml!");
			Bukkit.getLogger().info("[TownyColor] Caused by: " + e.getMessage());
		}
	}
	
	public YamlConfiguration getColors() {
		return (YamlConfiguration) colors;
	}
	
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		try {
		e.setMessage(ChatColor.translateAlternateColorCodes('&', getColors().getString(e.getPlayer().getName())) + e.getMessage());
		} catch (NullPointerException chate) {
			Bukkit.getLogger().info("DB: No color given");
		}
	}
	
	 public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
	      Player p = (Player)s;      
	      if (cmd.getName().equalsIgnoreCase("color")) {
	        if (p.hasPermission("vox.towny.color.use")) {
	          if (args.length == 0) {
	            for (String chelp : getConfig().getStringList("Messages.Help")) {
	              p.sendMessage(ChatColor.translateAlternateColorCodes('&', chelp));
	            }
	          }
	          else if (args.length == 1) {
	            if (args[0].equalsIgnoreCase("list")) {
	              for (String clist : getConfig().getStringList("Messages.AvalibleColors")) {
	                p.sendMessage(ChatColor.translateAlternateColorCodes('&', clist));
	              }
	            }
	            else if (args[0].equalsIgnoreCase("reset")) {
	              p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.ColorReset")));
	              getServer().dispatchCommand(getServer().getConsoleSender(), "pex user " + p.getName() + " set suffix " + ChatColor.RESET);
	            }
	            else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
	            	System.out.println("DB: /color set fired");
	            	if (args[1].equals(getConfig().getStringList("Allowed-Colors"))) {
	            		getColors().set(p.getName(), args[1]);
	            		System.out.println("DB: Got arg");
	            	} else {
	            		p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.IllegalArg")));
	            		System.out.println("DB: Sent IllegalArg message.");
	            	}
	            } 	
	          }
	        }
	      }
	      return true;
	 }
}