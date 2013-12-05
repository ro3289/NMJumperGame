package com.jumpergame;

public class users {
	private String Name = "Dummy";
	private int  Score   = 0;
	public users(String name, int score)
	{
		Name=name;
		Score=score;
	}
	public void setName(String name)
	{
		Name=name;
	}
	public void setScore(int score)
	{
		Score=score;
	}
	public String getName()
	{
		return Name;
	}
	public int getScore()
	{
		return Score;
	}
}
