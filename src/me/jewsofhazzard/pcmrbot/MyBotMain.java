/*	  It's a Twitch bot, because we can.
 *    Copyright (C) 2015  Logan Ssaso, James Wolff, Angablade
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.jewsofhazzard.pcmrbot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.jewsofhazzard.pcmrbot.database.Database;
import me.jewsofhazzard.pcmrbot.util.Options;
import me.jewsofhazzard.pcmrbot.util.TFileReader;
import me.jewsofhazzard.pcmrbot.util.TFileWriter;

import org.jibble.pircbot.IrcException;

public class MyBotMain implements Runnable {

	private String channel;
	private static final HashMap<String, IRCBot> connectedChannels = new HashMap<>();
	private static final String botChannel = "#pcmrbot";
	private static String oAuth = "";
	private static final Logger logger = Logger.getLogger(MyBotMain.class + "");

	/**
	 * Creates a new instance of MyBotMain (called whenever the bot joins a new
	 * channel).
	 * 
	 * @param channel
	 *            - the channel we are joining
	 */
	public MyBotMain(String channel) {
		this.channel = channel;
		new Thread(this).start();

	}

	/**
	 * Performs all of the setup for the bot, both on first run, and all
	 * subsequent runs.
	 * 
	 * @param args
	 *            - the oAuth for the bot is passed on the command-line
	 */
	public static void main(String[] args) {
		Database.initDBConnection(args[1]);
		if (Database.getMainTables()) {
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ getBotChannel().substring(1) + "Mods VALUES(\'pcmrbot\')");
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ getBotChannel().substring(1)
					+ "Mods VALUES(\'j3wsofhazard\')");
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ getBotChannel().substring(1)
					+ "Mods VALUES(\'donald10101\')");
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ getBotChannel().substring(1)
					+ "Mods VALUES(\'angablade\')");
			Database.setOption(
					getBotChannel().substring(1),
					Options.welcomeMessage,
					"Welcome %user% to our channel, may you find it entertaining or flat out enjoyable.");
			Database.setOption(getBotChannel().substring(1), Options.numCaps, "10");
			Database.setOption(getBotChannel().substring(1), Options.numEmotes, "10");
			Database.setOption(getBotChannel().substring(1), Options.numSymbols, "10");
			Database.setOption(getBotChannel().substring(1), Options.paragraphLength, "250");
		}
		oAuth = args[0];
		getConnectedChannels().put(getBotChannel(),
				new IRCBot(getBotChannel()));

		getConnectedChannels().get(getBotChannel()).setVerbose(true);
		try {
			getConnectedChannels().get(getBotChannel()).connect(
					"irc.twitch.tv", 6667, oAuth);
		} catch (IOException | IrcException e) {
			logger.log(Level.SEVERE, "An error occurred while connecting to "
					+ getBotChannel(), e);
		}
		getConnectedChannels().get(getBotChannel())
				.joinChannel(getBotChannel());
		File f = new File("connectedChannels.txt");
		if (f.exists()) {
			for (String s : TFileReader.readFile(f)) {
				new MyBotMain(s);
			}
			f.delete();
		}

	}

	/**
	 * Performs all of the setup for the bot in the channel specified, both on
	 * first run, and all subsequent runs.
	 */
	public void run() {

		if (connectedChannels.containsKey(channel)) {
			return;
		}

		boolean firstTime = false;
		if (Database.getChannelTables(channel.substring(1))) {
			firstTime = true;
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ channel.substring(1) + "Mods VALUES(\'pcmrbot\')");
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ channel.substring(1) + "Mods VALUES(\'j3wsofhazard\')");
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ channel.substring(1) + "Mods VALUES(\'donald10101\')");
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ channel.substring(1) + "Mods VALUES(\'angablade\')");
			Database.executeUpdate("INSERT INTO " + Database.DATABASE + "."
					+ channel.substring(1) + "Mods VALUES(\'"
					+ channel.substring(1) + "\')");
			Database.setOption(
					channel.substring(1),
					Options.welcomeMessage,
					"Welcome %user% to our channel, may you find it entertaining or flat out enjoyable.");
			Database.setOption(channel.substring(1), Options.numCaps, "10");
			Database.setOption(channel.substring(1), Options.numEmotes, "10");
			Database.setOption(channel.substring(1), Options.numSymbols, "10");
			Database.setOption(channel.substring(1), Options.paragraphLength, "250");
		}
		getConnectedChannels().put(channel, new IRCBot(channel));

		getConnectedChannels().get(channel).setVerbose(true);
		try {
			getConnectedChannels().get(channel).connect("irc.twitch.tv", 6667,
					oAuth);
		} catch (IrcException | IOException e) {
			logger.log(Level.SEVERE, "An error occurred while connecting to "
					+ getBotChannel(), e);
		}
		getConnectedChannels().get(channel).joinChannel(channel);
		if (firstTime) {
			getConnectedChannels().get(channel).onFirstJoin();
		}

	}

	public static void shutdown() {
		ArrayList<String> channels = new ArrayList<>();
		for (IRCBot bot : connectedChannels.values()) {

			if (!bot.getChannel().equalsIgnoreCase("#pcmrbot")) {
				channels.add(bot.getChannel());
				bot.sendMessage(bot.getChannel(),
						"I am shutting down, I will automatically rejoin your channel when I restart!");
			}

		}
		TFileWriter.overWriteFile(new File("connectedChannels.txt"), channels);
		System.exit(0);
	}

	public static HashMap<String, IRCBot> getConnectedChannels() {
		return connectedChannels;
	}

	public static String getBotChannel() {
		return botChannel;
	}

	public static IRCBot getConnectedChannel(String channel) {
		return connectedChannels.get(channel);
	}

}
