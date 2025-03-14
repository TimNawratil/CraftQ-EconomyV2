package de.craftq.tim.listener;

import de.craftq.tim.mysql.EconomyMySQLAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();

        if (!EconomyMySQLAPI.playerExists(playerUUID.toString())) {
            EconomyMySQLAPI.createPlayer(playerUUID.toString(), playerName);
        } else {
            EconomyMySQLAPI.updatePlayerName(playerUUID.toString(), playerName);
        }
    }


}
