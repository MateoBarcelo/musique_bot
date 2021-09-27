package me.Legadyn.JDABot.commands.musique;

import me.Legadyn.JDABot.commands.CommandContext;
import me.Legadyn.JDABot.commands.ICommand;
import me.Legadyn.JDABot.commands.musique.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class ClearCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState voiceState = selfMember.getVoiceState();

        if (!voiceState.inVoiceChannel()) {
            channel.sendMessage("No puedo limpiar la cola si no estoy en un canal de voz, usa -join para meterme.").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Tienes que estar en un canal de voz para que pueda limpiar la cola.").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(voiceState.getChannel())) {
            channel.sendMessage("Tenemos que estar en el mismo canal para que pueda limpiar la cola.").queue();
            return;
        }

        channel.sendMessageFormat("La cola ha sido limpiada con exito; `%s` videos limpiados.", PlayerManager.getInstance().getMusicManager(ctx.getGuild()).scheduler.queue.size()).queue();
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).scheduler.queue.clear();
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).player.stopTrack();
    }

    @Override
    public String getName() {
        return "clear";
    }
}
