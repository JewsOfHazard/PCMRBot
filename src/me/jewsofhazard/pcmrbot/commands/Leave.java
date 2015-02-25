/*	  It's a Twitch bot, because we can.
 *    Copyright (C) 2015  Logan Saso, James Wolff, Kyle Nabinger
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

package me.jewsofhazard.pcmrbot.commands;

import me.jewsofhazard.pcmrbot.MyBotMain;
import me.jewsofhazard.pcmrbot.util.CLevel;

public class Leave extends Command {

	@Override
	public CLevel getCommandLevel() {
		return CLevel.Owner;
	}
	
	@Override
	public String getCommandText() {
		return "leave";
	}
	
	@Override
	public String execute(String channel, String sender, String...parameters){
		if (!channel.equalsIgnoreCase(MyBotMain.getBotChannel())) {
			MyBotMain.partChannel(channel);
			return "I have disconnected from %channel%'s channel.".replace("%channel%", channel);
		}
		return "Sorry %user%, I cannot allow you to disconnect me from my home channel.".replace("%user%", sender);
		
		
	}
	
	
}





