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
			// ע������Ҫʹ�ñ���ʱ�� %customplaceholder_test%
			return String.valueOf(Main.pvpers.get(p).getKillscore());
		}
		if (indentifier.equals("score")) {
			// ע������Ҫʹ�ñ���ʱ�� %customplaceholder_test%
			return String.valueOf(Main.pvpers.get(p).getScore());
		}
		if (indentifier.equals("title")) {
			// ע������Ҫʹ�ñ���ʱ�� %customplaceholder_test%
			return String.valueOf(Main.pvpers.get(p).getTitle());
		}
		int i = 1;
		for (String pp : Main.rank.keySet()) {
			if (indentifier.equals("paimin_" + "killscore" + "_" + i)) {
				// ע������Ҫʹ�ñ���ʱ�� %customplaceholder_test%
				return String.valueOf(Main.rank.get(pp));
			}
			if (indentifier.equals("paimin_" + "player" + "_" + i)) {
				// ע������Ҫʹ�ñ���ʱ�� %customplaceholder_test%
				return pp;
			}
			i++;
		}
		if (indentifier.equals("next_killscore")) {
			// ע������Ҫʹ�ñ���ʱ�� %customplaceholder_test%
			return String.valueOf(Main.config.getConfigurationSection("level")
					.getConfigurationSection(String.valueOf(Main.pvpers.get(p).getLevel() + 1)).getInt("need"));
		}
		if (indentifier.equals("next_title")) {
			// ע������Ҫʹ�ñ���ʱ�� %customplaceholder_test%
			return String.valueOf(Main.config.getConfigurationSection("level")
					.getConfigurationSection(String.valueOf(Main.pvpers.get(p).getLevel() + 1)).getString("name"));
}
		return new String();
}
}