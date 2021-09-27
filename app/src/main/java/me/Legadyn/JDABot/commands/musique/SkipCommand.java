package me.Legadyn.JDABot.commands.musique;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.Legadyn.JDABot.commands.CommandContext;
import me.Legadyn.JDABot.commands.ICommand;
import me.Legadyn.JDABot.commands.musique.lavaplayer.GuildMusicManager;
import me.Legadyn.JDABot.commands.musique.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState voiceState = selfMember.getVoiceState();

        if (!voiceState.inVoiceChannel()) {
            channel.sendMessage("No puedo saltar canciones si no estoy en un canal de voz, usa -join para meterme.").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Tienes que estar en un canal de voz para que pueda saltar una canción.").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(voiceState.getChannel())) {
            channel.sendMessage("Tenemos que estar en el mismo canal para que pueda saltar la musica.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;

        try {
            if (musicManager.scheduler.queue.remainingCapacity() > 0) {
                musicManager.scheduler.nextTrack();

                channel.sendMessageFormat("`Saltando a... `")
                        .append(player.getPlayingTrack().getInfo().title).append("\n`Por:`")
                        .append(player.getPlayingTrack().getInfo().author).append("\n`Link: `")
                        .append(player.getPlayingTrack().getInfo().uri).queue();
            }
        } catch(Exception e) {
            channel.sendMessage("La cola esta vacia, no se puede saltar nada.").queue();
        }

    }

    @Override
    public String getName() {
        return "skip";
    }
}
