package me.Legadyn.JDABot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class Bot {

    private Bot() throws LoginException {

        JDA builder = JDABuilder.createDefault("ODg2OTk1NDczMjc2MjM1OTA3.YT9s6A.r5OCHZlhLqHmiY-PYYKe4ZlYvrE", GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                                    .disableCache(EnumSet.of(CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)).enableCache(CacheFlag.VOICE_STATE)
                                    .addEventListeners(new Listener()).setActivity(Activity.listening("Musique")).build();


    }



     public static void main(String[] args) throws LoginException {
        new Bot();
    }





}
