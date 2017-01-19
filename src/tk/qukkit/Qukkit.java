package tk.qukkit;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Qukkit extends JavaPlugin {
	
	public void onEnable() {
		getConfig().addDefault("#Messages", "(Color codes supported! Ex: &6&lQukkit)");
		getConfig().addDefault("vanish-off", "&cVanish disabled!&b Everyone can see you.");
		getConfig().addDefault("vanish-on", "&aVanish enabled!&b Noone can see you");
		getConfig().addDefault("no-perms", "&cYou don't have the right permission node.");
		getConfig().addDefault("usage-mute", "&cUsage: /mute [Player]");
		getConfig().addDefault("mute-offline", "&cis offline!");
		getConfig().addDefault("mute-mute", "&bis muted");
		getConfig().addDefault("mute-unmute", "&bis unmuted!");
		getConfig().addDefault("mute-muted", "&cYou are muted... Dummy!");
		getConfig().addDefault("mute-ignored", "&cThis player can't be muted.");
		getConfig().addDefault("main-1", "&6&m-------------------");
		getConfig().addDefault("main-2", "&b/qukkit reload &f- Reload the config file");
		getConfig().addDefault("main-3", "&b");
		getConfig().addDefault("main-4", "&bMore updates coming soon");
		getConfig().addDefault("main-5", "&6&m-------------------");
		getConfig().addDefault("#Settings", "");	
		getConfig().addDefault("ad", true);
		getConfig().addDefault("achievement-fireworks", true);
		saveConfig();
	}
	
	public ArrayList<Player> mute = new ArrayList<Player>();
	public ArrayList<Player> vanish = new ArrayList<Player>();
	
	@EventHandler
	public void onPlayerMuteChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		if(mute.contains(player)) {
			if(!(player.hasPermission("qukkit.deny.mute"))) {
				e.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("mute-muted")));
			}
		}
	}
	
	public void onAchievement(PlayerAchievementAwardedEvent e) {
		Player player = e.getPlayer();
		Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
		FireworkMeta  fwm = fw.getFireworkMeta();
		Random r = new Random();
		if(getConfig().getBoolean("achievement-fireworks", true)) {
			int rt = r.nextInt(4) + 1;
			Type type = Type.BALL;
            if (rt == 1) type = Type.BALL;
            if (rt == 2) type = Type.BALL_LARGE;
            if (rt == 3) type = Type.BURST;
            if (rt == 4) type = Type.CREEPER;
            int r1i = r.nextInt(17) + 1;
            int r2i = r.nextInt(17) + 1;
            Color c1 = getColor(r1i);
            Color c2 = getColor(r2i);
            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
            fwm.addEffect(effect);
            int rp = r.nextInt(2);
            fwm.setPower(rp);
            fw.setFireworkMeta(fwm);
			}
		}
	
	private Color getColor(int r1i) {
		return null;
	}
	
	public void onPlayerJoinAd(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if(getConfig().getBoolean("ad", true)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6This server is using the &bQukkit &6plugin! &eDownload for your server."));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&ohttps://dev.bukkit.org/projects/qukkit"));
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("qukkit")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("main-1")));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("main-2")));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("main-3")));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("main-4")));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("main-5")));
		}
		if(cmd.getName().equalsIgnoreCase("vanish")) {
			if(player.hasPermission("qukkit.vanish")) {
				if(vanish.contains(player)) {
					vanish.remove(player);
					player.showPlayer(player);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("vanish-off")));
				} else {
					vanish.add(player);
					player.hidePlayer(player);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("vanish-on")));
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-perms")));
			}
		}
		if(cmd.getName().equalsIgnoreCase("mute")) {
			if(player.hasPermission("qukkit.mute")) {
				if(args.length != 1) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("usage-mute")));
				} else {
					Player muteplayer = Bukkit.getPlayer(args[0]);
					if(muteplayer == null) {
						player.sendMessage(args[0]+" "+ChatColor.translateAlternateColorCodes('&', getConfig().getString("mute-offline")));
					} else {
						if(mute.contains(muteplayer)) {
							mute.remove(muteplayer);
							player.sendMessage(ChatColor.RED+args[0]+" "+ChatColor.translateAlternateColorCodes('&', getConfig().getString("mute-mute")));
						} else {
							if(muteplayer.hasPermission("qukkit.ignore.mute")) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("mute-ignored")));
							} else {
							mute.remove(muteplayer);
							player.sendMessage(ChatColor.RED+args[0]+" "+ChatColor.translateAlternateColorCodes('&', getConfig().getString("mute-unmute")));
							}
						}
					}
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-perms")));
			}
		}
		if(cmd.getName().equalsIgnoreCase("invsee")) {
			if(player.hasPermission("qukkit.invsee")) {
				Player tplayer = Bukkit.getServer().getPlayer(args[0]);
				Inventory tinv = tplayer.getInventory();
				player.openInventory(tinv);
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-perms")));
			}
		}
		return true;
	}
	
	

}
