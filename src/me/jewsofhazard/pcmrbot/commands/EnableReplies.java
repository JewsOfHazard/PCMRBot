package me.jewsofhazard.pcmrbot.commands;

import me.jewsofhazard.pcmrbot.Main;
import me.jewsofhazard.pcmrbot.util.CommandLevel;

public class EnableReplies extends Command {

	private CommandLevel level = CommandLevel.Mod;

	@Override
	public CommandLevel getCommandLevel() {
		return level;
	}

	@Override
	public String getCommandText() {
		return "enablereplies";
	}

	@Override
	public String execute(String channel, String sender, String... parameters) {
		Main.getBot().setConfirmationEnabled(channel, true);
		return "%user% has disabled bot replies".replace("%user%", sender);
	}

}
