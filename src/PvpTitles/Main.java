package PvpTitles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public static FileConfiguration config;
	public static HashMap<Player, Pvper> pvpers;
	public static HashMap<String, Integer> rank;
	public static sql sq;
	@Override
	public void onEnable() {
		rank = new HashMap();
		saveDefaultConfig();
		config = getConfig();
		pvpers = new HashMap();
		getLogger().info("PvpTitles已经载入");
		getLogger().info("作者:Mxxs");
		getLogger().info("QQ:2040005066");
		getCommand("pvpt").setExecutor(new Command());
		Bukkit.getPluginManager().registerEvents(this, this);
		// PlaceholderAPI
		sq = new sql(config.getConfigurationSection("mysql").getString("name"),
				config.getConfigurationSection("mysql").getString("pwd"),
				config.getConfigurationSection("mysql").getString("ip"),
				config.getConfigurationSection("mysql").getString("table"),
				config.getConfigurationSection("mysql").getString("port"),
				config.getConfigurationSection("mysql").getString("data"));
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (Player p : pvpers.keySet()) {

					sq.save(p.getName(), pvpers.get(p));

				}
			}

		}, 100L, config.getInt("savetime") * 20l);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				rank = gettop();
			}

		}, 0L, config.getInt("update") * 20l);
		new Test(this).hook();
	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		Player en = null;
		Player d = null;
		if (e.getEntity() instanceof Player) {
			en = (Player) e.getEntity();
		}
		if (e.getEntity().getKiller() instanceof Player) {
			d = e.getEntity().getKiller();
		}
		if (e.getEntity().getKiller() instanceof Projectile
				&& ((Projectile) e.getEntity().getKiller()).getShooter() instanceof Player) {
			d = (Player) ((Projectile) e.getEntity().getKiller()).getShooter();
		}

		if (en != null && d != null) {
			Calendar c = Calendar.getInstance();
			int time = c.SECOND + c.MINUTE * 60 + c.HOUR_OF_DAY * 60 * 60;
			Pvper enp = pvpers.get(en);
			Pvper dp = pvpers.get(d);
			if (time - enp.getDeathtime() < 0 || time - enp.getDeathtime() >= config.getInt("limittime")) {
				enp.setDeathtime();
				int score = (int) Math.floor(enp.getKillscore() * config.getDouble("rate"));
				en.sendMessage(config.getConfigurationSection("deathmessage").getString("deather")
						.replace("%player%", d.getName()).replace("%killscore%", String.valueOf(score)));
				d.sendMessage(config.getConfigurationSection("deathmessage").getString("killer")
						.replace("%player%", en.getName())
						.replace("%killscore%", String.valueOf(Math.floor(score * config.getDouble("deatherdrop")))));
				dp.setKillscore(dp.getKillscore() + score);
				enp.setKillscore((int) (dp.getKillscore() - Math.floor(score * config.getDouble("deatherdrop"))));
				if (dp.getData() != c.DAY_OF_YEAR) {
					dp.setData(c.DAY_OF_YEAR);
					dp.setTodayscore(0);
				} else {
					if (dp.getTodayscore() + config.getInt("score") <= config.getInt("day-score")) {
						dp.setScore(dp.getScore() + config.getInt("score"));
						dp.setTodayscore(dp.getTodayscore() + config.getInt("score"));
						d.sendMessage(
								config.getString("scoremessage").replace("%score%", String.valueOf(dp.getScore())));
					}
				}
				check(enp, dp, en, d);
			}
		}
	}

	private void check(Pvper enp, Pvper dp, Player en, Player d) {
		// TODO Auto-generated method stub
		if (enp.getLevel() != 0 && enp.getKillscore() < config.getConfigurationSection("level")
				.getConfigurationSection(String.valueOf(enp.getLevel())).getInt("need")) {
			enp.setLevel(enp.getLevel() - 1);
			enp.setTitle(config.getConfigurationSection("level").getConfigurationSection(String.valueOf(enp.getLevel()))
					.getString("name"));
			en.sendMessage(config.getString("leveldownmessage").replace("%name%", enp.getTitle()));
		}
		if (dp.getKillscore() > config.getConfigurationSection("level")
				.getConfigurationSection(String.valueOf(dp.getLevel() + 1)).getInt("need")) {
			dp.setLevel(dp.getLevel() + 1);
			dp.setTitle(config.getConfigurationSection("level").getConfigurationSection(String.valueOf(dp.getLevel()))
					.getString("name"));
			d.sendMessage(config.getString("levelupmessage").replace("%name%", dp.getTitle()));
		}

	}

	public HashMap<String, Integer> gettop() {
		HashMap<String, Integer> ranks = sql.get();
		if (ranks.size() < 10) {
			return null;
		}
		List<String> rankplayer = new ArrayList();
		List<Integer> rankscore = new ArrayList();
		for (String name : ranks.keySet()) {
			if (rankplayer.size() == 0) {
				rankplayer.add(name);
				rankscore.add(ranks.get(name));
				continue;
			}
			for (int i = 0; i < 10; i++) {
				if (rankscore.get(i) < ranks.get(name)) {
					rankscore.add(i, ranks.get(name));
					rankplayer.add(i, name);
					continue;
				}
				if (rankplayer.size() == i) {
					rankplayer.add(name);
					rankscore.add(ranks.get(name));
					continue;
				}
			}
		}
		HashMap<String, Integer> neww = new HashMap();
		for (int i = 0; i < 10; i++) {
			neww.put(rankplayer.get(i), rankscore.get(i));
		}
		return neww;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		sq.load(e.getPlayer().getName());
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getName().equalsIgnoreCase(Main.config.getString("shopname"))) {
			if (e.getWhoClicked().isOp() && Command.isAdmin) {
				return;
			}
			e.setCancelled(true);
			if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
					&& e.getCurrentItem().getItemMeta().hasLore()
					&& e.getCurrentItem().getItemMeta().getLore().toString().contains("价值积分:")) {
				if (pvpers.get(e.getWhoClicked()).getScore() < Integer.valueOf(
						e.getCurrentItem().getItemMeta().getLore().toString().split("价值积分: ")[1].split("积分")[0])) {
					e.getWhoClicked().sendMessage(config.getString("scorenotenough"));
				} else {
					e.getWhoClicked().sendMessage(config.getString("buyitem"));
					pvpers.get(e.getWhoClicked()).setScore(pvpers.get(e.getWhoClicked()).getScore() - Integer.valueOf(
							e.getCurrentItem().getItemMeta().getLore().toString().split("价值积分: ")[1].split("积分")[0]));
					e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (Command.isAdmin && e.getPlayer().isOp()) {
			if (e.getInventory().getName().equalsIgnoreCase(Main.config.getString("shopname"))) {
				FileConfiguration shop = YamlConfiguration.loadConfiguration(new File("plugins/PvpTitles/shop.yml"));
				for (int i = 0; i < e.getInventory().getSize(); i++) {
					if (e.getInventory().getItem(i) != null) {
						shop.set(String.valueOf(i), e.getInventory().getItem(i));
					}
				}
				try {
					shop.save(new File("plugins/PvpTitles/shop.yml"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.getPlayer().sendMessage("保存成功");
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		sq.save(e.getPlayer().getName(), pvpers.get(e.getPlayer()));
	}
}
