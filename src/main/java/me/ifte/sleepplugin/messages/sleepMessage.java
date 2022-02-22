package me.ifte.sleepplugin.messages;

import me.ifte.sleepplugin.Sleepplugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class sleepMessage {


    public static void message(Player player) {
        TextComponent msgYes = new TextComponent(Sleepplugin.getPlugin().getConfig().getString("accept-hover"));
        msgYes.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        msgYes.setBold(true);
        msgYes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sleep accept"));
        msgYes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Sleepplugin.getPlugin().getConfig().getString("accept-text-on-hover"))));


        TextComponent msgNo = new TextComponent(Sleepplugin.getPlugin().getConfig().getString("deny-hover"));
        msgNo.setColor(net.md_5.bungee.api.ChatColor.RED);
        msgNo.setBold(true);
        msgNo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sleep deny"));
        msgNo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Sleepplugin.getPlugin().getConfig().getString("deny-text-on-hover"))));

        TextComponent msgStrike = new TextComponent("                                                                        \n\n");
        msgStrike.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        msgStrike.setStrikethrough(true);

        TextComponent msgVote = new TextComponent(Sleepplugin.getPlugin().getConfig().getString("vote-initiated-hover-msg"));
        msgVote.setBold(true);

        TextComponent message = new TextComponent("");
        message.addExtra(msgStrike);
        message.addExtra(msgVote);
        message.addExtra(new TextComponent("\n\n                      "));
        message.addExtra(msgYes);
        message.addExtra(" - ");
        message.addExtra(msgNo);
        message.addExtra(new TextComponent("\n\n"));
        message.addExtra(new TextComponent(msgStrike));

        player.spigot().sendMessage(message);
    }

}
