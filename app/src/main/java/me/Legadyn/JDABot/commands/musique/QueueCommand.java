package me.Legadyn.JDABot.commands.musique;

import me.Legadyn.JDABot.commands.CommandContext;
import me.Legadyn.JDABot.commands.ICommand;
import me.Legadyn.JDABot.commands.musique.lavaplayer.GuildMusicManager;
import me.Legadyn.JDABot.commands.musique.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState voiceState = selfMember.getVoiceState();

        if (!voiceState.inVoiceChannel()) {
            channel.sendMessage("No puedo obtener informacion si no estoy en un canal de voz, usa -join para meterme.").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Tienes que estar en un canal de voz para que pueda obtener esta informacion.").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(voiceState.getChannel())) {
            channel.sendMessage("Tenemos que estar en el mismo canal para que pueda obtener esta informacion.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        String nowPlay, nextPlay;

        try {
            nowPlay = musicManager.player.getPlayingTrack().getInfo().title + " | " + musicManager.player.getPlayingTrack().getInfo().author;
        } catch(Exception e) {
            nowPlay = "Nada ";
        }
        try {
            nextPlay = musicManager.scheduler.queue.iterator().next().getInfo().uri;
        } catch (Exception e) {
            nextPlay = "Ninguno";
        }
            channel.sendMessage("`Ahora reproduciendo... `").append(nowPlay).append("`\nSiguiente video: `").append(nextPlay).queue();

    }
    @Override
    public String getName() {
        return "queue";
    }
}
