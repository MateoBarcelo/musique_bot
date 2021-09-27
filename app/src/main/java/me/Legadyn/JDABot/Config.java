package me.Legadyn.JDABot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(ConfigKeys key) {

        switch (key) {
            case TOKEN: return dotenv.get("TOKEN");
            case PREFIX: return dotenv.get("PREFIX");
            case CREADOR: return dotenv.get("CREADOR");
            default: break;
        }
        return null;
    }

}
