/*
 * Copyright (C) 2013-2014 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.bukkitdev.skywars.commands.mainsubcommands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.daboross.bukkitdev.commandexecutorbase.SubCommand;
import net.daboross.bukkitdev.commandexecutorbase.filters.ArgumentFilter;
import net.daboross.bukkitdev.skywars.api.SkyWars;
import net.daboross.bukkitdev.skywars.api.game.SkyIDHandler;
import net.daboross.bukkitdev.skywars.api.translations.SkyTrans;
import net.daboross.bukkitdev.skywars.api.translations.TransKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CancelCommand extends SubCommand {

    private final SkyWars plugin;

    public CancelCommand(SkyWars plugin) {
        super("cancel", true, "skywars.cancel", SkyTrans.get(TransKey.CMD_CANCEL_DESCRIPTION));
        addArgumentNames(SkyTrans.get(TransKey.CMD_CANCEL_ARGUMENT));
        this.addCommandFilter(new ArgumentFilter(ArgumentFilter.ArgumentCondition.LESS_THAN, 2, SkyTrans.get(TransKey.TOO_MANY_PARAMS)));
        this.addCommandFilter(new ArgumentFilter(ArgumentFilter.ArgumentCondition.GREATER_THAN, 0, SkyTrans.get(TransKey.NOT_ENOUGH_PARAMS)));
        this.plugin = plugin;
    }

    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        int id;
        try {
            id = Integer.parseInt(subCommandArgs[0]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(SkyTrans.get(TransKey.NOT_AN_INTEGER, subCommandArgs[0]));
            return;
        }
        SkyIDHandler idh = plugin.getIDHandler();
        if (idh.getGame(id) == null) {
            sender.sendMessage(SkyTrans.get(TransKey.CMD_CANCEL_NO_GAMES_WITH_ID, id));
            return;
        }
        sender.sendMessage(SkyTrans.get(TransKey.CMD_CANCEL_CONFIRMATION, id));
        plugin.getGameHandler().endGame(id, true);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final Command baseCommand, final String baseCommandLabel, final SubCommand subCommand, final String subCommandLabel, final String[] subCommandArgs) {
        if (subCommandArgs.length > 1) {
            return Collections.emptyList();
        }
        ArrayList<String> resultList = new ArrayList<>();
        Collection<Integer> currentIds = plugin.getIDHandler().getCurrentIDs();
        for (Integer id : currentIds) {
            String idString = id.toString();
            if (idString.startsWith(subCommandArgs[0])) {
                resultList.add(idString);
            }
        }
        return resultList;
    }
}
