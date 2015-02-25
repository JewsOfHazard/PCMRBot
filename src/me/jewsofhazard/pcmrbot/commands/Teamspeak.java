package me.jewsofhazard.pcmrbot.commands;

import me.jewsofhazard.pcmrbot.util.CLevel;

public class Teamspeak extends Command implements ICommand {

	private CLevel level=CLevel.Normal;

	@Override
	public CLevel getCommandLevel() {
		return level;
	}
	
	@Override
	public String getCommandText() {
		return "teamspeak";
	}
	
	@Override
	public String execute(String channel, String sender, String... parameters) {
		return "You can download teamspeak here:  http://www.teamspeak.com/?page=downloads";
	}

}
