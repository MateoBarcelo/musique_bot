package me.Legadyn.JDABot.commands;

import me.Legadyn.JDABot.commands.musique.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

public class PlayCommand implements ICommand{
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember(); //bot
        final GuildVoiceState voiceState = selfMember.getVoiceState(); //voicestate of the bot

        if(ctx.getArgs().isEmpty()) {
            channel.sendMessage("`Usa -play <link> para reproducir una cancion.`").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!voiceState.inVoiceChannel()) {
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();

            audioManager.openAudioConnection(memberChannel); //Con esto se conecta al canal, se abre una conexión para que pueda entrar el bot
            channel.sendMessageFormat("Conectando al canal... ( %s )", memberChannel.getName()).queue();
        }

        if(!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Tienes que estar en un canal de voz para que pueda reproducir musica.").queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(voiceState.getChannel()) && voiceState.inVoiceChannel()) {
            channel.sendMessage("Tenemos que estar en el mismo canal para que pueda reproducir musica.").queue();
            return;
        }

        String link = String.join(" ", ctx.getArgs());

        if(!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getInstance().loadAndPlay(channel, link);

        //PROBLEMA - SOLUCIÓN: Hacer que ponga playlist solo cuando en la url hay una playlist
    }

    private boolean isUrl(String link) {
    /*
    * SI SE PUEDE HACER UN URI CON UN LINK CON URL ESPECIFICADA, NO DA ERROR, DE OTRA FORMA DA FALSE PORQUE NO ES UNA URL VÁLIDA
    *
    * */
        try{
            new URI(link);
            return true;
        } catch(URISyntaxException e) {
            return false;
        }

    }

    @Override
    public String getName() {
        return "play";
    }
}
