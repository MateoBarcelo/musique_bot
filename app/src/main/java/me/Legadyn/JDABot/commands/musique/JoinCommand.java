package me.Legadyn.JDABot.commands.musique;

import me.Legadyn.JDABot.commands.CommandContext;
import me.Legadyn.JDABot.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember(); //bot
        final GuildVoiceState voiceState = selfMember.getVoiceState(); //voicestate of the bot

        if(!selfMember.hasPermission(Permission.VOICE_CONNECT)) {
            channel.sendMessage("No tengo permisos para acceder a los canales de voz.");
            return;
        }

        if(voiceState.inVoiceChannel()) {
            channel.sendMessage("No puedes reproducir música, estoy en otro canal de voz.").queue();
            return;
        }

        final Member member = ctx.getMember(); //member that put the command
        final GuildVoiceState memberState = member.getVoiceState();

        if(!memberState.inVoiceChannel()) {
            channel.sendMessage("No puedes reproducir musica, tienes que estar en un canal de voz.").queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberState.getChannel();

        audioManager.openAudioConnection(memberChannel); //Con esto se conecta al canal, se abre una conexión para que pueda entrar el bot
        channel.sendMessageFormat("Conectando al canal... ( %s )", memberChannel.getName()).queue();

    }

    @Override
    public String getName() {
        return "join";
    }
}
