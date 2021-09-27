package me.Legadyn.JDABot;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.Legadyn.JDABot.Config;
import me.Legadyn.JDABot.ConfigKeys;
import me.Legadyn.JDABot.commands.CommandContext;
import me.Legadyn.JDABot.commands.ICommand;
import me.Legadyn.JDABot.commands.PingCommand;
import me.Legadyn.JDABot.commands.PlayCommand;
import me.Legadyn.JDABot.commands.musique.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() { //add commands in the constructor
        addCommand(new PingCommand());
        addCommand(new JoinCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new QueueCommand());
        addCommand(new ClearCommand());
        addCommand(new LeaveCommand());
    }

    private void addCommand(ICommand cmd) {

        boolean commandFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName())); //Comprobar si ya existe el comando que se quiere agregar

        if(commandFound) {
            throw new IllegalArgumentException("This command has been created");
        }

        commands.add(cmd);
    }

    @Nullable
    private ICommand getCommand(String command) {

        String commandLower = command.toLowerCase();

        for(ICommand cmd : this.commands) {

            if(cmd.getName().equalsIgnoreCase(commandLower)) {
                return cmd;
            }

        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event) throws InterruptedException {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Objects.requireNonNull(Config.get(ConfigKeys.PREFIX))), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke); //Usamos el metodo de arriba para obtener el comando

        if(cmd != null) {

            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length); //Cortamos el prefix hasta el largo del comando para guardarlo como un argumento

            CommandContext ctx = new CommandContext(event, args);
            cmd.handle(ctx);
        }
    }


}
