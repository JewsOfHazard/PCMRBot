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

package me.jewsofhazard.pcmrbot.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.jewsofhazard.pcmrbot.Main;

public class Timeouts implements Runnable {

	private static final Logger logger = Logger.getLogger(Timeouts.class + "");

	private String channel;
	private String user;
	private int time;
	private TType type;

	public Timeouts(String c, String u, int t, TType type) {
		channel = c;
		user = u;
		time = t;
		this.type = type;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Unable to timeout user: " + user, e);
		}
		if (type.previousOffender(user)) {
			if (type.getOffender(user) == 1) {
				Main.getBot().sendMessage(channel,
						"/timeout " + user + " 300");
				Main.getBot().sendMessage(
						channel,
						type.getRandomMessage()
								+ " Second warning, 5 minute timeout!");
				type.updateOffender(user, 2);
			} else if (type.getOffender(user) == 2) {
				Main.getBot().sendMessage(channel,
						"/timeout " + user + " 600");
				Main.getBot().sendMessage(
						channel,
						type.getRandomMessage()
								+ "Third warning, 10 minute timeout!");
				type.updateOffender(user, 3);
			} else if (type.getOffender(user) == 3) {
				Main.getBot().sendMessage(channel,
						"/timeout " + user + " 900");
				Main.getBot().sendMessage(
						channel,
						type.getRandomMessage()
								+ " Fourth warning, 15 minute timeout!");
				type.updateOffender(user, 4);
			} else if (type.getOffender(user) == 4) {
				Main.getBot().sendMessage(channel,
						"/timeout " + user + " 1200");
				Main.getBot().sendMessage(
						channel,
						type.getRandomMessage()
								+ " Fifth warning, 20 minute timeout!");
				type.updateOffender(user, 5);
			} else {
				Main.getBot().sendMessage(channel,
						"/timeout " + user + " 7200");
				Main
						.getBot()
						.sendMessage(
								channel,
								type.getRandomMessage()
										+ " Sixth warning, 2 hour timeout! You dun' goofed!");
				type.removeOffender(user);
			}
		} else {
			type.updateOffender(user, 1);
			Main.getBot().sendMessage(channel,
					"/timeout " + user + " " + time);
			Main.getBot().sendMessage(channel,
					type.getRandomMessage() + " First warning");
		}
	}
}
