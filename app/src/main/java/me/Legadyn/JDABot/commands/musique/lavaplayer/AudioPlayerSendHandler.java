package me.Legadyn.JDABot.commands.musique.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {

    private final AudioPlayer player;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer player) {
        this.player = player;
        this.buffer = ByteBuffer.allocate(1024); //cantidad que el buffer va a dar cada 20ms
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
    }


    @Override
    public boolean canProvide() {
        return this.player.provide(this.frame); //si esto es true, va a llamar al provide20msAudio
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return this.buffer.flip(); //flip setea la posición del buffer a 0 asi puede empezar
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
