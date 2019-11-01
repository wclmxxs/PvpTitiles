package PvpTitles;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Command implements CommandExecutor {

	public static boolean isAdmin;
	@Override
	public boolean onCommand(CommandSender s, org.bukkit.command.Command arg1, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 0 && s.isOp()) {
			s.sendMessage("/pvpt shop   打开商店");
			s.sendMessage("/pvpt rank   查看排行");
			s.sendMessage("/pvpt edit   开启或关闭商店编辑模式");
			s.sendMessage("物品添加lore[价值积分: 10积分]然后放到编辑模式的商店中,然后关闭商店保存");
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("rank")) {
				int i = 1;
				for (String p : Main.rank.keySet()) {
					s.sendMessage("§e" + i++ + ".§a" + p + ": §c" + Main.rank.get(p));
				}
			}
			if (args[0].equalsIgnoreCase("shop")) {
				FileConfiguration shop = YamlConfiguration.loadConfiguration(new File("plugins/PvpTitles/shop.yml"));
				Inventory inv = Bukkit.createInventory(null, 54, Main.config.getString("shopname"));
				for (String key : shop.getKeys(false)) {
					if (shop.getItemStack(key) != null) {
						inv.setItem(Integer.valueOf(key), shop.getItemStack(key));
					}

				}
				((Player) s).openInventory(inv);
			}
			if (args[0].equalsIgnoreCase("edit") && s.isOp()) {
				isAdmin = !isAdmin;

				if (isAdmin) {
					s.sendMessage("编辑模式已经开启,输入/pvpt shop 打开商店编辑完后关闭,即可保存");
				} else {
					s.sendMessage("编辑模式已经关闭");
				}
			}
		}
		return true;
	}

}
