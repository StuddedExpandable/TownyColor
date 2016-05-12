package thevoxsudios.vtownycolor;

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
	private ChatColor color = null;
	private String[] bannedColors = getConfig().getStringList("Banned-Colors").toArray(new String[getConfig().getStringList("Banned-Colors").size()]);
	private String[] bannedAddons = getConfig().getStringList("Banned-Addons").toArray(new String[getConfig().getStringList("Banned-Addons").size()]);	
	
	
	@Override
	public void onEnable() {
		createConfig();
		Bukkit.getLogger().info("[TownyColor] is now enabled!");
		getCommand("color").setExecutor(this);
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
			config.set("Messages.NoPermission", "You do not have permission to do this command!");
			config.set("Messages.BannedColor", "You are not allowed to use <bannedcolor> as it is being used by staff ranks!");
			config.set("Messages.SetColorTo", "You have just set your chat color to <color>!");
			config.set("Messages.IllegalArg", "That is a invalid color arguement! Do /color list to view the list of valid avalible colours!");
			ArrayList<String> help = new ArrayList<String>();
			help.add("&8&m----------------------------&8[&dColor Help&8]&8&m----------------------------");
			help.add(" ");
			help.add("&8- &6/color <color> &8- &6 changes your chat to the request color. Usage: i.e. /color purple");
			help.add("&8- &6/color reset &8- &6Resets your chat to the default color (white)");
			help.add("&8- &6/color list&8- &6Shows the list of allowed colors");
			help.add(" ");
			help.add("&8&m--------------------------------------------------------------------");
			config.set("Messages.Help", help);
			ArrayList<String> colorlist = new ArrayList<String>();
			colorlist.add("&8&m----------------------------&8[&dAvalible Colors&8]&8&m----------------------------");
			colorlist.add(" ");
			colorlist.add("&8- &1DarkBlue");
			colorlist.add("&8- &2DarkGreen");
			colorlist.add("&8- &5DarkPurple");
			colorlist.add("&8- &eYellow");
			colorlist.add("&8- &6Gold");
			colorlist.add("&8- &7LightGrey");
			colorlist.add("&8- &8DarkGrey");
			colorlist.add("&8- &9Blue");
			colorlist.add(" ");
			colorlist.add("&8&m-------------------------------------------------------------------------");
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
				Bukkit.getLogger().info("[TownyColors] Creating config.yml....");
				Bukkit.getLogger().info("[TownyColors] Crated config.yml!");
			} catch (IOException e) {
				Bukkit.getLogger().info("[TownyColors] Failed to create/save config.yml!");
				Bukkit.getLogger().info("[TownyColors] Caused by: " + e.getMessage());
			}
		}
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		Player p = (Player)s;
		String colorName = args[1].toUpperCase();
		if (cmd.getName().equalsIgnoreCase("color") || cmd.getName().equalsIgnoreCase("c")) {
			if (p.hasPermission("vox.towny.color.use")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Help")));
					return true;
				}				
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("color")) {
						if (s instanceof Player) {
								try {
									color = ChatColor.valueOf(colorName);
								} catch (IllegalArgumentException ex) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.IllegalArg")));
								}
								Bukkit.getLogger().info("[TownyColor] Command executed by " + p.getName());
							}
							} else {
								Bukkit.getLogger().info("[TownyColor] Only players can do this command, noob!");
							}				
				}
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermission")));
			}
		}
    return true;
	}
}
