package me.Legadyn.JDABot.commands.musique;

import me.Legadyn.JDABot.commands.CommandContext;
import me.Legadyn.JDABot.commands.ICommand;
import me.Legadyn.JDABot.commands.musique.lavaplayer.GuildMusicManager;
import me.Legadyn.JDABot.commands.musique.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class StopCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState voiceState = selfMember.getVoiceState();

        if (!voiceState.inVoiceChannel()) {
            channel.sendMessage("No puedo parar musica si no estoy en un canal de voz, usa -join para meterme.").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Tienes que estar en un canal de voz para que pueda parar la musica.").queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(voiceState.getChannel())) {
            channel.sendMessage("Tenemos que estar en el mismo canal para que pueda parar la musica.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        channel.sendMessageFormat("La canción %s", musicManager.player.getPlayingTrack(), " se paró correctamente.");
    }
    @Override
    public String getName() {
        return "stop";
    }
}
