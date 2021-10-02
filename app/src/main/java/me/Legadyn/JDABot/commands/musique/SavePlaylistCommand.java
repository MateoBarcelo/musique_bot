package me.Legadyn.JDABot.commands.musique;

import me.Legadyn.JDABot.commands.CommandContext;
import me.Legadyn.JDABot.commands.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class SavePlaylistCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember(); //bot
        List<String> args = ctx.getArgs();

        if(args.size() < 1) {
           channel.sendMessage("`Usa -saveplaylist <nombre> <link> para guardar una playlist`").queue();
           return;
        }

        JSONObject playlist = new JSONObject();
        playlist.put(args.get(0), args.get(1));

        try {

            FileWriter writer = new FileWriter("app/src/main/resources/playlists.json");
            writer.write(playlist.toJSONString());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            channel.sendMessage("`No se pudo guardar la playlist, intentalo de nuevo.`").queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "saveplaylist";
    }
}
