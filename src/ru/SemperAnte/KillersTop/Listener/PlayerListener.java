package ru.SemperAnte.KillersTop.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.SemperAnte.KillersTop.KillersTop;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        addToDB(event.getPlayer().getName().toLowerCase());
    }

    @EventHandler
    public void onKills(EntityDeathEvent event)
    {
        if (event.getEntity() instanceof Player && event.getEntity().getKiller() != null)
            addPoint(event.getEntity().getKiller().getName());
    }

    private void addToDB(String playername)
    {
        if (!KillersTop.getMySQL().getString("SELECT playername FROM `Top_kills` WHERE playername = '%s'", playername).equalsIgnoreCase(playername))
            KillersTop.getMySQL().update("INSERT INTO `Top_Kills` (`playername`, `kills`) VALUES ('%s', 0);", playername);
    }

    private void addPoint(String playername)
    {
        KillersTop.getMySQL().update("UPDATE `Top_Kills` SET kills = kills+1 WHERE playername = '%s'", playername);
    }
}
