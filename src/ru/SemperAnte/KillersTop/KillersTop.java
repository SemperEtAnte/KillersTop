package ru.SemperAnte.KillersTop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import ru.SemperAnte.KillersTop.Listener.PlayerListener;
import ru.SemperAnte.KillersTop.Task.MessageTask;
import ru.SemperAnte.plugins.API.MainAPI;
import ru.SemperAnte.plugins.API.Utils.ConfigUtils;
import ru.SemperAnte.plugins.API.Utils.LangUtils;
import ru.SemperAnte.plugins.API.Utils.LoggerUtils;
import ru.SemperAnte.plugins.API.UtilsBase.MySQL;

public final class KillersTop extends JavaPlugin
{
    private static ConfigUtils CU;
    private static LangUtils LU;
    private static MySQL MYSQL;
    private int task;
    private int period;

    public void onEnable()
    {
        if (MainAPI.isMustEnable(this))
        {
            CU = new ConfigUtils(this);
            LU = new LangUtils(this);
            MYSQL = new MySQL(this, "MySQL");
            LoggerUtils logger = new LoggerUtils(this);
            period = CU.getInt("period") * 20;
            if (!MYSQL.hasConnection())
            {
                logger.debugError("I can't connect to MySQL db. I will not work.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
            else
                MYSQL.update("CREATE TABLE IF NOT EXISTS Top_Kills(playername VARCHAR(50), kills INT");
            Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MessageTask(), period, period);
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender.hasPermission("KillersTop.admin"))
        {
            try
            {
                String commands = args[0].toLowerCase();
                switch (commands)
                {
                    case "reload":
                        Bukkit.getScheduler().cancelTask(task);
                        CU.reloadConfig();
                        LU.reloadLang();
                        period = CU.getInt("period") * 20;
                        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MessageTask(), period, period);
                        return true;
                    case "period":
                        period = Integer.valueOf(args[1]) * 20;
                        CU.set("period", period);
                        Bukkit.getScheduler().cancelTask(task);
                        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MessageTask(), period, period);
                        return true;
                }
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                sender.sendMessage("/kt reload - reload config");
                sender.sendMessage("/kt period <period> - set message period");
            }
            catch (NumberFormatException e)
            {
                sender.sendMessage(ChatColor.RED + " Period must be a number!");
            }
        }
        else
            sender.sendMessage("У Вас недостаточно прав.");
        return true;
    }

    public static MySQL getMySQL()
    {
        return MYSQL;
    }

    public static LangUtils getLangUtils()
    {
        return LU;
    }
}
