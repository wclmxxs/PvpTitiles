package PvpTitles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;

public class sql {
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	private static String table;
	private static Connection c;
	static String creatsql;

	public sql(String name, String pwd, String ip, String table, String port, String data) {
		this.table = table;

		driver = "com.mysql.jdbc.Driver";// 驱动程序名
		url = "jdbc:MySQL://" + ip + ":" + port + "/" + data;// url指向要访问的数据库study
		user = name;// MySQL配置时的用户名
		password = pwd;// MySQL配置时的密码
		try {
			Class.forName(driver);// 加载驱动程序
			c = DriverManager.getConnection(url, user, password);// 连接数据库
			if (!c.isClosed())
				System.out.println("数据库连接成功!");
		} catch (ClassNotFoundException e) {
			System.out.println("无法加载数据库驱动!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String checkTable="show tables like \""+table+"\"";
		creatsql = "CREATE TABLE "+table+"("
		        + "player varchar(20) not null,"
		        + "score int(8) not null,"
		        + "todayscore int(8) not null,"
		        + "killscore int(8) not null,"
		        + "title varchar(20) not null,"
		        + "level int(8) not null,"
				+ "deathtime int(8) not null"
				+ ")charset=utf8;";

		try {
			Statement stmt = c.createStatement();
			ResultSet resultSet = stmt.executeQuery(checkTable);
			if (resultSet.next()) {
				System.out.println("数据表已经存在!");
			} else {
				if (stmt.executeUpdate(creatsql) == 0) {
					System.out.println("数据表创建成功!");
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void save(String name,Pvper p) {
		try {
			// 获取Statement对象
			 Statement statement = c.createStatement();
			String sql = "select * from " + table;// 要执行的sql语句
				ResultSet rs = statement.executeQuery(sql);
				boolean isContain = false;
				while (rs.next()) {
					if (rs.getString("player").equalsIgnoreCase(name)) {
						isContain = true;
						rs.close();
					}
				}
				if(isContain) {
					String UpdateSql = "UPDATE "+table+" SET "+"score = '"+p.getScore()+"' "
						+ "todayscore = '" + p.getTodayscore() + "' " + "killscore = '" + p.getKillscore() + "' "
						+ "title = '" + p.getTitle() + "' " + "level = '" + p.getLevel() + "' " + "deathtime = '"
						+ p.getDeathtime() + "' " + "WHERE player = " + name + ";";
		            PreparedStatement ps = c.prepareStatement(UpdateSql);
				// 执行sql语句
		            ps.executeUpdate();
				ps.close();
			} else {
				String insertsql = "insert into " + table + " values (?,?,?,?,?,?,?);";
				PreparedStatement ps = c.prepareStatement(insertsql);
				ps.setString(0, name);
				ps.setInt(1, p.getScore());
				ps.setInt(2, p.getTodayscore());
				ps.setInt(3, p.getKillscore());
				ps.setString(4, p.getTitle());
				ps.setInt(5, p.getLevel());
				ps.setInt(6, p.getDeathtime());
				statement.executeUpdate(insertsql);
				}
			// 执行sql语句

		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static HashMap<String, Integer> get() {
		HashMap<String, Integer> hm = new HashMap();
		try {
			Statement statement = c.createStatement();// 操作数据库
			String sql = "select * from " + table;// 要执行的sql语句
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				hm.put(rs.getString("player"), rs.getInt("killscore"));
			}
			rs.close();
		} catch (Exception e) {

		}
		return hm;
	}
	public void load(String name) {
		try {
			Statement statement = c.createStatement();// 操作数据库
			String sql = "select * from " + table;// 要执行的sql语句
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				if (rs.getString("player").equalsIgnoreCase(name)) {
					String player = rs.getString("player");
					int score = rs.getInt("score");
					int todayscore = rs.getInt("todayscore");
					int killscore = rs.getInt("killscore");
					String title = rs.getString("title");
					int level = rs.getInt("level");
					int deathtime = rs.getInt("deathtime");
					Main.pvpers.put(Bukkit.getPlayer(name), new Pvper(player, score, killscore, level, todayscore, title, deathtime));
					rs.close();
					return;
				}
			}
			Main.pvpers.put(Bukkit.getPlayer(name), new Pvper(name, 0, 0, 0, 0, Main.config.getConfigurationSection("level").getConfigurationSection("0").getString("name"), 0));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

