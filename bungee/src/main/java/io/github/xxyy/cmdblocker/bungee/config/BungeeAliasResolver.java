/*
 * Command Blocker Ultimate
 * Copyright (C) 2014-2017 Philipp Nowak / Literallie (xxyy.github.io)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.github.xxyy.cmdblocker.bungee.config;

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import io.github.xxyy.cmdblocker.common.config.AliasResolver;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collection;

/**
 * Resolves aliases on BungeeCord proxy servers.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.7.14
 */
public class BungeeAliasResolver implements AliasResolver {

    private Map<String, Command> commandMap;
    private final Plugin plugin;

    public BungeeAliasResolver(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void refreshMap() {
        this.commandMap = stealCommandMap(plugin);
    }

    @Override
    public List<String> resolve(String commandName) {
        List<String> emptyList = Collection.emptyList();
        return emptyList;
    }

    private Map<String, Command> stealCommandMap(Plugin plugin) {
        try {
            Field commandMapField = plugin.getProxy().getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Command> commandMap = (Map<String, Command>) commandMapField.get(plugin.getProxy().getPluginManager());

            return commandMap;
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
            plugin.getLogger().warning("Could not get BungeeCord command map! That probably means that they changed their internals." +
                    "Please open an issue at https://github.com/xxyy/commandblockerultimate/issues! Thank you!");
            plugin.getLogger().warning("This means that we can't get aliases for commands. Sorry for that!");
        }

        return null;
    }
}
