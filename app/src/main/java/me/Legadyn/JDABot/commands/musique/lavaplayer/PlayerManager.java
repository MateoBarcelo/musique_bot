package me.Legadyn.JDABot.commands.musique.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager manager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.manager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);

    }

    public GuildMusicManager getMusicManager(Guild guild) {

        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.manager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });

    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {

        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.manager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                channel.sendMessage("`Poniendo en la cola: `")
                        .append(track.getInfo().title)
                        .append("\n`Por: `")
                        .append(track.getInfo().author)
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                if (!playlist.isSearchResult()) {

                    final List<AudioTrack> tracks = playlist.getTracks();

                    channel.sendMessage("`Poniendo en la cola una playlist con:` ")
                            .append(String.valueOf(tracks.size())).append(" videos")
                            .append("\n`Nombre:` ")
                            .append(playlist.getName())
                            .queue();

                    for (final AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                    }

                } else {

                    final List<AudioTrack> tracks = playlist.getTracks();
                    AudioTrack track = tracks.get(0);
                    channel.sendMessage("`Poniendo en la cola: `")
                            .append(track.getInfo().title)
                            .append("\n`Por: `")
                            .append(track.getInfo().author)
                            .queue();

                    musicManager.scheduler.queue(track);
                }
            }
            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });


    }

    public static PlayerManager getInstance() {

        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
