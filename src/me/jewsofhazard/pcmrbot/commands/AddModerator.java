package me.jewsofhazard.pcmrbot.commands;

import me.jewsofhazard.pcmrbot.database.Database;
import me.jewsofhazard.pcmrbot.util.CommandLevel;

public class AddModerator extends Command {

	private CommandLevel level = CommandLevel.Mod;

	@Override
	public CommandLevel getCommandLevel() {
		return level;
	}

	@Override
	public String getCommandText() {
		return "addmoderator";
	}

	@Override
	/**
	 * Add's a moderator to the channel's Mod's table in the database if they are not already in it.
	 * 
	 * @return a formatted message with the results of the method
	 */
	public String execute(String channel, String sender, String... parameters) {

		String moderator = parameters[0];
		if (Database.isMod(moderator, channel.substring(1))) {
			return "%mod% is already a moderator!".replace("%mod%", moderator);
		} else {
			Database.addMod(moderator, channel.substring(1));
			return String.format(
					"Successfully added %s to the bots mod list for %s!",
					moderator, channel);
		}

	}

}
