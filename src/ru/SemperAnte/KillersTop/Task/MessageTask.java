package ru.SemperAnte.KillersTop.Task;

import org.bukkit.Bukkit;
import ru.SemperAnte.KillersTop.KillersTop;

import java.util.Map;

public class MessageTask implements Runnable
{
    @Override
    public void run()
    {
        Map<String, Integer> top = KillersTop.getMySQL().getStringIntegerMap("SELECT playername, kills FROM `Top_Kills` ORDER BY kills LIMIT 5", "playername", "kills");
        StringBuilder str = new StringBuilder();
        str.append(KillersTop.getLangUtils().castString("message")).append("\n");
        for (String s : top.keySet())
            str.append("\t").append(s).append(": ").append(top.get(s)).append("\n");
        Bukkit.broadcastMessage(str.toString());
    }
}
