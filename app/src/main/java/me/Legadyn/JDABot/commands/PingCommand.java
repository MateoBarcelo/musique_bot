package me.Legadyn.JDABot.commands;

import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand{
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel().sendMessageFormat("PingDiscord:%sMs\nWebSocketPing: %sms", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getName() {
        return "ping"; //NOMBRE DEL COMANDO
    }
}
