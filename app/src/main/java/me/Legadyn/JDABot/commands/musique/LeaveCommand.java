package me.Legadyn.JDABot.commands.musique;

import me.Legadyn.JDABot.commands.CommandContext;
import me.Legadyn.JDABot.commands.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState voiceState = selfMember.getVoiceState();

        if (!voiceState.inVoiceChannel()) {
            channel.sendMessage("No puedo salirme si no estoy en un canal de voz, usa -join para meterme.").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Tienes que estar en un canal de voz para que pueda salirme.").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(voiceState.getChannel())) {
            channel.sendMessage("Tenemos que estar en el mismo canal para que pueda salirme.").queue();
            return;
        }

        ctx.getGuild().getAudioManager().closeAudioConnection();
        channel.sendMessage("Saliendo del canal... ").queue();
    }

    @Override
    public String getName() {
        return "leave";
    }
}
