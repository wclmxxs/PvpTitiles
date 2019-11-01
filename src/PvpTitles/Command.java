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
			s.sendMessage("/pvpt shop   ���̵�");
			s.sendMessage("/pvpt rank   �鿴����");
			s.sendMessage("/pvpt edit   ������ر��̵�༭ģʽ");
			s.sendMessage("��Ʒ���lore[��ֵ����: 10����]Ȼ��ŵ��༭ģʽ���̵���,Ȼ��ر��̵걣��");
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("rank")) {
				int i = 1;
				for (String p : Main.rank.keySet()) {
					s.sendMessage("��e" + i++ + ".��a" + p + ": ��c" + Main.rank.get(p));
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
					s.sendMessage("�༭ģʽ�Ѿ�����,����/pvpt shop ���̵�༭���ر�,���ɱ���");
				} else {
					s.sendMessage("�༭ģʽ�Ѿ��ر�");
				}
			}
		}
		return true;
	}

}
