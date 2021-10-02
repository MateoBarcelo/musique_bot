package me.Legadyn.JDABot;

import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class); // para que loguee cuando la clase listener readyevent funque
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(ReadyEvent event) {

        LOGGER.info("{} esta listo! User: " + event.getJDA().getSelfUser());

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String prefix = Config.get(ConfigKeys.PREFIX);
        String message = event.getMessage().getContentRaw();
        if (!message.startsWith("-")) return;
        /*
        APAGADO
        (ONLY ADMIN)
         */
        if (message.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get(ConfigKeys.CREADOR))) {

            LOGGER.info("Apagado exitoso!");
            BotCommons.shutdown(event.getJDA());

        }
        try {
            manager.handle(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
