package PvpTitles;

import java.util.Calendar;

public class Pvper {

	private int score;
	private int date;
	private int todayscore;
	private int killscore;
	private String title;
	private int level;
	private int deathtime;
	private String player;

	public Pvper(String player, int score, int killscore, int level, int todayscore, String title, int deathtime) {
		this.player = player;
		this.deathtime = deathtime;
		this.score = score;
		this.killscore = killscore;
		this.level = level;
		this.todayscore = todayscore;
		this.title = title;
		this.setData(Calendar.getInstance().DAY_OF_YEAR);

	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getKillscore() {
		return killscore;
	}

	public void setKillscore(int killscore) {
		this.killscore = killscore;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getDeathtime() {
		return deathtime;
	}

	public void setDeathtime() {
		Calendar c = Calendar.getInstance();
		int time = c.SECOND + c.MINUTE * 60 + c.HOUR_OF_DAY * 60 * 60;
		this.deathtime = time;
	}

	public int getTodayscore() {
		return todayscore;
	}

	public void setTodayscore(int todayscore) {
		this.todayscore = todayscore;
	}

	public int getData() {
		return date;
	}

	public void setData(int data) {
		this.date = data;
	}
}
