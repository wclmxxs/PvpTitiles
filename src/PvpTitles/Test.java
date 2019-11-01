package PvpTitles;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class Test extends EZPlaceholderHook {
	public Test(Main main) {
		super(main, "pvptitles");
}
@Override
	public String onPlaceholderRequest(Player p, String indentifier) {
		if (p == null) {
			return new String();
		}
		if (indentifier.equals("killscore")) {
			// 注意这里要使用变量时是 %customplaceholder_test%
			return String.valueOf(Main.pvpers.get(p).getKillscore());
		}
		if (indentifier.equals("score")) {
			// 注意这里要使用变量时是 %customplaceholder_test%
			return String.valueOf(Main.pvpers.get(p).getScore());
		}
		if (indentifier.equals("title")) {
			// 注意这里要使用变量时是 %customplaceholder_test%
			return String.valueOf(Main.pvpers.get(p).getTitle());
		}
		int i = 1;
		for (String pp : Main.rank.keySet()) {
			if (indentifier.equals("paimin_" + "killscore" + "_" + i)) {
				// 注意这里要使用变量时是 %customplaceholder_test%
				return String.valueOf(Main.rank.get(pp));
			}
			if (indentifier.equals("paimin_" + "player" + "_" + i)) {
				// 注意这里要使用变量时是 %customplaceholder_test%
				return pp;
			}
			i++;
		}
		if (indentifier.equals("next_killscore")) {
			// 注意这里要使用变量时是 %customplaceholder_test%
			return String.valueOf(Main.config.getConfigurationSection("level")
					.getConfigurationSection(String.valueOf(Main.pvpers.get(p).getLevel() + 1)).getInt("need"));
		}
		if (indentifier.equals("next_title")) {
			// 注意这里要使用变量时是 %customplaceholder_test%
			return String.valueOf(Main.config.getConfigurationSection("level")
					.getConfigurationSection(String.valueOf(Main.pvpers.get(p).getLevel() + 1)).getString("name"));
}
		return new String();
}
}