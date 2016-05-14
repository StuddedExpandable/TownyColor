package thevoxstudios.vtownycolor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class townycolor extends JavaPlugin {

	private File configf = null;
	private FileConfiguration config = null;
	private String[] bannedColors = getConfig().getStringList("Banned-Colors").toArray(new String[getConfig().getStringList("Banned-Colors").size()]);
	private String[] bannedAddons = getConfig().getStringList("Banned-Addons").toArray(new String[getConfig().getStringList("Banned-Addons").size()]);	
	private ChatColor color = null;
	
	@Override
	public void onEnable() {
		createConfig();
		Bukkit.getLogger().info("[TownyColor] is now enabled!");
		getCommand("color").setExecutor(this);
		getCommand("color set").setExecutor(this);
		getCommand("color list").setExecutor(this);
		getCommand("color reset").setExecutor(this);
	}
	
	@Override
	public void onDisable() {
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
			ArrayList<String> help = new ArrayList<String>();
			help.add("&8&m---------------------------&8[&aColor Help&8]&8&m---------------------------");
			help.add(" ");
			help.add("&8- &6/color set <color> &8- &7 changes your chat to the request color.");
			help.add("&8- &6/color reset &8- &7Resets your chat to the default color (white)");
			help.add("&8- &6/color list&8- &7Shows the list of allowed colors");
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
			ArrayList<String> bannedcolors = new ArrayList<String>();
			bannedcolors.add("&d");
			bannedcolors.add("&c");
			bannedcolors.add("&b");
			config.set("Banned-Colors", bannedcolors);
			ArrayList<String> bannedaddons = new ArrayList<String>();
			bannedaddons.add("&m");
			bannedaddons.add("&l");
			bannedaddons.add("&n");
			config.set("Banned-Addons", bannedaddons);
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
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		Player p = (Player)s;
		if (cmd.getName().equalsIgnoreCase("color") || cmd.getName().equalsIgnoreCase("c")) {
			if (p.hasPermission("vox.towny.color.use")) {
				if (s instanceof Player) {
					if (args.length == 0) {
						for (String chelp : getConfig().getStringList("Messages.Help")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', chelp));
						}
					}
			        if (args.length > 1 && args[0].equalsIgnoreCase("set")) {
					    try {
						    color = ChatColor.valueOf(args[0].toUpperCase());
					    } catch (NullPointerException e) {
						    p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Mesages.ErrorOccured")));
						    Bukkit.getLogger().info("[TownyColor] Problem occured when trying to get color from " + p.getName());
						    Bukkit.getLogger().info("[TownyColor] Caused by " + e.getMessage());
					    }
					if (color.equals(bannedAddons) || (color.equals(bannedColors))) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.IllegalArg")));
					} else {
						getServer().dispatchCommand(getServer().getConsoleSender(),"pex user " + p.getName() + " set suffix " + color.toString());
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.SetColorTo").replace("<color>", (CharSequence) color)));
					}
					return true;
			        }
			        if (args.length > 1 && args[0].equalsIgnoreCase("list")) {
							for (String cclist : getConfig().getStringList("Messages.AvalibleColors")) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', cclist));
							}
							return true;
			        }
				    if (args.length > 1 && args[0].equalsIgnoreCase("reset")) {
						    getServer().dispatchCommand(getServer().getConsoleSender(), "pex user " + p.getName() + " set suffix " + ChatColor.RESET);
						    p.sendMessage(ChatColor.translateAlternateColorCodes('&',getConfig().getString("Messages.ResetColor"))); 
				    }
				    return true;
				}
			}
		}
    return true;
	}
}
